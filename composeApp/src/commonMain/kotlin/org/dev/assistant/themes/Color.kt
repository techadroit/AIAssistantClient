package org.dev.assistant.themes

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val accentColor = Color(0xFFfaa627)
val lightAccentColor = Color(0xFFf96163)
val errorColor = Color(0xffd00036)

val primaryColorDark = Color(0xFF4E342E)
val primaryVariantColorDark = Color(0xff3E2723)
val secondaryColorDark = Color(0xff3E2723)
val onSecondaryColorDark = Color(0xFFfaa627)
val backgroundColorDark = Color(0xFF4E342E)
val surfaceColorDark = Color(0xFF5D4037)
val onBackgroundColorDark = Color(0xFFF5F5F5)
val onSurfaceColorDark = Color(0xFFF5F5F5)
val onPrimary = Color(0xFFF5F5F5)

val primaryColorLight = Color(0xFF344955)
val primaryVariantColorLight = Color(0xFF5f7481)
val secondaryColorLight = lightAccentColor
val backgroundColorLight = Color(0xFFfafafa)
val surfaceColorLight = Color.White
val onBackgroundColorLight = Color(0xFF000000)
val onSurfaceColorLight = Color(0xFF000000)
val onPrimaryLight = Color(0xFF000000)

val lightGrayColor = Color(0xFF9E9E9E)
val chatBackgroundColor = Color(0xFFF5F5F5)
val cardBackground = Color(0xFFF3f1f3)
val priceColor = Color(0xFF0A5147)

// colors for use interest screen
val surfaceColor = Color(0xFFFEDBD0)
val backgroundColor = Color(0xFFFEEAE6)
val secondaryColor = Color(0xFF442C2E)
//F5F5F6

fun ColorScheme.getChatBackgroundColor(): Color {
    return chatBackgroundColor
}

fun ColorScheme.productCartBackground(): Color {
    return cardBackground
}

fun ColorScheme.getPriceColor(): Color {
    return priceColor
}