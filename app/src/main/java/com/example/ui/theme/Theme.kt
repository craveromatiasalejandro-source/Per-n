package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ArgentineBluePrimaryDark,
    onPrimary = ArgentineBlueOnPrimaryDark,
    primaryContainer = ArgentineBlueContainerDark,
    onPrimaryContainer = ArgentineOnBlueContainerDark,
    secondary = SunGoldSecondaryDark,
    onSecondary = SunGoldOnSecondaryDark,
    secondaryContainer = SunGoldContainerDark,
    onSecondaryContainer = SunGoldOnContainerDark,
    background = HistoricalDarkBackground,
    onBackground = HistoricalDarkOnSurface,
    surface = HistoricalDarkSurface,
    onSurface = HistoricalDarkOnSurface,
    surfaceVariant = HistoricalDarkSurfaceVariant,
    onSurfaceVariant = HistoricalDarkOnSurfaceVariant,
    outline = HistoricalDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = ArgentineBluePrimary,
    onPrimary = ArgentineBlueOnPrimary,
    primaryContainer = ArgentineBlueContainer,
    onPrimaryContainer = ArgentineOnBlueContainer,
    secondary = SunGoldSecondary,
    onSecondary = SunGoldOnSecondary,
    secondaryContainer = SunGoldContainer,
    onSecondaryContainer = SunGoldOnContainer,
    background = HistoricalParchmentBackground,
    onBackground = HistoricalOnSurface,
    surface = HistoricalParchmentSurface,
    onSurface = HistoricalOnSurface,
    surfaceVariant = HistoricalParchmentSurfaceVariant,
    onSurfaceVariant = HistoricalOnSurfaceVariant,
    outline = HistoricalOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our tailored classical Argentine palette by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
