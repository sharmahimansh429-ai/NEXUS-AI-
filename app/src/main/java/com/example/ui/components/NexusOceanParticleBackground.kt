package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.Random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Data model for a 3D floating particle with distinct depth, size, aesthetic color,
 * and multidirectional procedural trajectory.
 */
private class Floating3DDot(
    var normX: Float,
    var normY: Float,
    val baseDepth: Float, // 0.25f (deep back) to 1.0f (foreground)
    val diameterDp: Float, // 3.2dp to 7.8dp
    val colorGroup: Int, // 0..9 palette group
    val speedFactor: Float, // procedural drift velocity
    val trajectoryAngle: Float, // diagonal / lateral angle (approx 20-50 degrees)
    val seedA: Float,
    val seedB: Float,
    val seedC: Float
)

/**
 * Data model for a subtle frosted-glass wave ribbon.
 */
private class FrostedWaveRibbon(
    val baseNormY: Float,
    val waveDepth: Float,
    val speed: Float,
    val freqScale: Float,
    val amplitudeDp: Float,
    val seed: Float
)

/**
 * Aesthetic Color Palette for Floating 3D Particles:
 * - Pearl silver and platinum grey
 * - Soft icy blue and pastel cyan
 * - Lavender and muted violet
 * - Pale rose and subtle champagne
 * - Occasional pearl-white highlights
 *
 * Formulated with balanced saturation so every particle has rich definition
 * and crisp depth visibility against the static pure white background (#FFFFFF).
 */
private val DotColorPlatinumGrey = Color(0xFF475569)
private val DotColorPearlSilver = Color(0xFF64748B)
private val DotColorSoftIcyBlue = Color(0xFF0284C7)
private val DotColorPastelCyan = Color(0xFF0EA5E9)
private val DotColorLavender = Color(0xFF7C3AED)
private val DotColorMutedViolet = Color(0xFF8B5CF6)
private val DotColorPaleRose = Color(0xFFE11D48)
private val DotColorChampagne = Color(0xFFD97706)
private val DotColorPearlWhite = Color(0xFFF8FAFC)
private val DotColorSoftMint = Color(0xFF0D9488)

/**
 * Pure White Background with Subtle Frosted-Glass Waves & Colorful 3D Floating Dots.
 *
 * Layer 1: Solid, static pure white base (#FFFFFF).
 * Layer 2: Subtle frosted-glass light refraction & fluid organic wave currents (behind the dots).
 * Layer 3: Colorful, elegant 3D floating dots with frosted-glass halos, specular highlights, and ambient depth.
 *
 * Follows multidirectional, diagonal, lateral, curved, organic procedural motion with irrational
 * frequency ratios for an infinite, smooth, non-repeating 3D living environment.
 */
