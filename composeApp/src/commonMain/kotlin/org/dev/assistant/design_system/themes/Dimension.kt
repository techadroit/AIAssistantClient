package org.dev.assistant.design_system.themes

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MaterialTheme.dimension() = LocalDimensionProvider.current

fun createDimension(
    paddingBody: Dp = 8.dp,
    paddingTitle: Dp = 12.dp,
    paddingSubtitle: Dp = 8.dp,
    paddingPara: Dp = 4.dp,
    thumbnailHeight: Dp = 180.dp,
    thumbnailWidth: Dp = 160.dp,
    cardHeight: Dp = 230.dp,
    cardHorizontalPadding: Dp = 12.dp,
    cardVerticalPadding: Dp = 8.dp,
    contentPadding: Dp = 80.dp,
    videoListGrid: GridCells = GridCells.Fixed(2),
    elevation: Dp = 4.dp,
    drawerMinWidth: Dp = 240.dp
) = Dimension(
    paddingBody = paddingBody,
    paddingTitle = paddingTitle,
    paddingSubtitle = paddingSubtitle,
    thumbnailHeight = thumbnailHeight,
    thumbnailWidth = thumbnailWidth,
    cardHeight = cardHeight,
    cardHorizontalPadding = cardHorizontalPadding,
    paddingPara = paddingPara,
    cardVerticalPadding = cardVerticalPadding,
    contentPadding = contentPadding,
    videoListGrid = videoListGrid,
    elevation = elevation,
    drawerMinWidth = drawerMinWidth
)

fun compactDimension() = createDimension()
fun mediumDimension() = createDimension()
fun expandedDimension() = createDimension()

data class Dimension(
    val spacingXs: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 12.dp,
    val spacingLarge: Dp = 16.dp,
    val spacingXLarge: Dp = 32.dp,
    val paddingBody: Dp = 8.dp,
    val paddingTitle: Dp = 12.dp,
    val paddingSubtitle: Dp = 8.dp,
    val paddingPara: Dp = 4.dp,
    val thumbnailHeight: Dp = 180.dp,
    val thumbnailWidth: Dp = 160.dp,
    val cardHeight: Dp = 230.dp,
    val cardHorizontalPadding: Dp = 12.dp,
    val cardVerticalPadding: Dp = 8.dp,
    val contentPadding: Dp = 80.dp,
    val videoListGrid: GridCells = GridCells.Fixed(2),
    val maxContentWidth: Dp = 400.dp,
    val elevation: Dp,
    val drawerMinWidth: Dp
)

val LocalDimensionProvider = compositionLocalOf { createDimension() }