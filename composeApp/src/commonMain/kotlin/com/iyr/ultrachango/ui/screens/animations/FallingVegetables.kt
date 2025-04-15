package com.iyr.ultrachango.ui.screens.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class VegetableFinalPosition(
    val x: Float,
    val y: Float,
    val rotation: Float = 0f
)

@Composable
fun FallingVegetables(
    vegetables: List<DrawableResource>,
    numberOfVegetables: Int,
    startY: Float,
    vegetablePositions: List<Float>,
    finalPositions: List<VegetableFinalPosition>,
    basketImage: DrawableResource,
    onAllAnimationsComplete: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var completedFallAnimations by remember { mutableStateOf(0) }

    // Obtenemos el painter del basket para calcular su alto
    val basketPainter = painterResource(basketImage)
    val basketHeight = basketPainter.intrinsicSize.height.dp.value

    // Animaciones de aparición
    val appearAnimations = remember(vegetables.size) {
        vegetables.map { Animatable(initialValue = 0f) }
    }

    // Animaciones de caída en Y
    val fallAnimations = remember(vegetables.size) {
        vegetables.map { Animatable(initialValue = startY) }
    }

    // Animaciones de rotación
    val rotationAnimations = remember(vegetables.size) {
        vegetables.mapIndexed { index, _ ->
            Animatable(initialValue = 0f)
        }
    }

    // Estado para controlar qué verduras están activas
    val activeVegetables = remember { mutableStateListOf<Int>() }

    // Efecto para iniciar las animaciones secuencialmente
    LaunchedEffect(Unit) {
        vegetables.forEachIndexed { index, _ ->
            delay(200L) // Espera antes de iniciar la siguiente verdura
            activeVegetables.add(index)
        }
    }

    // Efectos individuales para cada verdura
    vegetables.forEachIndexed { index, _ ->
        LaunchedEffect(activeVegetables.contains(index)) {
            if (activeVegetables.contains(index)) {
                // Primero la animación de aparición
                appearAnimations[index].animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                
                // Inmediatamente después, la animación de caída y rotación
                val duration = Random.nextLong(800, 1200)
                
                // Animamos la posición Y
                fallAnimations[index].animateTo(
                    targetValue = finalPositions[index].y,
                    animationSpec = tween(
                        durationMillis = duration.toInt(),
                        easing = FastOutSlowInEasing
                    )
                )
                
                // Animamos la rotación
                rotationAnimations[index].animateTo(
                    targetValue = finalPositions[index].rotation,
                    animationSpec = tween(
                        durationMillis = duration.toInt(),
                        easing = FastOutSlowInEasing
                    )
                )
                
                completedFallAnimations++
            }
        }
    }

    // Verificar si todas las animaciones han terminado
    LaunchedEffect(completedFallAnimations) {
        if (completedFallAnimations == vegetables.size) {
            onAllAnimationsComplete()
        }
    }

    Box {
        vegetables.forEachIndexed { index, vegetable ->
            if (activeVegetables.contains(index)) {
                val appearAnimation = appearAnimations[index]
                val fallAnimation = fallAnimations[index]
                val rotationAnimation = rotationAnimations[index]
                
                Image(
                    painter = painterResource(vegetable),
                    contentDescription = "Vegetable $index",
                    modifier = Modifier
                        .offset(
                            x = vegetablePositions[index].dp,
                            y = fallAnimation.value.dp
                        )
                        .size(200.dp)
                        .scale(appearAnimation.value)
                        .alpha(appearAnimation.value)
                        .graphicsLayer {
                            rotationZ = rotationAnimation.value
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}