package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

enum class OrbState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING,
    ERROR
}

/**
 * Aesthetic Color Palette for NEXUS Core & Particles:
 * Dominant: Silver, metallic grey, graphite, pearl white, ice blue 🩵
 * Accents: Cyan, soft lavender, muted violet, mint, subtle rose pink
 */
private val ColorSilver = Color(0xFFCBD5E1)
private val ColorMetallicGrey = Color(0xFF94A3B8)
private val ColorGraphite = Color(0xFF64748B)
private val ColorPearlWhite = Color(0xFFF8FAFC)
private val ColorIceBlue = Color(0xFFBAE6FD)
private val ColorCyan = Color(0xFF38BDF8)
private val ColorSoftLavender = Color(0xFFDDD6FE)
private val ColorMutedViolet = Color(0xFFA78BFA)
private val ColorMint = Color(0xFFA7F3D0)
private val ColorRosePink = Color(0xFFFBCFE8)

/**
 * Pre-calculated point on unit sphere with coordinates, layer, and aesthetic color identity
 */
private class FinePoint(
    val x0: Float,
    val y0: Float,
    val z0: Float,
    val phi: Float,
    val lambda: Float,
    val layer: Int, // 0 = shell, 1 = inner nucleus, 2 = orbital stream
    val colorIndex: Int, // 0..9 palette index
    val sizeClass: Int // 0 = normal (30-50% larger than before), 1 = medium, 2 = prominent accent
)

/**
 * Dynamic particle for continuous non-repeating life cycle.
 * Spawns in 3D space, drifts smoothly, and fades away after its lifetime.
 */
private class DynamicFloatingParticle(
    var x: Float,
    var y: Float,
    var z: Float,
    var vx: Float,
    var vy: Float,
    var vz: Float,
    var age: Float,
    var lifetime: Float,
    var baseRadiusDp: Float,
    var color: Color
)

