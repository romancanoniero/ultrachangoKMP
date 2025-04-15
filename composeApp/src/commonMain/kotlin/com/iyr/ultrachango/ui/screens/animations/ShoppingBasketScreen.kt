package com.iyr.ultrachango.ui.screens.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import com.iyr.ultrachango.validateForm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.baguette
import ultrachango2.composeapp.generated.resources.basket
import ultrachango2.composeapp.generated.resources.botella
import ultrachango2.composeapp.generated.resources.choclo
import ultrachango2.composeapp.generated.resources.lechuga
import ultrachango2.composeapp.generated.resources.uvas
import ultrachango2.composeapp.generated.resources.vino
import ultrachango2.composeapp.generated.resources.zanahoria

@Composable
fun ShoppingBasketScreen(
    navController: NavHostController,
    authRepository: AuthRepository
) {

    val isLoggedIn by remember { mutableStateOf(authRepository.isUserSignedIn()) }
    // Obtengo el usuario actual

    val me = authRepository.getCurrentUser()
    val checkLoggedIn = isLoggedIn && validateForm(
        validateImage = true,
        firstName = me?.firstName,
        lastName = me?.lastName,
        imageProfile = me?.profilePicturePath,
        gender = me?.gender,
        birthDate = me?.birthDate,
    )
    val isProfileComplete by remember { mutableStateOf(checkLoggedIn) }


    var animationComplete by remember { mutableStateOf(false) }
    var showFinalAnimation by remember { mutableStateOf(false) }
    var showExitAnimation by remember { mutableStateOf(false) }
    var currentStage by remember { mutableStateOf(1) }
    var totalAnimationComplete by remember { mutableStateOf(false) }
    val basketYPosition = 300.dp

    // Animaciones del conjunto
    val finalScaleAnimation = remember { Animatable(initialValue = 1f) }
    val exitAnimation = remember { Animatable(initialValue = 0f) }

    // Animaciones del texto
    val textScaleAnimation = remember { Animatable(initialValue = 0.5f) }
    val textAlphaAnimation = remember { Animatable(initialValue = 0f) }
    val textWidthAnimation = remember { Animatable(initialValue = 1f) }
    val textYOffsetAnimation = remember { Animatable(initialValue = 350f) }

    // Configuración de imágenes
    val basketPainter = painterResource(Res.drawable.basket)
    val basketHeight = basketPainter.intrinsicSize.height.dp.value

    val vegetables = remember {
        listOf(
            Res.drawable.zanahoria,
            Res.drawable.lechuga,
            Res.drawable.choclo,
            Res.drawable.botella,
            Res.drawable.uvas,
            Res.drawable.vino,
            Res.drawable.baguette,

            )
    }

    val vegetablePositions = remember(vegetables.size) {
        listOf(
            170f, // Zanahoria
            130f, // Lechuga
            50f,  // Choclo
            100f,  // Botella
            60f, // Uvas
            80f, // Vino
            70f, // Baguette
        )
    }

    val finalPositions = remember(vegetables.size) {
        listOf(
            VegetableFinalPosition(
                x = vegetablePositions[0],
                y = basketYPosition.value + 40,
                rotation = 15f
            ),
            VegetableFinalPosition(
                x = vegetablePositions[1],
                y = basketYPosition.value + 40,
                rotation = -10f
            ),
            VegetableFinalPosition(
                x = vegetablePositions[2],
                y = basketYPosition.value + 40,
                rotation = -25f
            ),
            VegetableFinalPosition(
                x = vegetablePositions[3],
                y = basketYPosition.value + 40,
                rotation = 10f
            ),
            VegetableFinalPosition(
                x = vegetablePositions[4],
                y = basketYPosition.value + 40,
                rotation = 10f
            ),
            VegetableFinalPosition(
                x = vegetablePositions[5],
                y = basketYPosition.value + 40,
                rotation = 10f
            ),
            VegetableFinalPosition(
                x = vegetablePositions[6],
                y = basketYPosition.value + 40,
                rotation = 10f
            )
        )
    }

    LaunchedEffect(animationComplete) {
        if (animationComplete) {
            // Etapa 2: Reducción de tamaño
            currentStage = 2
            finalScaleAnimation.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )

            // Etapa 3: Aparece el texto
            currentStage = 3
            showFinalAnimation = true
            textAlphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )

            // Etapa 4: El carrito se mueve hacia la izquierda y el texto se expande/contrae
            currentStage = 4
            delay(500)
            showExitAnimation = true

            // Animación del carrito
            launch {
                exitAnimation.animateTo(
                    targetValue = -2000f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            // Animación del texto
            launch {
                // Primero expande el texto a casi todo el ancho de la pantalla
                textWidthAnimation.animateTo(
                    targetValue = 3.5f,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )

                // Luego contrae el texto y sube al centro mientras aumenta de tamaño
                launch {
                    textWidthAnimation.animateTo(
                        targetValue = 1.7f,
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    )
                }

                launch {
                    textYOffsetAnimation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }

            // Espera a que todas las animaciones terminen
            delay(1500)
            totalAnimationComplete = true
        }
    }

    // Efecto para redirigir cuando la animación total termine
    LaunchedEffect(totalAnimationComplete) {
        if (totalAnimationComplete) {
            // Aquí puedes agregar la redirección a otra pantalla
            // Por ejemplo:
            //     navController.navigate("ruta_de_la_siguiente_pantalla")

            var start = if (isLoggedIn) {
                if (isProfileComplete)
                    RootRoutes.HomeRoute.route
                else {
                    RootRoutes.SetupProfileRoute.createRoute(me)
                }
            } else {
                RootRoutes.LandingRoute.route
            }

            navController.navigate(start)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Contenedor del carrito y verduras
        Box(
            modifier = Modifier
                .scale(finalScaleAnimation.value)
                .offset(x = exitAnimation.value.dp)
        ) {
            Box {
                FallingVegetables(
                    vegetables = vegetables as List<DrawableResource>,
                    numberOfVegetables = vegetables.size,
                    startY = 0f,
                    vegetablePositions = vegetablePositions,
                    finalPositions = finalPositions,
                    basketImage = Res.drawable.basket,
                    onAllAnimationsComplete = {
                        animationComplete = true
                    }
                )

                Image(
                    painter = basketPainter,
                    contentDescription = "Shopping basket",
                    modifier = Modifier
                        .offset(y = basketYPosition)
                        .zIndex(1f)
                )
            }
        }

        // Texto UltraChango (siempre centrado e independiente del carrito)
        if (showFinalAnimation) {
            Text(
                text = "UltraChango",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = textYOffsetAnimation.value.dp)
                    .scale(textScaleAnimation.value * textWidthAnimation.value)
                    .alpha(textAlphaAnimation.value)
            )
        }
    }
}