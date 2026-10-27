package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.NexusPureBlack
import kotlinx.coroutines.isActive
import java.util.Random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Data model for a distinct floating dot-matrix wave particle.
 * Sized 4dp to 8dp with individual depth, parallax, and gentle wave dynamics.
 */
private class OceanParticle(
    val baseNormX: Float,
    val baseNormY: Float,
    val diameterDp: Float,
    val depthLayer: Float, // 0.35f (far) to 1.0f (near)
    val phaseX: Float,
    val phaseY: Float,
    val driftSpeed: Float,
    val colorGroup: Int,
    val baseAlpha: Float
)

/**
 * Aesthetic palette for background ocean particles:
 * Dominant: Silver, Metallic Grey, Pearl White, Ice Blue 🩵
 * Accents: Mint, Soft Lavender, Subtle Rose Pink
 */
private val BgColorSilver = Color(0xFFCBD5E1)
private val BgColorMetallicGrey = Color(0xFF94A3B8)
private val BgColorPearlWhite = Color(0xFFF8FAFC)
private val BgColorIceBlue = Color(0xFFBAE6FD)
private val BgColorMint = Color(0xFFA7F3D0)
private val BgColorLavender = Color(0xFFDDD6FE)
private val BgColorRosePink = Color(0xFFFBCFE8)

/**
 * Premium Futuristic Pure-Black Ocean-Wave Dot-Matrix Background.
 *
 * Renders a full-screen, continuously animated ocean-like wave surface composed of
 * noticeably visible 4–8dp floating particles with subtle depth, glow, parallax, and
 * organic wave undulation.
 *
 * Adapts smoothly to 120Hz, 90Hz, and 60Hz displays with frame-rate-independent timing.
 * Sits behind all UI elements and never consumes touch events.
 */
