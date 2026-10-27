package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusLightColorScheme = lightColorScheme(
    primary = NexusTitanium,
    onPrimary = Color.White,
    primaryContainer = NexusSurfaceElevated,
    onPrimaryContainer = NexusSilverBright,
    secondary = NexusSilver,
    onSecondary = Color.White,
    secondaryContainer = NexusCardDark,
    onSecondaryContainer = NexusSilverBright,
    tertiary = ParticleLightBlue,
    onTertiary = Color.White,
    background = Color.White,
    onBackground = NexusSilverBright,
    surface = NexusCardDark,
    onSurface = NexusSilverBright,
    surfaceVariant = NexusSurfaceDark,
    onSurfaceVariant = NexusSilverMuted,
    outline = NexusBorderHairline,
    outlineVariant = NexusBorderSubtle
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
                window.statusBarColor = android.graphics.Color.WHITE
                window.navigationBarColor = android.graphics.Color.WHITE
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