@Composable
fun NexusOrb(
    modifier: Modifier = Modifier,
    orbState: OrbState = OrbState.IDLE,
    soundLevel: Float = 0f,
    orbSize: Dp = 320.dp,
    isTyping: Boolean = false,
    reducedMotion: Boolean = false,
    onClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val touchPulse = remember { Animatable(0f) }

    // Parallax interactive 3D drag rotation angles
    var dragYaw by remember { mutableFloatStateOf(0f) }
    var dragPitch by remember { mutableFloatStateOf(0f) }

    // Typing responsiveness energy factor (smoothly animates when user types)
    val typingEnergy = remember { Animatable(0f) }
    LaunchedEffect(isTyping) {
        if (isTyping) {
            typingEnergy.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        } else {
            typingEnergy.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
        }
    }

    // Precomputed structured 3D particle cloud (~3,040 points)
    val structuredParticles = remember {
        val list = ArrayList<FinePoint>(3040)
        val goldenRatio = (1f + sqrt(5f)) / 2f
        val goldenAngle = (2f * PI * (1f - 1f / goldenRatio)).toFloat()
        val rng = Random(424242L)

        // Helper to pick aesthetic palette color (Dominant 80%, Accents 20%)
        fun pickColor(i: Int, layer: Int): Int {
            val r = rng.nextFloat()
            return when {
                r < 0.28f -> 0 // Silver
                r < 0.48f -> 1 // Metallic Grey
                r < 0.64f -> 4 // Ice Blue 🩵
                r < 0.78f -> 3 // Pearl White
                r < 0.84f -> 2 // Graphite
                r < 0.88f -> 5 // Cyan
                r < 0.92f -> 6 // Soft Lavender
                r < 0.95f -> 7 // Muted Violet
                r < 0.98f -> 8 // Mint
                else -> 9      // Subtle Rose Pink
            }
        }

        // Layer 0: High-density spherical wave shell (2,400 points)
        val countShell = 2400
        for (i in 0 until countShell) {
            val y0 = 1f - (i.toFloat() / (countShell - 1f)) * 2f
            val rCirc = sqrt((1f - y0 * y0).coerceAtLeast(0f))
            val theta = goldenAngle * i
            val x0 = cos(theta) * rCirc
            val z0 = sin(theta) * rCirc
            val phi = atan2(z0, x0)
            val lambda = kotlin.math.asin(y0.coerceIn(-1f, 1f))
            val colorIdx = pickColor(i, layer = 0)
            val sizeClass = when {
                i % 24 == 0 -> 2 // Prominent stardust node with micro-bloom
                i % 7 == 0 -> 1  // Medium stardust node
                else -> 0        // Fine stardust dot (~30-50% larger than legacy)
            }
            list.add(FinePoint(x0, y0, z0, phi, lambda, layer = 0, colorIndex = colorIdx, sizeClass = sizeClass))
        }

        // Layer 1: Inner nucleus stardust (360 points with pearl white & ice blue glow)
        val countInner = 360
        for (i in 0 until countInner) {
            val y0 = 1f - (i.toFloat() / (countInner - 1f)) * 2f
            val rCirc = sqrt((1f - y0 * y0).coerceAtLeast(0f))
            val theta = goldenAngle * (i + 1400)
            val radInner = 0.40f + 0.14f * sin(i.toFloat() * 0.75f)
            val x0 = cos(theta) * rCirc * radInner
            val z0 = sin(theta) * rCirc * radInner
            val colorIdx = if (i % 3 == 0) 4 else if (i % 5 == 0) 3 else 0 // Heavy ice-blue and pearl-white
            val sizeClass = if (i % 14 == 0) 2 else 0
            list.add(FinePoint(x0, y0 * radInner, z0, theta, y0, layer = 1, colorIndex = colorIdx, sizeClass = sizeClass))
        }

        // Layer 2: Orbital harmonic stream ring (280 points)
        val countRing = 280
        for (i in 0 until countRing) {
            val angle = (2f * PI * i / countRing).toFloat()
            val rSpread = 1.18f + 0.06f * sin(angle * 4f)
            val tilt = (22f * PI / 180f).toFloat()
            val x0 = cos(angle) * rSpread
            val y0 = sin(angle) * sin(tilt) * rSpread
            val z0 = sin(angle) * cos(tilt) * rSpread
            val colorIdx = when {
                i % 12 == 0 -> 4 // Ice blue
                i % 19 == 0 -> 6 // Lavender
                i % 27 == 0 -> 8 // Mint
                else -> 0        // Silver
            }
            val sizeClass = if (i % 16 == 0) 1 else 0
            list.add(FinePoint(x0, y0, z0, angle, tilt, layer = 2, colorIndex = colorIdx, sizeClass = sizeClass))
        }

        list
    }

    // Dynamic living floating particles pool (Keep animation alive, non-repeating life cycles)
    val dynamicParticles = remember {
        val poolSize = 72
        val rng = Random(999111L)
        val palette = listOf(
            ColorSilver, ColorMetallicGrey, ColorGraphite, ColorPearlWhite,
            ColorIceBlue, ColorCyan, ColorSoftLavender, ColorMint, ColorRosePink
        )

        fun spawnParticle(staggerAge: Boolean): DynamicFloatingParticle {
            val u = rng.nextFloat() * 2f * PI.toFloat()
            val v = rng.nextFloat() * PI.toFloat()
            val radius = 0.90f + rng.nextFloat() * 0.65f
            val x = radius * sin(v) * cos(u)
            val y = radius * sin(v) * sin(u)
            val z = radius * cos(v)

            val speed = 0.08f + rng.nextFloat() * 0.16f
            val lifetime = 2.4f + rng.nextFloat() * 2.2f
            val age = if (staggerAge) rng.nextFloat() * lifetime else 0f
            val sizeDp = 0.55f + rng.nextFloat() * 0.95f
            val col = palette[rng.nextInt(palette.size)]

            return DynamicFloatingParticle(
                x = x, y = y, z = z,
                vx = (x / radius) * speed + (rng.nextFloat() - 0.5f) * 0.04f,
                vy = (y / radius) * speed + (rng.nextFloat() - 0.5f) * 0.04f,
                vz = (z / radius) * speed + (rng.nextFloat() - 0.5f) * 0.04f,
                age = age,
                lifetime = lifetime,
                baseRadiusDp = sizeDp,
                color = col
            )
        }

        Array(poolSize) { spawnParticle(staggerAge = true) }
    }

    // Continuous Frame-Rate-Independent 120Hz/90Hz/60Hz Simulation State
    var continuousYaw by remember { mutableFloatStateOf(0f) }
    var surfaceWavePhase by remember { mutableFloatStateOf(0f) }
    var breathingPhase by remember { mutableFloatStateOf(0f) }
    val frameTrigger = remember { mutableLongStateOf(0L) }

    LaunchedEffect(reducedMotion, orbState) {
        if (reducedMotion) return@LaunchedEffect

        var lastNanos = 0L
        while (isActive) {
            withFrameNanos { nowNanos ->
                if (lastNanos > 0L) {
                    val dt = ((nowNanos - lastNanos) / 1_000_000_000f).coerceIn(0.001f, 0.045f)

                    // Rotation speed calibrated per state (seconds for full 360)
                    val yawPeriod = when (orbState) {
                        OrbState.THINKING -> 2.8f
                        OrbState.LISTENING -> 6.2f
                        OrbState.SPEAKING -> 4.4f
                        OrbState.ERROR -> 10.0f
                        OrbState.IDLE -> 8.5f
                    }
                    val yawSpeed = (2f * PI / yawPeriod).toFloat()
                    continuousYaw = (continuousYaw + yawSpeed * dt) % (2f * PI.toFloat())

                    // Surface harmonic wave speed
                    val wavePeriod = if (orbState == OrbState.THINKING) 1.5f else 3.8f
                    val waveSpeed = (2f * PI / wavePeriod).toFloat() * (1f + typingEnergy.value * 0.5f)
                    surfaceWavePhase = (surfaceWavePhase + waveSpeed * dt) % (2f * PI.toFloat())

                    // Natural organic breathing pulse
                    val breathPeriod = if (orbState == OrbState.SPEAKING) 0.8f else 2.2f
                    val breathSpeed = (2f * PI / breathPeriod).toFloat()
                    breathingPhase = (breathingPhase + breathSpeed * dt) % (2f * PI.toFloat())

                    // Update dynamic floating particles
                    val rng = Random()
                    val palette = listOf(
                        ColorSilver, ColorMetallicGrey, ColorPearlWhite,
                        ColorIceBlue, ColorCyan, ColorSoftLavender, ColorMint
                    )
                    for (p in dynamicParticles) {
                        p.age += dt
                        p.x += p.vx * dt
                        p.y += p.vy * dt
                        p.z += p.vz * dt

                        if (p.age >= p.lifetime) {
                            // Respawn at a fresh 3D location around the core
                            val u = rng.nextFloat() * 2f * PI.toFloat()
                            val v = rng.nextFloat() * PI.toFloat()
                            val rad = 0.95f + rng.nextFloat() * 0.55f
                            p.x = rad * sin(v) * cos(u)
                            p.y = rad * sin(v) * sin(u)
                            p.z = rad * cos(v)

                            val speed = 0.07f + rng.nextFloat() * 0.15f
                            p.vx = (p.x / rad) * speed + (rng.nextFloat() - 0.5f) * 0.03f
                            p.vy = (p.y / rad) * speed + (rng.nextFloat() - 0.5f) * 0.03f
                            p.vz = (p.z / rad) * speed + (rng.nextFloat() - 0.5f) * 0.03f
                            p.age = 0f
                            p.lifetime = 2.2f + rng.nextFloat() * 2.4f
                            p.color = palette[rng.nextInt(palette.size)]
                        }
                    }
                }
                lastNanos = nowNanos
                frameTrigger.longValue = nowNanos
            }
        }
    }

    Box(
        modifier = modifier
            .size(orbSize)
            .pointerInput(Unit) {
                detectTapGestures {
                    coroutineScope.launch {
                        touchPulse.snapTo(0f)
                        touchPulse.animateTo(1f, tween(550, easing = FastOutSlowInEasing))
                        touchPulse.snapTo(0f)
                    }
                    onClick()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // Gentle spring return towards upright viewing
                        coroutineScope.launch {
                            dragPitch *= 0.6f
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    dragYaw += dragAmount.x * 0.006f
                    dragPitch = (dragPitch - dragAmount.y * 0.006f).coerceIn(-1.0f, 1.0f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val _t = frameTrigger.longValue

            val center = Offset(size.width / 2f, size.height / 2f)
            // Enhanced visual proportion (25-35% larger presence)
            val baseRadius = (size.minDimension / 2f) * 0.46f

            // Total 3D viewing angles with interactive parallax
            val totalYaw = continuousYaw + dragYaw
            val totalPitch = (0.20f * sin(continuousYaw * 0.5f) + dragPitch).toFloat()

            val breathing = 0.96f + 0.07f * sin(breathingPhase)

            // 1. Volumetric Aura with soft pearl-white & ice-blue refraction
            drawVolumetricAura(center, baseRadius, orbState, typingEnergy.value)

            // 2. Faint Metallic Hairline Orbits & Concentric Guides
            drawMetallicReticles(center, baseRadius, totalYaw, orbState)

            // 3. Audio & Tap Harmonic Wave Ripples
            if (orbState == OrbState.SPEAKING || orbState == OrbState.LISTENING || touchPulse.value > 0f) {
                drawWaveHarmonics(center, baseRadius, surfaceWavePhase, soundLevel, touchPulse.value, orbState)
            }

            // 4. Render 3D Structured Fibonacci Stardust Cloud (30-50% larger dots, 10 aesthetic colors)
            draw3DFineParticleCloud(
                particles = structuredParticles,
                center = center,
                baseRadius = baseRadius,
                yaw = totalYaw,
                pitch = totalPitch,
                wavePhase = surfaceWavePhase,
                breathing = breathing,
                soundLevel = soundLevel,
                touchPulse = touchPulse.value,
                typingEnergy = typingEnergy.value,
                orbState = orbState
            )

            // 5. Render Living Dynamic Floating Particles (continuous non-repeating life cycles)
            drawDynamicFloatingParticles(
                particles = dynamicParticles,
                center = center,
                baseRadius = baseRadius,
                yaw = totalYaw,
                pitch = totalPitch
            )
        }
    }
}

// 1. Soft Volumetric Backdrop with Pearl-White & Ice-Blue Specular Aura
private fun DrawScope.drawVolumetricAura(
    center: Offset,
    radius: Float,
    state: OrbState,
    typingFactor: Float
) {
    val auraColor = when (state) {
        OrbState.ERROR -> NeonRed
        OrbState.LISTENING -> NeonEmerald
        OrbState.THINKING -> ColorMutedViolet
        OrbState.SPEAKING -> ColorCyan
        OrbState.IDLE -> if (typingFactor > 0.1f) ColorIceBlue else ColorGraphite
    }

    val glowRadius = radius * 1.85f
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0.0f to ColorPearlWhite.copy(alpha = 0.16f + typingFactor * 0.10f),
                0.22f to auraColor.copy(alpha = 0.12f),
                0.55f to auraColor.copy(alpha = 0.035f),
                0.80f to Color(0xFF0F172A).copy(alpha = 0.015f),
                1.0f to Color.Transparent
            ),
            center = center,
            radius = glowRadius
        ),
        radius = glowRadius,
        center = center
    )
}

