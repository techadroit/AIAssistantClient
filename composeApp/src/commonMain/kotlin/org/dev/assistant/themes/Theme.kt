package org.dev.assistant.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = primaryColorDark,
    primaryContainer = primaryVariantColorDark,
    onPrimary = onPrimary,
    secondary = secondaryColorDark,
    secondaryContainer = secondaryColorDark,
    onSecondaryContainer = accentColor,
    background = backgroundColorDark,
    surface = surfaceColorDark,
    surfaceVariant = surfaceColorDark,
    onBackground = onBackgroundColorDark,
    onSurface = onSurfaceColorDark,
    error = errorColor
)

private val LightColorPalette = lightColorScheme(
    primary = primaryColorLight,
    primaryContainer = primaryVariantColorLight,
    secondary = secondaryColorLight,
    secondaryContainer = surfaceColor,
    onSecondaryContainer = lightAccentColor,
    onPrimary = onPrimaryLight,
    background = backgroundColorLight,
    surface = surfaceColorLight,
    surfaceVariant = surfaceColorLight,
    onBackground = onBackgroundColorLight,
    onSurface = onSurfaceColorLight,
    error = errorColor
)

private val UserInterestColorPalette = lightColorScheme(
    primary = surfaceColor,
    onPrimary = onPrimaryLight,
    primaryContainer = surfaceColor,
    secondaryContainer = surfaceColor,
    onSecondaryContainer = lightAccentColor,
    surface = surfaceColor,
    secondary = secondaryColor,
    background = backgroundColor,
    onBackground = onBackgroundColorLight,
    onSurface = onSurfaceColorLight
)

@Composable
fun HomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    /*
    Enable this to support light and dark mode
     */
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }

//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colors.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    AppTheme(darkTheme = darkTheme, colors = LightColorPalette, content = content)
}
//
//@Composable
//fun UserInterestComposable(
//    darkTheme: Boolean,
//    content: @Composable () -> Unit
//) {
//    AppTheme(darkTheme = darkTheme, colors = UserInterestColorPalette, content = content)
//}

@Composable
fun AppTheme(
    darkTheme: Boolean,
    colors: ColorScheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colors,
        typography = Type.create(darkTheme),
        shapes = shapes,
        content = content
    )
}