@Composable
fun NexusOceanParticleBackground(
    modifier: Modifier = Modifier,
    dimAroundCenter: Boolean = true,
    reducedMotion: Boolean = false
) {
    // 1. Generate pre-allocated deterministic 3D particle array (~136 particles)
    val particleCount = 136
    val particles = remember {
        val list = ArrayList<Floating3DDot>(particleCount)
        val rng = Random(882244L)

        // Varied sizes from 3.2dp to 7.8dp
        val sizeBuckets = floatArrayOf(3.2f, 3.8f, 4.5f, 5.2f, 6.0f, 6.8f, 7.8f)

        for (i in 0 until particleCount) {
            val gridCol = i % 8
            val gridRow = i / 8
            val jitterX = (rng.nextFloat() - 0.5f) * 0.14f
            val jitterY = (rng.nextFloat() - 0.5f) * 0.10f

            val normX = ((gridCol + 0.5f) / 8f + jitterX).coerceIn(-0.05f, 1.05f)
            val normY = ((gridRow + 0.5f) / 17f + jitterY).coerceIn(-0.05f, 1.05f)

            val size = sizeBuckets[rng.nextInt(sizeBuckets.size)]
            val depth = 0.25f + (size - 3.2f) / (7.8f - 3.2f) * 0.75f // Larger particles are closer
            val speed = 0.55f + rng.nextFloat() * 0.65f

            // Multidirectional diagonal angle: 25 to 55 degrees with gentle variance
            val baseAngle = (25f + rng.nextFloat() * 30f) * (PI.toFloat() / 180f)

            val colorGroup = when {
                i % 14 == 0 -> 8 // Pearl white highlight
                i % 9 == 0 -> 6  // Pale rose
                i % 7 == 0 -> 4  // Lavender
                i % 6 == 0 -> 7  // Champagne
                i % 5 == 0 -> 5  // Muted violet
                i % 4 == 0 -> 2  // Soft icy blue
                i % 3 == 0 -> 3  // Pastel cyan
                i % 2 == 0 -> 0  // Platinum grey
                else -> 1        // Pearl silver
            }

            list.add(
                Floating3DDot(
                    normX = normX,
                    normY = normY,
                    baseDepth = depth,
                    diameterDp = size,
                    colorGroup = colorGroup,
                    speedFactor = speed,
                    trajectoryAngle = baseAngle,
                    seedA = rng.nextFloat() * 100f,
                    seedB = rng.nextFloat() * 100f,
                    seedC = rng.nextFloat() * 100f
                )
            )
        }
        list
    }

    // 2. Generate pre-allocated subtle frosted-glass wave ribbons
    val waveRibbons = remember {
        listOf(
            FrostedWaveRibbon(baseNormY = 0.12f, waveDepth = 0.40f, speed = 0.65f, freqScale = 2.4f, amplitudeDp = 18f, seed = 1.11f),
            FrostedWaveRibbon(baseNormY = 0.32f, waveDepth = 0.75f, speed = 0.90f, freqScale = 2.9f, amplitudeDp = 26f, seed = 2.45f),
            FrostedWaveRibbon(baseNormY = 0.52f, waveDepth = 0.55f, speed = 0.72f, freqScale = 2.2f, amplitudeDp = 22f, seed = 3.78f),
            FrostedWaveRibbon(baseNormY = 0.72f, waveDepth = 0.85f, speed = 1.05f, freqScale = 3.1f, amplitudeDp = 28f, seed = 4.92f),
            FrostedWaveRibbon(baseNormY = 0.88f, waveDepth = 0.45f, speed = 0.60f, freqScale = 2.6f, amplitudeDp = 16f, seed = 5.63f)
        )
    }

    // Lifecycle-aware animation state: pause when app is in background to save battery
    val lifecycleOwner = LocalLifecycleOwner.current
    var isAppResumed by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isAppResumed = (event == Lifecycle.Event.ON_RESUME)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Procedural continuous elapsed time (frame-rate independent 120Hz/90Hz/60Hz)
    var elapsedTimeSec by remember { mutableFloatStateOf(0f) }
    val frameTrigger = remember { mutableLongStateOf(0L) }

    LaunchedEffect(reducedMotion, isAppResumed) {
        if (reducedMotion || !isAppResumed) return@LaunchedEffect

        var lastNanos = 0L
        while (isAppResumed) {
            withFrameNanos { nowNanos ->
                if (lastNanos > 0L) {
                    val dt = ((nowNanos - lastNanos) / 1_000_000_000f).coerceIn(0.001f, 0.035f)
                    elapsedTimeSec += dt
                }
                lastNanos = nowNanos
                frameTrigger.longValue = nowNanos
            }
        }
    }

    // Reusable Path object for wave curves (prevents allocation per frame)
    val wavePath = remember { Path() }

    // PART 1: Solid Pure White Background (#FFFFFF) - static and unchanged
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val _t = frameTrigger.longValue // recomposition trigger
            val time = elapsedTimeSec

            val canvasW = size.width
            val canvasH = size.height
            if (canvasW <= 0f || canvasH <= 0f) return@Canvas

            val centerX = canvasW * 0.5f
            val centerY = canvasH * 0.44f
            val coreDimRadiusPx = if (dimAroundCenter) canvasW * 0.42f else 0f

            // =========================================================================
            // PART 3: Subtle Frosted-Glass & Fluid Waves Behind the Dots
            // =========================================================================
            for (wIndex in waveRibbons.indices) {
                val ribbon = waveRibbons[wIndex]
                wavePath.reset()

                val waveDepth = ribbon.waveDepth
                val speed = ribbon.speed
                val ampPx = ribbon.amplitudeDp * density
                val baseY = ribbon.baseNormY * canvasH

                val steps = 24
                var isFirst = true

                for (s in 0..steps) {
                    val u = s.toFloat() / steps
                    // Diagonal trajectory: diagonal across screen from top-left toward bottom-right
                    val x = u * canvasW * 1.25f - canvasW * 0.12f

                    // Procedural wave equation with irrational harmonics (no repeating patterns)
                    val theta1 = u * ribbon.freqScale * 3.14159f + time * 0.16f * speed + ribbon.seed
                    val theta2 = u * (ribbon.freqScale * 1.618f) - time * 0.091f * speed + ribbon.seed * 0.7f
                    val theta3 = (u * canvasW * 0.003f) + time * 0.043f * speed

                    val waveOffset = sin(theta1) * ampPx + cos(theta2) * (ampPx * 0.45f) + sin(theta3) * (ampPx * 0.25f)
                    val y = baseY + (u - 0.5f) * canvasH * 0.25f + waveOffset

                    if (isFirst) {
                        wavePath.moveTo(x, y)
                        isFirst = false
                    } else {
                        wavePath.lineTo(x, y)
                    }
                }

                // 1. Soft refraction shadow stroke (creates subtle optical depth over white)
                drawPath(
                    path = wavePath,
                    color = Color(0xFF64748B).copy(alpha = (0.045f * waveDepth)),
                    style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
                )

                // 2. Translucent frosted-glass fluid ribbon (pure platinum/silver refraction)
                drawPath(
                    path = wavePath,
                    color = Color(0xFFCBD5E1).copy(alpha = (0.16f * waveDepth)),
                    style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
                )

                // 3. Specular highlight line (delicate frosted-glass refraction highlight)
                drawPath(
                    path = wavePath,
                    color = Color.White.copy(alpha = (0.75f * waveDepth)),
                    style = Stroke(width = 0.9.dp.toPx(), cap = StrokeCap.Round)
                )

                // 4. Subtle caustic micro-condensation nodes along the wave curve
                val causticCount = 5
                for (c in 0 until causticCount) {
                    val causticU = ((c.toFloat() / causticCount) + (time * 0.015f * speed + ribbon.seed) * 0.2f) % 1.0f
                    val causticX = causticU * canvasW * 1.25f - canvasW * 0.12f
                    val theta1 = causticU * ribbon.freqScale * 3.14159f + time * 0.16f * speed + ribbon.seed
                    val waveOffset = sin(theta1) * ampPx
                    val causticY = baseY + (causticU - 0.5f) * canvasH * 0.25f + waveOffset

                    val causticRadius = (1.8.dp.toPx() * (0.8f + 0.4f * sin(time * 0.4f + c)))
                    drawCircle(
                        color = Color(0xFFE2E8F0).copy(alpha = (0.22f * waveDepth)),
                        radius = causticRadius * 2.2f,
                        center = Offset(causticX, causticY)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = (0.60f * waveDepth)),
                        radius = causticRadius,
                        center = Offset(causticX, causticY)
                    )
                }
            }

            // =========================================================================
            // PART 2 & PART 4: Colorful 3D Floating Dots (Multidirectional & Infinite)
            // =========================================================================
            for (i in particles.indices) {
                val p = particles[i]

                // Multidirectional, diagonal, lateral, curved organic procedural motion
                val lateralWander = sin(time * 0.043f + p.seedA) * 0.38f + cos(time * 0.027f + p.seedB) * 0.26f
                val driftAngle = p.trajectoryAngle + lateralWander

                // Time-based curved displacement
                val baseSpeed = 0.014f * p.speedFactor
                val curSpeedX = cos(driftAngle) * baseSpeed
                val curSpeedY = sin(driftAngle) * (baseSpeed * 0.75f)

                // Procedural harmonic waves (organic drift)
                val organicWaveX = sin(p.normY * 4.2f + time * 0.11f + p.seedA) * (9.dp.toPx() * p.baseDepth)
                val organicWaveY = cos(p.normX * 3.6f + time * 0.087f + p.seedB) * (14.dp.toPx() * p.baseDepth)

                // Update continuous coordinates with toroidal wrapping
                val screenNormX = (p.normX + time * curSpeedX) % 1.15f
                val screenNormY = (p.normY + time * curSpeedY) % 1.15f

                val rawX = if (screenNormX < -0.05f) screenNormX + 1.20f else screenNormX
                val rawY = if (screenNormY < -0.05f) screenNormY + 1.20f else screenNormY

                val x = rawX * canvasW + organicWaveX
                val y = rawY * canvasH + organicWaveY

                // 3D Depth oscillation (subtle breathing in Z dimension)
                val dynamicZ = (p.baseDepth + sin(time * 0.071f + p.seedC) * 0.10f).coerceIn(0.20f, 1.0f)

                // Attenuate particles near the central 3D core so it remains completely unobstructed
                var particleAlpha = (0.50f + dynamicZ * 0.45f).coerceIn(0.35f, 0.95f)
                if (dimAroundCenter && coreDimRadiusPx > 0f) {
                    val dx = (x - centerX).toDouble()
                    val dy = (y - centerY).toDouble()
                    val distToCenter = kotlin.math.sqrt(dx * dx + dy * dy).toFloat()
                    if (distToCenter < coreDimRadiusPx) {
                        val factor = (distToCenter / coreDimRadiusPx).coerceIn(0.08f, 1.0f)
                        particleAlpha *= factor
                    }
                }

                if (particleAlpha <= 0.05f) continue

                // Particle Color from Aesthetic Palette
                val dotColor = when (p.colorGroup) {
                    0 -> DotColorPlatinumGrey
                    1 -> DotColorPearlSilver
                    2 -> DotColorSoftIcyBlue
                    3 -> DotColorPastelCyan
                    4 -> DotColorLavender
                    5 -> DotColorMutedViolet
                    6 -> DotColorPaleRose
                    7 -> DotColorChampagne
                    8 -> DotColorPearlWhite
                    else -> DotColorSoftMint
                }

                val radiusPx = (p.diameterDp * 0.5f) * density * (0.75f + dynamicZ * 0.35f)

                // Subtle Shimmer / Twinkle factor
                val shimmer = 0.85f + 0.15f * sin(time * 0.73f * p.speedFactor + p.seedA)

                // 1. Subtle Ambient Occlusion / Micro-Drop Shadow (creates convincing 3D separation over white)
                drawCircle(
                    color = Color(0xFF475569).copy(alpha = (0.13f * dynamicZ * particleAlpha).coerceIn(0.02f, 0.22f)),
                    radius = radiusPx * 1.15f,
                    center = Offset(x + 1.1.dp.toPx(), y + 1.4.dp.toPx())
                )

                // 2. Frosted-Glass Translucent Capsule Halo around particle
                drawCircle(
                    color = dotColor.copy(alpha = (0.14f * dynamicZ * particleAlpha).coerceIn(0.04f, 0.28f)),
                    radius = radiusPx * 2.15f,
                    center = Offset(x, y)
                )

                // 3. Main 3D Spherical Particle Body with Directional Specular Shading
                val specularCenterX = x - radiusPx * 0.30f
                val specularCenterY = y - radiusPx * 0.30f

                val isWhiteDot = p.colorGroup == 8
                val particleBrush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to Color.White.copy(alpha = (0.95f * shimmer * particleAlpha).coerceIn(0.4f, 1.0f)),
                        0.35f to (if (isWhiteDot) Color(0xFFF1F5F9) else dotColor).copy(alpha = (0.85f * particleAlpha).coerceIn(0.4f, 1.0f)),
                        0.85f to dotColor.copy(alpha = (0.92f * particleAlpha).coerceIn(0.5f, 1.0f)),
                        1.0f to (if (isWhiteDot) DotColorPearlSilver else dotColor).copy(alpha = (0.96f * particleAlpha).coerceIn(0.5f, 1.0f))
                    ),
                    center = Offset(specularCenterX, specularCenterY),
                    radius = radiusPx * 1.25f
                )

                drawCircle(
                    brush = particleBrush,
                    radius = radiusPx,
                    center = Offset(x, y)
                )

                // 4. Delicate Specular Pearl-White Highlight Reflection (top-left glint)
                if (dynamicZ > 0.45f) {
                    drawCircle(
                        color = Color.White.copy(alpha = (0.94f * shimmer * particleAlpha).coerceIn(0.4f, 1.0f)),
                        radius = radiusPx * 0.32f,
                        center = Offset(specularCenterX, specularCenterY)
                    )
                }

                // 5. Delicate translucent frosted ring around foreground prominent particles
                if (dynamicZ > 0.75f && p.diameterDp >= 6.0f) {
                    drawCircle(
                        color = Color(0xFF94A3B8).copy(alpha = (0.22f * particleAlpha)),
                        radius = radiusPx * 2.15f,
                        center = Offset(x, y),
                        style = Stroke(width = 0.6.dp.toPx())
                    )
                }
            }
        }
    }
}