// 2. Subtle Metallic Titanium Hairline Orbits & Specular Reticles
private fun DrawScope.drawMetallicReticles(
    center: Offset,
    radius: Float,
    yaw: Float,
    state: OrbState
) {
    val ringColor = when (state) {
        OrbState.ERROR -> NeonRed.copy(alpha = 0.22f)
        OrbState.LISTENING -> NeonEmerald.copy(alpha = 0.22f)
        OrbState.THINKING -> ColorMutedViolet.copy(alpha = 0.30f)
        else -> ColorSilver.copy(alpha = 0.26f)
    }

    // Outer faint metallic hairline orbit
    drawCircle(
        color = ringColor,
        radius = radius * 1.34f,
        center = center,
        style = Stroke(
            width = 0.9.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 16f), yaw * 14f)
        )
    )

    // Inner fine concentric guide with soft metallic sheen
    drawCircle(
        color = ColorGraphite.copy(alpha = 0.35f),
        radius = radius * 1.15f,
        center = center,
        style = Stroke(width = 0.65.dp.toPx())
    )
}

// 3. Audio & Tap Harmonic Wave Ripples
private fun DrawScope.drawWaveHarmonics(
    center: Offset,
    radius: Float,
    phase: Float,
    soundLevel: Float,
    tapProgress: Float,
    state: OrbState
) {
    val waveColor = when (state) {
        OrbState.LISTENING -> NeonEmerald
        OrbState.SPEAKING -> ColorIceBlue
        OrbState.ERROR -> NeonRed
        else -> ColorSilver
    }

    if (state == OrbState.SPEAKING || state == OrbState.LISTENING) {
        val count = 3
        for (i in 0 until count) {
            val fraction = ((phase / (2 * PI).toFloat()) + (i.toFloat() / count)) % 1f
            val rippleRadius = radius * (1.02f + fraction * 0.95f)
            val alpha = (1f - fraction) * (0.22f + soundLevel.coerceIn(0f, 1f) * 0.38f)
            drawCircle(
                color = waveColor.copy(alpha = alpha.coerceIn(0f, 0.65f)),
                radius = rippleRadius,
                center = center,
                style = Stroke(width = 1.0.dp.toPx() * (1f - fraction * 0.45f))
            )
        }
    }

    if (tapProgress > 0f) {
        val tapRadius = radius * (1.0f + tapProgress * 1.40f)
        val tapAlpha = (1f - tapProgress).coerceIn(0f, 1f) * 0.60f
        drawCircle(
            color = ColorPearlWhite.copy(alpha = tapAlpha),
            radius = tapRadius,
            center = center,
            style = Stroke(width = 1.3.dp.toPx() * (1f - tapProgress * 0.6f))
        )
    }
}

