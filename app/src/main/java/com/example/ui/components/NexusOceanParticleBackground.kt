package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.NexusPureBlack
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
    val depthLayer: Float, // 0.3f (far) to 1.0f (near)
    val phaseX: Float,
    val phaseY: Float,
    val driftSpeed: Float,
    val colorGroup: Int,
    val baseAlpha: Float
)

/**
 * Premium Futuristic Pure-Black Ocean-Wave Dot-Matrix Background.
 *
 * Renders a full-screen, continuously animated ocean-like wave surface composed of
 * noticeably visible 4–8dp floating particles with subtle depth, glow, parallax, and
 * organic wave undulation.
 *
 * Particles softly shift between silver grey, metallic grey, very light blue, and soft white.
 * Never consumes gestures or blocks touch interactions.
 */
@Composable
fun NexusOceanParticleBackground(
    modifier: Modifier = Modifier,
    dimAroundCenter: Boolean = true,
    reducedMotion: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "NexusOceanWaveLoop")

    // Continuous primary wave cycle (16 seconds)
    val wavePhase by if (reducedMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2f * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 16000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "wavePhase"
        )
    }

    // Secondary subtle cross-swell phase (22 seconds)
    val swellPhase by if (reducedMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2f * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 22000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "swellPhase"
        )
    }

    // Gentle metallic/light-blue/silver color transition cycle (26 seconds)
    val colorPhase by if (reducedMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0.5f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2f * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 26000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "colorPhase"
        )
    }

    // Generate pre-allocated deterministic particle array with varied 4–8dp diameters
    val particleCount = 104
    val particles = remember {
        val list = ArrayList<OceanParticle>(particleCount)
        val rng = java.util.Random(133742L)

        // Varied sizes from 4.0dp to 8.0dp
        val sizeBuckets = floatArrayOf(4.0f, 4.8f, 5.5f, 6.2f, 7.0f, 8.0f)

        for (i in 0 until particleCount) {
            val gridCol = i % 8
            val gridRow = i / 8
            val jitterX = (rng.nextFloat() - 0.5f) * 0.11f
            val jitterY = (rng.nextFloat() - 0.5f) * 0.07f

            val normX = ((gridCol + 0.5f) / 8f + jitterX).coerceIn(0.02f, 0.98f)
            val normY = ((gridRow + 0.5f) / 13f + jitterY).coerceIn(0.02f, 0.98f)

            val size = sizeBuckets[rng.nextInt(sizeBuckets.size)]
            val depth = 0.35f + (size - 4.0f) / 4.0f * 0.65f // Larger particles feel closer
            val phaseX = rng.nextFloat() * (2f * PI.toFloat())
            val phaseY = rng.nextFloat() * (2f * PI.toFloat())
            val speed = 0.7f + rng.nextFloat() * 0.6f
            val colorGroup = rng.nextInt(4)
            val alpha = 0.35f + depth * 0.40f // 0.35f to 0.75f for refined presence

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height
            if (canvasW <= 0f || canvasH <= 0f) return@Canvas

            val centerX = canvasW * 0.5f
            val centerY = canvasH * 0.45f
            val coreDimRadiusPx = if (dimAroundCenter) canvasW * 0.36f else 0f

            // Palette: Silver grey, metallic grey, very light blue, soft white
            val metallicGrey = Color(0xFF64748B)
            val silverGrey = Color(0xFF94A3B8)
            val lightBlue = Color(0xFFBAE6FD)
            val softWhite = Color(0xFFF8FAFC)

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
                        val factor = (distToCenter / coreDimRadiusPx).coerceIn(0.12f, 1f)
                        particleAlpha *= factor
                    }
                }

                if (particleAlpha <= 0.05f) continue

                // Soft color shift across metallic, silver, light-blue, and white
                val particleColor = when (p.colorGroup) {
                    0 -> lerpColor(metallicGrey, silverGrey, colorCycleT)
                    1 -> lerpColor(silverGrey, softWhite, colorCycleT)
                    2 -> lerpColor(silverGrey, lightBlue, colorCycleT)
                    else -> lerpColor(lightBlue, softWhite, colorCycleT)
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
                        color = Color.White.copy(alpha = (particleAlpha * 0.85f).coerceIn(0f, 1f)),
                        radius = radiusPx * 0.42f,
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
