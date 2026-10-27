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
 * Data model for a microscopic 3D floating node in the complex digital environment.
 * Fine, elegant, high-precision coordinates with procedural drift.
 */
private class DigitalNode(
    var normX: Float,
    var normY: Float,
    val baseDepth: Float, // 0.20f (deep background) to 1.0f (foreground)
    val diameterDp: Float, // 1.2dp to 3.4dp (microscopic precision)
    val paletteIndex: Int,
    val speedFactor: Float,
    val driftAngle: Float,
    val seedA: Float,
    val seedB: Float,
    val seedC: Float
)

/**
 * Data model for subtle, fluid refractive wave currents flowing behind the particles.
 */
private class OpticalWaveRibbon(
    val baseNormY: Float,
    val waveDepth: Float,
    val speed: Float,
    val freqScale: Float,
    val amplitudeDp: Float,
    val seed: Float
)

/**
 * Sophisticated, mature digital matrix palette:
 * - Pearl silver and platinum grey (dominant base)
 * - Muted lavender & pale steel cyan (desaturated, futuristic)
 * - Subtle champagne accents (understated warmth)
 * - Deep obsidian core & pure specular white
 */
private val NodePlatinumGrey = Color(0xFF4A5568)
private val NodePearlSilver = Color(0xFF7E8C9F)
private val NodeSteelIce = Color(0xFF588B9E)
private val NodePaleCyan = Color(0xFF6A9BB0)
private val NodeMutedLavender = Color(0xFF7E7B9A)
private val NodeSoftViolet = Color(0xFF8F8CAE)
private val NodeChampagne = Color(0xFF9C9078)
private val NodeDeepObsidian = Color(0xFF1E293B)
private val NodePureSpecular = Color(0xFFFFFFFF)

/**
 * NEXUS Mature Digital Environment Background.
 *
 * Visual Direction:
 * - Base: Solid Pure White (#FFFFFF) - static, unchanged at all times.
 * - Fluid Optical Waves: Silky, ultra-fine multidirectional refractive interference ribbons (behind particles).
 * - Digital Nodes: Tiny, sophisticated 3D particles with restrained frosted-glass highlights,
 *   subtle subpixel shadows, and rare micro-neural vectors. Feels like an engineered, intelligent
 *   quantum computing environment, not glitter or confetti.
 */