// 4. Primary 3D Structured Fibonacci Stardust Cloud
private fun DrawScope.draw3DFineParticleCloud(
    particles: List<FinePoint>,
    center: Offset,
    baseRadius: Float,
    yaw: Float,
    pitch: Float,
    wavePhase: Float,
    breathing: Float,
    soundLevel: Float,
    touchPulse: Float,
    typingEnergy: Float,
    orbState: OrbState
) {
    val cosYaw = cos(yaw)
    val sinYaw = sin(yaw)
    val cosPitch = cos(pitch)
    val sinPitch = sin(pitch)

    val camDist = baseRadius * 3.4f

    // Wave amplitude based on state and typing energy
    val waveAmp = when (orbState) {
        OrbState.THINKING -> 0.16f
        OrbState.LISTENING -> 0.10f + soundLevel.coerceIn(0f, 1f) * 0.18f
        OrbState.SPEAKING -> 0.12f + soundLevel.coerceIn(0f, 1f) * 0.22f
        OrbState.ERROR -> 0.15f
        OrbState.IDLE -> 0.07f + typingEnergy * 0.05f
    }

    val stateRadialScale = when (orbState) {
        OrbState.LISTENING -> 0.94f
        OrbState.SPEAKING -> 1.06f
        OrbState.THINKING -> 1.02f
        else -> 1.0f
    }

    val effectiveBaseRadius = baseRadius * breathing * stateRadialScale

    // Specular lighting vector for metallic sheen
    val lx = 0.35f
    val ly = 0.45f
    val lz = 0.82f

    // 30–50% increased dot sizes with natural mix of small, medium, and occasional larger nodes
    val shellBaseRadiusPx = 0.34.dp.toPx()
    val shellVarRadiusPx = 0.62.dp.toPx()

    val nucleusBaseRadiusPx = 0.42.dp.toPx()
    val nucleusVarRadiusPx = 0.55.dp.toPx()

    val ringBaseRadiusPx = 0.28.dp.toPx()
    val ringVarRadiusPx = 0.44.dp.toPx()

    val prominentRadiusPx = 1.35.dp.toPx()

    val palette = arrayOf(
        ColorSilver, ColorMetallicGrey, ColorGraphite, ColorPearlWhite, ColorIceBlue,
        ColorCyan, ColorSoftLavender, ColorMutedViolet, ColorMint, ColorRosePink
    )

    for (p in particles) {
        // Spherical harmonics wave surface deformations
        val w1 = sin(3.2f * p.phi + wavePhase * 1.3f) * cos(2.6f * p.lambda)
        val w2 = sin(5.4f * p.lambda - wavePhase * 1.9f) * cos(3.2f * p.phi)
        val w3 = cos(4.0f * (p.x0 + p.y0 + p.z0) + wavePhase * 0.9f)
        val surfaceDisplacement = (0.50f * w1 + 0.30f * w2 + 0.20f * w3) * waveAmp

        val touchDisplacement = if (touchPulse > 0f && p.layer == 0) {
            0.22f * sin(touchPulse * PI.toFloat())
        } else 0f

        val r = when (p.layer) {
            0 -> effectiveBaseRadius * (1f + surfaceDisplacement + touchDisplacement)
            1 -> effectiveBaseRadius * 0.50f * (1f + surfaceDisplacement * 0.35f)
            else -> effectiveBaseRadius * (1.18f + 0.04f * sin(p.phi * 4f + wavePhase))
        }

        // 3D Cartesian coordinates
        val x3d = p.x0 * r
        val y3d = p.y0 * r
        val z3d = p.z0 * r

        // 3D Rotation (Yaw + Pitch)
        val xRot = x3d * cosYaw + z3d * sinYaw
        val zTemp = -x3d * sinYaw + z3d * cosYaw
        val yRot = y3d * cosPitch - zTemp * sinPitch
        val zRot = y3d * sinPitch + zTemp * cosPitch

        // Perspective scale & screen projection
        val perspective = camDist / (camDist - zRot)
        val screenX = center.x + xRot * perspective
        val screenY = center.y + yRot * perspective

        // Depth factor [0 = deep back, 1 = front closest]
        val depthNorm = ((zRot + baseRadius) / (2f * baseRadius)).coerceIn(0f, 1f)

        // Particle radius based on sizeClass and depth
        val dotRadius = when (p.sizeClass) {
            2 -> prominentRadiusPx * (0.8f + depthNorm * 0.45f) * perspective
            1 -> when (p.layer) {
                1 -> (nucleusBaseRadiusPx * 1.35f + depthNorm * nucleusVarRadiusPx) * perspective
                2 -> (ringBaseRadiusPx * 1.30f + depthNorm * ringVarRadiusPx) * perspective
                else -> (shellBaseRadiusPx * 1.35f + depthNorm * shellVarRadiusPx) * perspective
            }
            else -> when (p.layer) {
                1 -> (nucleusBaseRadiusPx + depthNorm * nucleusVarRadiusPx) * perspective
                2 -> (ringBaseRadiusPx + depthNorm * ringVarRadiusPx) * perspective
                else -> (shellBaseRadiusPx + depthNorm * shellVarRadiusPx) * perspective
            }
        }

        // Metallic reflection calculation
        val nx = p.x0 * cosYaw + p.z0 * sinYaw
        val nz = -p.x0 * sinYaw + p.z0 * cosYaw
        val nyRot = p.y0 * cosPitch - nz * sinPitch
        val nzRot = p.y0 * sinPitch + nz * cosPitch
        val dotProduct = (nx * lx + nyRot * ly + nzRot * lz).coerceIn(0f, 1f)
        val specular = dotProduct.pow(3) * 0.38f

        val baseAlpha = when (p.layer) {
            1 -> 0.32f + depthNorm * 0.58f + specular
            2 -> 0.20f + depthNorm * 0.62f
            else -> 0.16f + depthNorm * 0.74f + specular
        }.coerceIn(0.08f, 0.98f)

        // Color selection from palette with state overrides
        val dotColor = when {
            orbState == OrbState.ERROR -> {
                if (p.colorIndex == 5 || p.colorIndex == 7) NeonRed else ColorSilver
            }
            orbState == OrbState.LISTENING -> {
                if (p.colorIndex == 4 || p.colorIndex == 5) NeonEmerald else if (depthNorm > 0.75f) ColorPearlWhite else ColorSilver
            }
            orbState == OrbState.THINKING -> {
                if (p.colorIndex == 7 || p.colorIndex == 6) ColorMutedViolet else if (p.colorIndex == 4) ColorCyan else ColorSilver
            }
            else -> {
                if (specular > 0.24f) ColorPearlWhite else palette[p.colorIndex]
            }
        }

        // Draw particle body
        drawCircle(
            color = dotColor.copy(alpha = baseAlpha),
            radius = dotRadius,
            center = Offset(screenX, screenY)
        )

        // Micro-bloom halo for prominent foreground nodes (sizeClass == 2 or closest foreground)
        if (p.sizeClass == 2 || (depthNorm > 0.85f && p.layer == 0)) {
            val haloAlpha = (0.16f * depthNorm).coerceIn(0f, 0.28f)
            drawCircle(
                color = dotColor.copy(alpha = haloAlpha),
                radius = dotRadius * 2.0f,
                center = Offset(screenX, screenY)
            )
        }
    }
}

