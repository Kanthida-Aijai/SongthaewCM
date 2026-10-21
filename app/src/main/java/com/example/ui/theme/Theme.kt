package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CmRedPrimary,
    onPrimary = Color.White,
    primaryContainer = CmRedDark,
    onPrimaryContainer = Color.White,
    secondary = AmberAccent,
    onSecondary = Slate950,
    background = Slate950,
    surface = Slate900,
    surfaceVariant = Slate800,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Slate200
)

private val LightColorScheme = lightColorScheme(
    primary = CmRedPrimary,
    onPrimary = Color.White,
    primaryContainer = CmRedLight,
    onPrimaryContainer = CmRedDark,
    secondary = CmBlue,
    onSecondary = Color.White,
    tertiary = AmberAccent,
    background = Slate100,
    surface = Color.White,
    surfaceVariant = Slate50,
    onBackground = Slate900,
    onSurface = Slate900,
    onSurfaceVariant = Slate700
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
