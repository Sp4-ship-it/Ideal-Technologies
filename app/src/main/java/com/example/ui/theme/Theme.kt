package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = IdealOrange,
    onPrimary = Color.White,
    primaryContainer = IdealNavyLight,
    onPrimaryContainer = Color.White,
    secondary = IdealCyan,
    onSecondary = IdealNavyDark,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = IdealCyanBright,
    tertiary = IdealCyanBright,
    onTertiary = IdealNavyDark,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF1E3553)
)

private val LightColorScheme = lightColorScheme(
    primary = IdealNavyDark,
    onPrimary = Color.White,
    primaryContainer = IdealNavyLight,
    onPrimaryContainer = Color.White,
    secondary = IdealOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEAD6),
    onSecondaryContainer = Color(0xFF7A2B00),
    tertiary = IdealCyan,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) DarkBackground.toArgb() else IdealNavyDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme && false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
