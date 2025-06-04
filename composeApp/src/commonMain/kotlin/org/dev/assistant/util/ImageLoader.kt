package org.dev.assistant.util

import aiassistant.composeapp.generated.resources.Res
import aiassistant.composeapp.generated.resources.adidas_running_red
import aiassistant.composeapp.generated.resources.adidas_soccer_black
import aiassistant.composeapp.generated.resources.adidas_tennis_white
import aiassistant.composeapp.generated.resources.adidas_training_black
import aiassistant.composeapp.generated.resources.nike_basketball_blue
import aiassistant.composeapp.generated.resources.nike_casual_black
import aiassistant.composeapp.generated.resources.nike_grey_shoes
import aiassistant.composeapp.generated.resources.nike_hiking_red
import aiassistant.composeapp.generated.resources.nike_kids_running_white
import aiassistant.composeapp.generated.resources.nike_trail_brown
import aiassistant.composeapp.generated.resources.nike_walking_white
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

/**
 * Extracts image name from URL and loads corresponding drawable from resources.
 * For example, from "http://example.com/adidas_running_red.jpg" it extracts "adidas_running_red"
 * and loads the corresponding drawable resource.
 *
 * @param url The URL containing the image name
 * @return The Painter loaded from resources or null if resource not found
 */
@Composable
fun UrlImage(
    url: String,
    modifier: Modifier = Modifier.size(300.dp) // Default size of 100dp
) {
    // Extract the filename from the URL (everything after the last slash)
    val fileName = url.substringAfterLast('/')

    // Remove the extension from the filename
    val resourceName = fileName.substringBeforeLast('.')

    // Load the resource using the extracted name
    Image(
        painter = painterResource(drawableMap[resourceName] ?: Res.drawable.adidas_running_red),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit // Ensures the image fits within the bounds
    )
}

val drawableMap = mapOf(
    "adidas_running_red" to Res.drawable.adidas_running_red,
    "adidas_soccer_black" to Res.drawable.adidas_soccer_black,
    "adidas_tennis_white" to Res.drawable.adidas_tennis_white,
    "adidas_training_black" to Res.drawable.adidas_training_black,
    "nike_basketball_blue" to Res.drawable.nike_basketball_blue,
    "nike_casual_black" to Res.drawable.nike_casual_black,
    "nike_grey_shoes" to Res.drawable.nike_grey_shoes,
    "nike_hiking_red" to Res.drawable.nike_hiking_red,
    "nike_kids_running_white" to Res.drawable.nike_kids_running_white,
    "nike_trail_brown" to Res.drawable.nike_trail_brown,
    "nike_walking_white" to Res.drawable.nike_walking_white,
    "vans_skateboarding_blue" to Res.drawable.vans_skateboarding_blue,
    "vans_skateboarding_brown" to Res.drawable.vans_skateboarding_brown,
    "vans_slipon_blue" to  Res.drawable.vans_slipon_blue,
    "vans_classic_white" to  Res.drawable.vans_classic_white,
)