@Composable
fun NexusOceanParticleBackground(
    modifier: Modifier = Modifier,
    dimAroundCenter: Boolean = true,
    reducedMotion: Boolean = false
) {
    // Generate pre-allocated deterministic particle array with varied 4–8dp diameters
    val particleCount = 112
    val particles = remember {
        val list = ArrayList<OceanParticle>(particleCount)
        val rng = Random(133742L)

        // Varied sizes from 4.0dp to 8.0dp
        val sizeBuckets = floatArrayOf(4.0f, 4.8f, 5.5f, 6.2f, 7.0f, 8.0f)

        for (i in 0 until particleCount) {
            val gridCol = i % 8
            val gridRow = i / 8
            val jitterX = (rng.nextFloat() - 0.5f) * 0.12f
            val jitterY = (rng.nextFloat() - 0.5f) * 0.08f

            val normX = ((gridCol + 0.5f) / 8f + jitterX).coerceIn(0.02f, 0.98f)
            val normY = ((gridRow + 0.5f) / 14f + jitterY).coerceIn(0.02f, 0.98f)

            val size = sizeBuckets[rng.nextInt(sizeBuckets.size)]
            val depth = 0.35f + (size - 4.0f) / 4.0f * 0.65f // Larger particles feel closer
            val phaseX = rng.nextFloat() * (2f * PI.toFloat())
            val phaseY = rng.nextFloat() * (2f * PI.toFloat())
            val speed = 0.65f + rng.nextFloat() * 0.55f
            val colorGroup = rng.nextInt(6)
            val alpha = 0.32f + depth * 0.42f // 0.32f to 0.74f for elegant presence

            list.add(
                OceanParticle(
                    baseNormX = normX,
                    baseNormY = normY,
                    diameterDp = size,
                    depthLayer = depth,
                    phaseX = phaseX,
                    phaseY = phaseY,
                    driftSpeed = speed,
                    colorGroup = colorGroup,
                    baseAlpha = alpha
                )
            )
        }
        list
    }

    // Frame-rate-independent continuous timing (120Hz/90Hz/60Hz adaptive)
    var wavePhase by remember { mutableFloatStateOf(0f) }
    var swellPhase by remember { mutableFloatStateOf(0f) }
    var colorPhase by remember { mutableFloatStateOf(0f) }
    val frameTrigger = remember { mutableLongStateOf(0L) }

    LaunchedEffect(reducedMotion) {
        if (reducedMotion) return@LaunchedEffect

        var lastNanos = 0L
        while (isActive) {
            withFrameNanos { nowNanos ->
                if (lastNanos > 0L) {
                    val dt = ((nowNanos - lastNanos) / 1_000_000_000f).coerceIn(0.001f, 0.045f)

                    // Wave cycle (approx 16s period)
                    val waveSpeed = (2f * PI / 16.0f).toFloat()
                    wavePhase = (wavePhase + waveSpeed * dt) % (2f * PI.toFloat())

                    // Swell cycle (approx 22s period)
                    val swellSpeed = (2f * PI / 22.0f).toFloat()
                    swellPhase = (swellPhase + swellSpeed * dt) % (2f * PI.toFloat())

                    // Color evolution (approx 28s period)
                    val colorSpeed = (2f * PI / 28.0f).toFloat()
                    colorPhase = (colorPhase + colorSpeed * dt) % (2f * PI.toFloat())
                }
                lastNanos = nowNanos
                frameTrigger.longValue = nowNanos
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val _t = frameTrigger.longValue

            val canvasW = size.width
            val canvasH = size.height
            if (canvasW <= 0f || canvasH <= 0f) return@Canvas

            val centerX = canvasW * 0.5f
            val centerY = canvasH * 0.45f
            val coreDimRadiusPx = if (dimAroundCenter) canvasW * 0.40f else 0f

            val colorCycleT = (sin(colorPhase) + 1f) * 0.5f // 0f..1f

            for (i in 0 until particles.size) {
                val p = particles[i]

                // Dynamic sine-wave ocean undulation with depth parallax
                val waveOffset = (canvasW * 0.05f * p.depthLayer)
                val waveX = sin(wavePhase * p.driftSpeed + p.phaseX) * waveOffset
                val swellY = cos(swellPhase * 0.85f * p.driftSpeed + p.phaseY) * (waveOffset * 1.4f)
                val oceanWaveCurvature = sin(p.baseNormX * 3.5f + wavePhase) * (14f * p.depthLayer)

                val x = p.baseNormX * canvasW + waveX
                val y = p.baseNormY * canvasH + swellY + oceanWaveCurvature

                // Attenuate particles directly behind the central 3D core if requested
                var particleAlpha = p.baseAlpha
                if (dimAroundCenter) {
                    val distToCenter = sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY))
                    if (distToCenter < coreDimRadiusPx) {
                        val factor = (distToCenter / coreDimRadiusPx).coerceIn(0.10f, 1f)
                        particleAlpha *= factor
                    }
                }

                if (particleAlpha <= 0.04f) continue

                // Soft color shift across metallic, silver, light-blue, white, and subtle accents
                val particleColor = when (p.colorGroup) {
                    0 -> lerpColor(BgColorMetallicGrey, BgColorSilver, colorCycleT)
                    1 -> lerpColor(BgColorSilver, BgColorPearlWhite, colorCycleT)
                    2 -> lerpColor(BgColorSilver, BgColorIceBlue, colorCycleT)
                    3 -> lerpColor(BgColorIceBlue, BgColorPearlWhite, colorCycleT)
                    4 -> lerpColor(BgColorIceBlue, BgColorMint, colorCycleT)
                    else -> lerpColor(BgColorSilver, BgColorLavender, colorCycleT)
                }

                val radiusPx = (p.diameterDp * 0.5f) * density

                // 1. Outer soft glow halo
                drawCircle(
                    color = particleColor.copy(alpha = (particleAlpha * 0.16f).coerceIn(0f, 0.35f)),
                    radius = radiusPx * 2.1f,
                    center = Offset(x, y)
                )

                // 2. Main visible particle body (4-8 dp dot)
                drawCircle(
                    color = particleColor.copy(alpha = particleAlpha),
                    radius = radiusPx,
                    center = Offset(x, y)
                )

                // 3. Subtle bright core sparkle on closer particles
                if (p.depthLayer > 0.65f) {
                    drawCircle(
                        color = Color.White.copy(alpha = (particleAlpha * 0.82f).coerceIn(0f, 1f)),
                        radius = radiusPx * 0.40f,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

/**
 * Linearly interpolates between two colors.
 */
private fun lerpColor(c1: Color, c2: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = c1.red + (c2.red - c1.red) * f,
        green = c1.green + (c2.green - c1.green) * f,
        blue = c1.blue + (c2.blue - c1.blue) * f,
        alpha = c1.alpha + (c2.alpha - c1.alpha) * f
    )
}