@Composable
fun NexusOceanParticleBackground(
    modifier: Modifier = Modifier,
    dimAroundCenter: Boolean = true,
    reducedMotion: Boolean = false
) {
    // 1. Generate pre-allocated deterministic 3D particle array (~160 fine nodes)
    val nodeCount = 160
    val nodes = remember {
        val list = ArrayList<DigitalNode>(nodeCount)
        val rng = Random(993311L)

        // Microscopic sizes: 1.2dp to 3.4dp for precision nanotech aesthetic
        val sizeBuckets = floatArrayOf(1.2f, 1.5f, 1.9f, 2.3f, 2.8f, 3.4f)

        for (i in 0 until nodeCount) {
            val gridCol = i % 10
            val gridRow = i / 10
            val jitterX = (rng.nextFloat() - 0.5f) * 0.12f
            val jitterY = (rng.nextFloat() - 0.5f) * 0.08f

            val normX = ((gridCol + 0.5f) / 10f + jitterX).coerceIn(-0.05f, 1.05f)
            val normY = ((gridRow + 0.5f) / 16f + jitterY).coerceIn(-0.05f, 1.05f)

            val size = sizeBuckets[rng.nextInt(sizeBuckets.size)]
            val depth = 0.20f + (size - 1.2f) / (3.4f - 1.2f) * 0.80f
            val speed = 0.45f + rng.nextFloat() * 0.55f

            // Multidirectional subtle diagonal flow: 20 to 50 degrees
            val baseAngle = (20f + rng.nextFloat() * 30f) * (PI.toFloat() / 180f)

            val paletteIndex = when {
                i % 12 == 0 -> 8 // Pure specular white highlight
                i % 9 == 0 -> 6  // Subtle champagne
                i % 7 == 0 -> 4  // Muted lavender
                i % 6 == 0 -> 7  // Deep obsidian node
                i % 5 == 0 -> 5  // Soft violet
                i % 4 == 0 -> 2  // Steel ice
                i % 3 == 0 -> 3  // Pale cyan
                i % 2 == 0 -> 0  // Platinum grey
                else -> 1        // Pearl silver
            }

            list.add(
                DigitalNode(
                    normX = normX,
                    normY = normY,
                    baseDepth = depth,
                    diameterDp = size,
                    paletteIndex = paletteIndex,
                    speedFactor = speed,
                    driftAngle = baseAngle,
                    seedA = rng.nextFloat() * 100f,
                    seedB = rng.nextFloat() * 100f,
                    seedC = rng.nextFloat() * 100f
                )
            )
        }
        list
    }

    // 2. Optical refractive wave ribbons (clean fluid interference curves)
    val waveRibbons = remember {
        listOf(
            OpticalWaveRibbon(baseNormY = 0.15f, waveDepth = 0.35f, speed = 0.55f, freqScale = 2.1f, amplitudeDp = 14f, seed = 1.23f),
            OpticalWaveRibbon(baseNormY = 0.35f, waveDepth = 0.65f, speed = 0.75f, freqScale = 2.7f, amplitudeDp = 20f, seed = 2.87f),
            OpticalWaveRibbon(baseNormY = 0.55f, waveDepth = 0.45f, speed = 0.62f, freqScale = 1.9f, amplitudeDp = 16f, seed = 3.91f),
            OpticalWaveRibbon(baseNormY = 0.75f, waveDepth = 0.70f, speed = 0.82f, freqScale = 2.8f, amplitudeDp = 22f, seed = 4.45f),
            OpticalWaveRibbon(baseNormY = 0.90f, waveDepth = 0.40f, speed = 0.50f, freqScale = 2.3f, amplitudeDp = 12f, seed = 5.62f)
        )
    }

    // Lifecycle-aware: pause animation in background to save battery
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

    // Smooth continuous elapsed time (120Hz/90Hz/60Hz adaptive)
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

    val wavePath = remember { Path() }

    // Reusable array for node screen coordinates to compute subtle proximity lines efficiently
    val nodePositionsX = remember { FloatArray(nodeCount) }
    val nodePositionsY = remember { FloatArray(nodeCount) }
    val nodeAlpha = remember { FloatArray(nodeCount) }

    // Layer 1: Solid Pure White Background (#FFFFFF) - static and unchanged
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
            val coreDimRadiusPx = if (dimAroundCenter) canvasW * 0.40f else 0f

            // =========================================================================
            // Layer 2: Subtle Optical Refractive Waves (Clean Fluid Interference)
            // =========================================================================
            for (wIndex in waveRibbons.indices) {
                val ribbon = waveRibbons[wIndex]
                wavePath.reset()

                val waveDepth = ribbon.waveDepth
                val speed = ribbon.speed
                val ampPx = ribbon.amplitudeDp * density
                val baseY = ribbon.baseNormY * canvasH

                val steps = 28
                var isFirst = true

                for (s in 0..steps) {
                    val u = s.toFloat() / steps
                    val x = u * canvasW * 1.20f - canvasW * 0.10f

                    val theta1 = u * ribbon.freqScale * 3.14159f + time * 0.12f * speed + ribbon.seed
                    val theta2 = u * (ribbon.freqScale * 1.618f) - time * 0.075f * speed + ribbon.seed * 0.8f
                    val theta3 = (u * canvasW * 0.002f) + time * 0.032f * speed

                    val waveOffset = sin(theta1) * ampPx + cos(theta2) * (ampPx * 0.35f) + sin(theta3) * (ampPx * 0.20f)
                    val y = baseY + (u - 0.5f) * canvasH * 0.18f + waveOffset

                    if (isFirst) {
                        wavePath.moveTo(x, y)
                        isFirst = false
                    } else {
                        wavePath.lineTo(x, y)
                    }
                }

                // 1. Ultra-faint slate refraction hairline
                drawPath(
                    path = wavePath,
                    color = Color(0xFF64748B).copy(alpha = (0.025f * waveDepth)),
                    style = Stroke(width = 0.8.dp.toPx(), cap = StrokeCap.Round)
                )

                // 2. Faint titanium highlight line (delicate, restrained glass refraction)
                drawPath(
                    path = wavePath,
                    color = Color(0xFF94A3B8).copy(alpha = (0.045f * waveDepth)),
                    style = Stroke(width = 0.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // =========================================================================
            // Layer 3: Calculate Positions of Microscopic 3D Digital Nodes
            // =========================================================================
            for (i in 0 until nodeCount) {
                val node = nodes[i]

                val lateralWander = sin(time * 0.035f + node.seedA) * 0.25f + cos(time * 0.021f + node.seedB) * 0.18f
                val driftAngle = node.driftAngle + lateralWander

                val baseSpeed = 0.010f * node.speedFactor
                val curSpeedX = cos(driftAngle) * baseSpeed
                val curSpeedY = sin(driftAngle) * (baseSpeed * 0.70f)

                val organicWaveX = sin(node.normY * 3.8f + time * 0.08f + node.seedA) * (6.dp.toPx() * node.baseDepth)
                val organicWaveY = cos(node.normX * 3.2f + time * 0.065f + node.seedB) * (10.dp.toPx() * node.baseDepth)

                val screenNormX = (node.normX + time * curSpeedX) % 1.12f
                val screenNormY = (node.normY + time * curSpeedY) % 1.12f

                val rawX = if (screenNormX < -0.04f) screenNormX + 1.16f else screenNormX
                val rawY = if (screenNormY < -0.04f) screenNormY + 1.16f else screenNormY

                val x = rawX * canvasW + organicWaveX
                val y = rawY * canvasH + organicWaveY

                val dynamicZ = (node.baseDepth + sin(time * 0.055f + node.seedC) * 0.08f).coerceIn(0.18f, 1.0f)

                // Smooth attenuation near central core
                var alpha = (0.40f + dynamicZ * 0.50f).coerceIn(0.20f, 0.90f)
                if (dimAroundCenter && coreDimRadiusPx > 0f) {
                    val dx = (x - centerX).toDouble()
                    val dy = (y - centerY).toDouble()
                    val dist = sqrt(dx * dx + dy * dy).toFloat()
                    if (dist < coreDimRadiusPx) {
                        alpha *= (dist / coreDimRadiusPx).coerceIn(0.05f, 1.0f)
                    }
                }

                nodePositionsX[i] = x
                nodePositionsY[i] = y
                nodeAlpha[i] = alpha
            }

            // =========================================================================
            // Layer 4: Micro-Vector Neural Connections (Intelligent Complex Environment)
            // Ultra-faint connections between proximate nodes to create structured depth
            // =========================================================================
            val maxConnectDistPx = 42.dp.toPx()
            for (i in 0 until nodeCount step 2) {
                val a1 = nodeAlpha[i]
                if (a1 < 0.25f) continue
                val x1 = nodePositionsX[i]
                val y1 = nodePositionsY[i]

                // Check a small local neighborhood to keep performance at 120Hz
                val checkLimit = minOf(nodeCount, i + 8)
                for (j in (i + 1) until checkLimit) {
                    val a2 = nodeAlpha[j]
                    if (a2 < 0.25f) continue
                    val x2 = nodePositionsX[j]
                    val y2 = nodePositionsY[j]

                    val dx = (x2 - x1).toDouble()
                    val dy = (y2 - y1).toDouble()
                    val distSq = (dx * dx + dy * dy).toFloat()

                    if (distSq < maxConnectDistPx * maxConnectDistPx) {
                        val dist = sqrt(distSq.toDouble()).toFloat()
                        val connectionFactor = (1f - dist / maxConnectDistPx)
                        val lineAlpha = (0.045f * connectionFactor * minOf(a1, a2)).coerceIn(0.005f, 0.05f)

                        drawLine(
                            color = Color(0xFF64748B).copy(alpha = lineAlpha),
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 0.4.dp.toPx()
                        )
                    }
                }
            }

            // =========================================================================
            // Layer 5: Render Microscopic 3D Digital Nodes
            // =========================================================================
            for (i in 0 until nodeCount) {
                val alpha = nodeAlpha[i]
                if (alpha <= 0.04f) continue

                val node = nodes[i]
                val x = nodePositionsX[i]
                val y = nodePositionsY[i]

                val nodeColor = when (node.paletteIndex) {
                    0 -> NodePlatinumGrey
                    1 -> NodePearlSilver
                    2 -> NodeSteelIce
                    3 -> NodePaleCyan
                    4 -> NodeMutedLavender
                    5 -> NodeSoftViolet
                    6 -> NodeChampagne
                    7 -> NodeDeepObsidian
                    else -> NodePureSpecular
                }

                val radiusPx = (node.diameterDp * 0.5f) * density * (0.80f + node.baseDepth * 0.30f)
                val shimmer = 0.88f + 0.12f * sin(time * 0.65f * node.speedFactor + node.seedA)

                // 1. Micro subpixel ambient depth (whisper-soft 3D float over white)
                if (node.baseDepth > 0.45f) {
                    drawCircle(
                        color = Color(0xFF334155).copy(alpha = (0.05f * alpha)),
                        radius = radiusPx * 1.1f,
                        center = Offset(x + 0.4.dp.toPx(), y + 0.5.dp.toPx())
                    )
                }

                // 2. Restrained optical frosted highlight halo (compact, not bulky)
                drawCircle(
                    color = nodeColor.copy(alpha = (0.08f * alpha * shimmer)),
                    radius = radiusPx * 1.4f,
                    center = Offset(x, y)
                )

                // 3. Precision Spherical Specular Core
                val specOffsetX = x - radiusPx * 0.25f
                val specOffsetY = y - radiusPx * 0.25f

                val isWhite = node.paletteIndex == 8
                val gradientBrush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to Color.White.copy(alpha = (0.95f * shimmer * alpha).coerceIn(0.3f, 1.0f)),
                        0.45f to (if (isWhite) Color(0xFFF1F5F9) else nodeColor).copy(alpha = (0.85f * alpha)),
                        1.0f to (if (isWhite) NodePearlSilver else nodeColor).copy(alpha = (0.92f * alpha))
                    ),
                    center = Offset(specOffsetX, specOffsetY),
                    radius = radiusPx * 1.15f
                )

                drawCircle(
                    brush = gradientBrush,
                    radius = radiusPx,
                    center = Offset(x, y)
                )

                // 4. Pinpoint specular glint on key foreground nodes
                if (node.baseDepth > 0.70f && radiusPx >= 1.2.dp.toPx()) {
                    drawCircle(
                        color = Color.White.copy(alpha = (0.90f * shimmer * alpha)),
                        radius = radiusPx * 0.35f,
                        center = Offset(specOffsetX, specOffsetY)
                    )
                }
            }
        }
    }
}
