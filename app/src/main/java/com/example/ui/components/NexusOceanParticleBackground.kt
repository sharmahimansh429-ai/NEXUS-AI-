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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.PearlWhite
import com.example.ui.theme.PearlWhitePure
import com.example.ui.theme.PearlWhiteSoft
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Premium Futuristic Pearl-White Ocean-Wave Particle Field.
 *
 * Renders a full-screen, calm futuristic pearl-white ocean surface composed of
 * ultra-fine dot-matrix particles (microscopic points of light) undulating in
 * gentle 3D wave motion.
 *
 * Particles softly shift between muted pastel cyan, ice blue, silver, lavender,
 * and pale pink accents with restrained light refraction and subtle highlights.
 * White is the dominant canvas color.
 */
@Composable
fun NexusOceanParticleBackground(
    modifier: Modifier = Modifier,
    reducedMotion: Boolean = false,
    dimAroundCenter: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PearlOceanWaveTransition")

    // Slow, fluid wave cycle (24 seconds)
    val wavePhase by if (reducedMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2f * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 24000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "wavePhase"
        )
    }

    // Secondary harmonic cross-current (18 seconds)
    val secondaryPhase by if (reducedMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2f * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 18000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "secondaryPhase"
        )
    }

    // Gentle pastel color transition cycle (30 seconds)
    val colorCycle by if (reducedMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0.4f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 30000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "colorCycle"
        )
    }

    // Pre-allocated static grid topology for zero memory allocation inside render loop
    val cols = 56
    val rows = 42
    val totalGridPoints = cols * rows

    val uArray = remember {
        FloatArray(totalGridPoints) { index ->
            val col = index % cols
            col.toFloat() / (cols - 1).toFloat()
        }
    }

    val vArray = remember {
        FloatArray(totalGridPoints) { index ->
            val row = index / cols
            val normalizedRow = row.toFloat() / (rows - 1).toFloat()
            0.05f + 0.95f * normalizedRow
        }
    }

    val jitterArray = remember {
        FloatArray(totalGridPoints) { index ->
            val col = index % cols
            val row = index / cols
            sin(col * 0.42f + row * 0.68f) * 0.22f
        }
    }

    // Upper celestial pearl stardust (110 fine ambient particles)
    val ambientCount = 110
    val ambientX = remember {
        FloatArray(ambientCount) { i ->
            (i * 79.19f) % 1.0f
        }
    }
    val ambientY = remember {
        FloatArray(ambientCount) { i ->
            val seed = (i * 37.43f) % 1.0f
            0.02f + seed * 0.42f
        }
    }
    val ambientPhaseOffset = remember {
        FloatArray(ambientCount) { i ->
            (i * 1.618f) % (2f * PI.toFloat())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PearlWhite)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            if (width <= 0f || height <= 0f) return@Canvas

            // 1. Draw dominant pure white / soft pearl-white background gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PearlWhitePure,
                        PearlWhite,
                        PearlWhiteSoft
                    ),
                    startY = 0f,
                    endY = height
                ),
                size = Size(width, height)
            )

            val centerX = width * 0.5f
            val centerY = height * 0.5f
            val horizonY = height * 0.38f
            val usableDepthY = height - horizonY

            // 2. Draw upper pearl sky stardust
            drawPearlAmbientStardust(
                ambientCount = ambientCount,
                ambientX = ambientX,
                ambientY = ambientY,
                ambientPhaseOffset = ambientPhaseOffset,
                wavePhase = wavePhase,
                width = width,
                height = height,
                dimAroundCenter = dimAroundCenter,
                centerX = centerX,
                centerY = centerY
            )

            // 3. Draw 3D ocean-wave dot-matrix surface with pastel accents
            drawPearlOceanWaveGrid(
                totalPoints = totalGridPoints,
                uArray = uArray,
                vArray = vArray,
                jitterArray = jitterArray,
                wavePhase = wavePhase,
                secondaryPhase = secondaryPhase,
                colorCycle = colorCycle,
                width = width,
                height = height,
                horizonY = horizonY,
                usableDepthY = usableDepthY,
                centerX = centerX,
                centerY = centerY,
                dimAroundCenter = dimAroundCenter
            )
        }
    }
}