// 5. Living Dynamic Floating Particles (Continuous Non-Repeating Life Cycles)
private fun DrawScope.drawDynamicFloatingParticles(
    particles: Array<DynamicFloatingParticle>,
    center: Offset,
    baseRadius: Float,
    yaw: Float,
    pitch: Float
) {
    val cosYaw = cos(yaw)
    val sinYaw = sin(yaw)
    val cosPitch = cos(pitch)
    val sinPitch = sin(pitch)
    val camDist = baseRadius * 3.4f

    for (p in particles) {
        val fraction = (p.age / p.lifetime).coerceIn(0f, 1f)
        // Smooth sine fade-in / fade-out: peaks at mid-life
        val lifeAlpha = sin(fraction * PI.toFloat())
        if (lifeAlpha <= 0.02f) continue

        val x3d = p.x * baseRadius
        val y3d = p.y * baseRadius
        val z3d = p.z * baseRadius

        val xRot = x3d * cosYaw + z3d * sinYaw
        val zTemp = -x3d * sinYaw + z3d * cosYaw
        val yRot = y3d * cosPitch - zTemp * sinPitch
        val zRot = y3d * sinPitch + zTemp * cosPitch

        val perspective = camDist / (camDist - zRot).coerceAtLeast(10f)
        val screenX = center.x + xRot * perspective
        val screenY = center.y + yRot * perspective

        val depthNorm = ((zRot + baseRadius) / (2f * baseRadius)).coerceIn(0.1f, 1f)
        val radiusPx = (p.baseRadiusDp * density) * (0.8f + depthNorm * 0.4f) * perspective

        // Draw dynamic floating stardust dot
        drawCircle(
            color = p.color.copy(alpha = (lifeAlpha * (0.35f + depthNorm * 0.55f)).coerceIn(0f, 0.95f)),
            radius = radiusPx,
            center = Offset(screenX, screenY)
        )

        // Soft outer glow for dynamic particles
        drawCircle(
            color = p.color.copy(alpha = (lifeAlpha * 0.12f).coerceIn(0f, 0.20f)),
            radius = radiusPx * 2.2f,
            center = Offset(screenX, screenY)
        )
    }
}
