package com.iyr.ultrachango.ui.screens.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource

@Composable
fun FallingVegetable(
    vegetable: DrawableResource,
    modifier: Modifier = Modifier,
    xPosition: Float = 0f,
    startY: Float = 0f,
    endY: Float = 1000f,
    durationMillis: Int = 1500,
    delayMillis: Int = 0,
    onAnimationComplete: () -> Unit = {}
) {
    var animationComplete by remember { mutableStateOf(false) }
    var currentY by remember { mutableStateOf(startY) }

    LaunchedEffect(Unit) {
        currentY = endY
    }

    val yPosition by animateFloatAsState(
        targetValue = currentY,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = LinearEasing
        ),
        finishedListener = {
            if (!animationComplete) {
                animationComplete = true
                onAnimationComplete()
            }
        }
    )

    Image(
        modifier = Modifier.height(250.dp)
            .aspectRatio(1/1f)
            .offset(y = yPosition.dp,
                x = xPosition.dp
              ),
        bitmap = imageResource(vegetable),
        contentDescription = "Vegetable falling",
    )
}