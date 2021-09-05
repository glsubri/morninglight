package ch.subri.morninglight.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = MorningLightColors.ArcticWaters,
    primaryVariant = MorningLightColors.ArcticOcean,
    secondary = MorningLightColors.ClearIce,
    secondaryVariant = MorningLightColors.ArcticWaters,
    background = MorningLightColors.PolarNight900,
    surface = MorningLightColors.PolarNight900,
    error = MorningLightColors.AuroraRed,
    onPrimary = MorningLightColors.PolarNight700,
    onSurface = MorningLightColors.SnowStorm500,
    onBackground = MorningLightColors.SnowStorm500,
    onError = MorningLightColors.SnowStorm300,
)

@Composable
fun MorningLightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = MorningLightTypography,
        shapes = Shapes,
        content = content
    )
}