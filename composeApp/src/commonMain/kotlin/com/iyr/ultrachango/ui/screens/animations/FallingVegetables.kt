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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class VegetableFinalPosition(
    val x: Float,
    val y: Float,
    val rotation: Float = 0f,
    val scale: Float = 1f
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
    val alphaAnimations = remember(vegetables.size) {
        vegetables.map { Animatable(initialValue = 0f) }
    }

    // Animaciones de caída en Y
    val yAnimations = remember(vegetables.size) {
        vegetables.map { Animatable(initialValue = -500f) }
    }

    // Animaciones de rotación
    val rotationAnimations = remember(vegetables.size) {
        vegetables.mapIndexed { index, _ ->
            Animatable(initialValue = 0f)
        }
    }

    // Animaciones de escala
    val scaleAnimations = remember(vegetables.size) {
        vegetables.mapIndexed { index, _ ->
            Animatable(initialValue = 0.5f)
        }
    }

    // Estado para controlar qué verduras están activas
    val activeVegetables = remember { mutableStateListOf<Int>() }

    // Activar verduras secuencialmente
    LaunchedEffect(Unit) {
        for (i in 0 until numberOfVegetables) {
            activeVegetables.add(i)
            delay(50)
        }
    }
    
    // Animar cada verdura cuando se activa
    vegetables.forEachIndexed { index, vegetable ->
        LaunchedEffect(activeVegetables.contains(index)) {
            if (activeVegetables.contains(index)) {
                // Espera un tiempo antes de empezar la caída
                delay(index * 300L)
                
                // Aparece la verdura
                alphaAnimations[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = FastOutSlowInEasing
                    )
                )
                
                // Ajusta la escala
                scaleAnimations[index].animateTo(
                    targetValue = finalPositions[index].scale,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = FastOutSlowInEasing
                    )
                )
                
                // Cae y rota simultáneamente
                scope.launch {
                    yAnimations[index].animateTo(
                        targetValue = finalPositions[index].y,
                        animationSpec = keyframes {
                            durationMillis = 800 // Reducido de 1500 a 800 para caída más rápida
                            finalPositions[index].y at 700 with FastOutSlowInEasing
                            (finalPositions[index].y - 20) at 750 with FastOutSlowInEasing // Rebote hacia arriba
                            finalPositions[index].y at 800 with FastOutSlowInEasing // Vuelve a la posición final
                        }
                    )
                }
                
                scope.launch {
                    rotationAnimations[index].animateTo(
                        targetValue = finalPositions[index].rotation,
                        animationSpec = tween(
                            durationMillis = 800, // Reducido para coincidir con la caída
                            easing = FastOutSlowInEasing
                        )
                    )
                }
                
                if (index == numberOfVegetables - 1) {
                    delay(1500)
                    completedFallAnimations = numberOfVegetables
                    onAllAnimationsComplete()
                }
            }
        }
    }

    Box {
        vegetables.forEachIndexed { index, vegetable ->
            if (activeVegetables.contains(index)) {
                val alphaAnimation = alphaAnimations[index]
                val yAnimation = yAnimations[index]
                val rotationAnimation = rotationAnimations[index]
                val scaleAnimation = scaleAnimations[index]
                
                Image(
                    painter = painterResource(vegetable),
                    contentDescription = "Vegetable $index",
                    modifier = Modifier
                        .offset(
                            x = vegetablePositions[index].dp,
                            y = yAnimation.value.dp
                        )
                        .size(200.dp)
                        .scale(scaleAnimation.value)
                        .alpha(alphaAnimation.value)
                        .graphicsLayer {
                            rotationZ = rotationAnimation.value
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}