/**
 * Draws the pearl-white ocean wave grid using ultra-fine pastel dot-matrix particles
 */
private fun DrawScope.drawPearlOceanWaveGrid(
    totalPoints: Int,
    uArray: FloatArray,
    vArray: FloatArray,
    jitterArray: FloatArray,
    wavePhase: Float,
    secondaryPhase: Float,
    colorCycle: Float,
    width: Float,
    height: Float,
    horizonY: Float,
    usableDepthY: Float,
    centerX: Float,
    centerY: Float,
    dimAroundCenter: Boolean
) {
    val maxWaveAmp = height * 0.038f

    for (i in 0 until totalPoints) {
        val u = uArray[i]
        val v = vArray[i]
        val jitter = jitterArray[i]

        // 3D ocean wave height function with 3 layered harmonics
        val wave1 = sin(u * 13.8f + v * 7.8f + wavePhase + jitter)
        val wave2 = cos(u * 20.5f - v * 10.8f + secondaryPhase)
        val wave3 = sin((u + v) * 6.2f - wavePhase * 0.6f)

        val waveNorm = wave1 * 0.54f + wave2 * 0.30f + wave3 * 0.16f
        val depthAmp = v * (0.35f + 0.65f * v)
        val waveElevation = waveNorm * maxWaveAmp * depthAmp

        // 3D Perspective Projection
        val perspectiveSpread = 0.28f + 1.05f * (v * v)
        val xProj = centerX + (u - 0.5f) * width * 1.42f * perspectiveSpread

        val yBase = horizonY + usableDepthY * (v * v * 0.82f + v * 0.18f)
        val yProj = yBase - waveElevation

        if (xProj < -20f || xProj > width + 20f || yProj < -20f || yProj > height + 20f) {
            continue
        }

        // Horizon & Edge Vignette
        val horizonFade = when {
            v < 0.12f -> (v / 0.12f)
            else -> 1.0f
        }

        val edgeDist = kotlin.math.abs(u - 0.5f) * 2f
        val edgeFade = if (edgeDist > 0.85f) {
            ((1f - edgeDist) / 0.15f).coerceIn(0f, 1f)
        } else 1.0f

        // Center clearance: softly dims particles behind central 3D core
        var centerFade = 1.0f
        if (dimAroundCenter) {
            val dx = (xProj - centerX) / (width * 0.42f)
            val dy = (yProj - centerY) / (height * 0.28f)
            val distSq = dx * dx + dy * dy
            if (distSq < 1.0f) {
                centerFade = 0.30f + 0.70f * sqrt(distSq)
            }
        }

        val combinedFade = horizonFade * edgeFade * centerFade
        if (combinedFade <= 0.02f) continue

        val crestFactor = ((waveNorm + 1f) * 0.5f).coerceIn(0f, 1f)

        // Ultra-tiny particle radius: 0.65px to 1.30px (microscopic points of light)
        val radiusPx = (0.65f + 0.50f * v + 0.25f * crestFactor).coerceIn(0.6f, 1.35f)

        // Soft pastel palette with muted saturation against pearl-white canvas
        val particleColor = computePearlOceanParticleColor(
            crestFactor = crestFactor,
            depth = v,
            u = u,
            colorCycle = colorCycle,
            fade = combinedFade
        )

        drawCircle(
            color = particleColor,
            radius = radiusPx,
            center = Offset(xProj, yProj)
        )
    }
}

/**
 * Computes a muted, premium pastel particle color shifting smoothly between
 * pastel ice blue, silver, lavender, and pale pink with restrained light refraction.
 */
