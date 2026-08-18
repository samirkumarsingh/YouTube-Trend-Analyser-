package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = YoutubeRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3B0B14),
    onPrimaryContainer = Color(0xFFFFDAD9),
    secondary = NeonCyan,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F58),
    onSecondaryContainer = Color(0xFF97F0FF),
    tertiary = ElectricViolet,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF321B66),
    onTertiaryContainer = Color(0xFFEADBFF),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    outlineVariant = Color(0xFF3E4357)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to creator studio dark mode
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
