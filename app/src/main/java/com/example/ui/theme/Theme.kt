package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusLightColorScheme = lightColorScheme(
    primary = TextGraphite,
    onPrimary = PearlWhitePure,
    primaryContainer = GlassMilkyElevated,
    onPrimaryContainer = TextGraphite,
    secondary = TextCharcoal,
    onSecondary = PearlWhitePure,
    secondaryContainer = GlassMilkyCard,
    onSecondaryContainer = TextCharcoal,
    tertiary = PastelIceBlue,
    onTertiary = PearlWhitePure,
    background = PearlWhite,
    onBackground = TextGraphite,
    surface = PearlWhitePure,
    onSurface = TextGraphite,
    surfaceVariant = GlassMilkyCard,
    onSurfaceVariant = TextSlate,
    outline = GlassBorderSubtle,
    outlineVariant = GlassBorder
)

@Composable
fun NexusAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = NexusLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = PearlWhite.toArgb()
                window.navigationBarColor = PearlWhite.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