private fun computePearlOceanParticleColor(
    crestFactor: Float,
    depth: Float,
    u: Float,
    colorCycle: Float,
    fade: Float
): Color {
    // Spatial + temporal phase for gentle non-uniform pastel drift
    val phase = (colorCycle + u * 0.35f) % 1.0f

    // Soft, muted, desaturated pastel bases (low saturation, elegant & quiet)
    // 0.0 -> Ice Blue: (126, 166, 200)
    // 0.25 -> Silver Grey: (158, 166, 180)
    // 0.50 -> Soft Lavender: (166, 150, 194)
    // 0.75 -> Pale Pink Mist: (200, 150, 168)
    // 1.0 -> Muted Pastel Cyan: (122, 182, 188)
    val r: Float
    val g: Float
    val b: Float

    when {
        phase < 0.25f -> {
            val t = phase / 0.25f
            r = 126f + (158f - 126f) * t
            g = 166f + (166f - 166f) * t
            b = 200f + (180f - 200f) * t
        }
        phase < 0.50f -> {
            val t = (phase - 0.25f) / 0.25f
            r = 158f + (166f - 158f) * t
            g = 166f + (150f - 166f) * t
            b = 180f + (194f - 180f) * t
        }
        phase < 0.75f -> {
            val t = (phase - 0.50f) / 0.25f
            r = 166f + (200f - 166f) * t
            g = 150f + (150f - 150f) * t
            b = 194f + (168f - 194f) * t
        }
        else -> {
            val t = (phase - 0.75f) / 0.25f
            r = 200f + (122f - 200f) * t
            g = 150f + (182f - 150f) * t
            b = 168f + (188f - 168f) * t
        }
    }

    // Crest light refraction highlight:
    // Wave peaks catch soft white/light pearl shimmer, troughs stay soft pastel/silver
    val highlight = crestFactor * 35f
    val rShimmer = (r + highlight).coerceIn(0f, 255f)
    val gShimmer = (g + highlight).coerceIn(0f, 255f)
    val bShimmer = (b + highlight).coerceIn(0f, 255f)

    // Soft, restrained alpha against the pearl-white background
    // Low opacity prevents loudness or neon-like glare
    val baseAlpha = (0.18f + 0.35f * depth + 0.15f * crestFactor) * fade

    return Color(
        red = rShimmer / 255f,
        green = gShimmer / 255f,
        blue = bShimmer / 255f,
        alpha = baseAlpha.coerceIn(0.04f, 0.65f)
    )
}

/**
 * Draws ultra-fine ambient stardust particles in the upper pearl sky
 */
private fun DrawScope.drawPearlAmbientStardust(
    ambientCount: Int,
    ambientX: FloatArray,
    ambientY: FloatArray,
    ambientPhaseOffset: FloatArray,
    wavePhase: Float,
    width: Float,
    height: Float,
    dimAroundCenter: Boolean,
    centerX: Float,
    centerY: Float
) {
    for (i in 0 until ambientCount) {
        val u = ambientX[i]
        val v = ambientY[i]
        val offset = ambientPhaseOffset[i]

        val twinkle = (sin(wavePhase * 1.4f + offset) + 1f) * 0.5f

        val x = u * width
        val y = v * height

        var alpha = (0.12f + 0.25f * twinkle)

        if (dimAroundCenter) {
            val dx = (x - centerX) / (width * 0.4f)
            val dy = (y - centerY) / (height * 0.25f)
            val dSq = dx * dx + dy * dy
            if (dSq < 1.0f) {
                alpha *= (0.35f + 0.65f * sqrt(dSq))
            }
        }

        if (alpha > 0.04f) {
            drawCircle(
                color = Color(0xFF9EAEC5).copy(alpha = alpha.coerceIn(0.03f, 0.40f)),
                radius = 0.75f,
                center = Offset(x, y)
            )
        }
    }
}
