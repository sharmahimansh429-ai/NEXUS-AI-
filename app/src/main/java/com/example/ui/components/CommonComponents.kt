package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassHighlight
import com.example.ui.theme.GlassMilkyCard
import com.example.ui.theme.GlassMilkyWhite
import com.example.ui.theme.GlassShadow
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium

fun Modifier.cyberBorder(
    color: Color = NexusBorderHairline,
    width: Dp = 0.5.dp,
    shape: Shape = RoundedCornerShape(12.dp)
) = this
    .border(width = width, color = color, shape = shape)
    .clip(shape)

@Composable
fun CyberStatusBadge(
    text: String,
    statusColor: Color = NeonEmerald,
    icon: ImageVector = Icons.Default.Circle
) {
    Surface(
        color = NexusSurfaceDark,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, NexusBorderHairline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(statusColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text.uppercase(),
                color = NexusSilver,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Frosted glass modifier providing high-translucency milky surface, micro-hairline border,
 * delicate reflection highlight, and soft diffuse shadow.
 */
fun Modifier.frostedGlass(
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = GlassMilkyCard,
    borderColor: Color = GlassBorder,
    borderWidth: Dp = 0.5.dp,
    elevation: Dp = 3.dp
) = this
    .shadow(
        elevation = elevation,
        shape = shape,
        spotColor = GlassShadow,
        ambientColor = GlassShadow.copy(alpha = 0.03f)
    )
    .background(backgroundColor, shape)
    .border(width = borderWidth, color = borderColor, shape = shape)
    .clip(shape)

/**
 * Minimalist frosted glass status indicator container.
 * Precision 34dp circular lens with delicate micro-refraction and clean indicator pin.
 */
@Composable
fun NexusConnectionIndicator(
    state: com.example.viewmodel.ConnectionState,
    onIndicatorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (indicatorColor, isChecking) = when (state) {
        is com.example.viewmodel.ConnectionState.Online -> Color(0xFF059669) to false
        is com.example.viewmodel.ConnectionState.Checking -> Color(0xFF64748B) to true
        is com.example.viewmodel.ConnectionState.Error,
        is com.example.viewmodel.ConnectionState.NoKey -> Color(0xFFE11D48) to false
    }

    val infiniteTransition = rememberInfiniteTransition(label = "indicatorTransition")
    val checkingAlpha = if (isChecking) {
        val anim by infiniteTransition.animateFloat(
            initialValue = 0.35f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(750),
                repeatMode = RepeatMode.Reverse
            ),
            label = "checkingAlpha"
        )
        anim
    } else {
        1.0f
    }

    Surface(
        modifier = modifier
            .size(34.dp)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                spotColor = GlassShadow,
                ambientColor = GlassShadow.copy(alpha = 0.02f)
            )
            .clickable(onClick = onIndicatorClick),
        shape = CircleShape,
        color = GlassMilkyWhite,
        border = BorderStroke(0.5.dp, GlassBorder)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Subtle glass refraction gradient
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(GlassHighlight.copy(alpha = 0.6f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )

            // Outer soft aura
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(indicatorColor.copy(alpha = 0.16f * checkingAlpha), CircleShape)
            )
            // Core luminous point
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(indicatorColor.copy(alpha = checkingAlpha), CircleShape)
            )
        }
    }
}

@Composable
fun ApiKeyWarningBanner(
    onConfigureClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        color = Color(0xFFFFFBEB), // Soft warm champagne/amber tint on pure white
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, NeonAmber.copy(alpha = 0.40f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Key,
                contentDescription = null,
                tint = NeonAmber,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Gemini API Key Required",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Text(
                    text = "Configure in AI Studio Secrets or enter key in Settings.",
                    color = NexusSilverMuted,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onConfigureClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusSilverBright),
                border = BorderStroke(0.5.dp, NexusBorderHairline),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Setup", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun CyberChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NexusBorderHairline
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = NexusSurfaceDark,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, NexusBorderHairline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = NexusSilver,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
