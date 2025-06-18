package com.iyr.ultrachango.ui.screens.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.utils.ui.otp.dpToPx
import com.iyr.ultrachango.validateForm
import kotlinx.coroutines.delay
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
import kotlin.math.PI
import kotlin.math.atan2

// Clase para almacenar las dimensiones del carrito
data class BasketBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val innerLeft: Float,
    val innerRight: Float,
    val innerTop: Float,
    val innerLoadingAreaBottom: Float,  // Límite inferior del área de carga
    val bottomLeftX: Float,  // Coordenada X del punto inferior izquierdo del trapecio
    val bottomRightX: Float  // Coordenada X del punto inferior derecho del trapecio
)

// Estructura para almacenar la información de cada parábola
data class ParabolaInfo(
    val startX: Float,
    val endX: Float,
    val endY: Float,
    val vertexY: Float,
    val isLeftSide: Boolean
)

@Composable
fun ShoppingBasketScreen(
    navController: NavHostController,
    authRepository: AuthRepository? = null
) {
    // Estado para las dimensiones del carrito
    var basketBounds by remember { mutableStateOf<BasketBounds?>(null) }
    
    val isLoggedIn by remember { mutableStateOf(authRepository?.isUserSignedIn() ?: false) }
    val me = authRepository?.getCurrentUser()
    val checkLoggedIn = isLoggedIn && validateForm(
        validateImage = true,
        firstName = me?.firstName,
        lastName = me?.lastName,
        imageProfile = me?.profilePicturePath,
        gender = me?.gender,
        birthDate = me?.birthDate,
    )
    val isProfileComplete by remember { mutableStateOf(checkLoggedIn) }

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




    val productsToAnimate = remember<List<DrawableResource>> {
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

    val screenWidth = com.iyr.ultrachango.utils.ui.device.getScreenWidth().dpToPx()
    val screenHeight = com.iyr.ultrachango.utils.ui.device.getScreenHeight().dpToPx()

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con los recorridos parabólicos
        ParabolicPathBackground(
            modifier = Modifier.fillMaxSize(),
            numberOfPaths = 5,
            screenHeight = screenHeight,
            screenWidth = screenWidth,
            basketBounds = basketBounds,
            productsToAnimate = productsToAnimate
        )

        // Agregamos los productos animados
        basketBounds?.let { bounds ->
            productsToAnimate.forEachIndexed { index, productImage ->
                val trayectoria = TrayectoriaParabolica(
                    inicioX = if (index % 2 == 0) 0f else screenWidth,
                    finX = bounds.bottomLeftX + (bounds.bottomRightX - bounds.bottomLeftX) * (index / (productsToAnimate.size - 1f)),
                    finY = bounds.innerLoadingAreaBottom,
                    verticeY = screenHeight * (0.3f * (index % 5) / 5 + 0.1f),
                    esLadoIzquierdo = index % 2 == 0
                )
                
                ProductoAnimado(
                    imagenProducto = productImage,
                    trayectoria = trayectoria,
                    retrasoInicial = index * 500,
                    tamanoProducto = 40.dp
                )
            }
        }

        // Mantenemos el ShoppingCartAnimationObject existente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            ShoppingCartAnimationObject(
                basketImage = Res.drawable.basket,
                scale = .6f,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(2f),
                showDebugArea = true,
                onBoundsCalculated = { bounds ->
                    basketBounds = bounds
                }
            )
        }
    }
}

// Nueva versión de ShoppingCartAnimationObject que reporta sus dimensiones
@Composable
fun ShoppingCartAnimationObject(
    basketImage: DrawableResource,
    scale: Float,
    modifier: Modifier = Modifier,
    showDebugArea: Boolean = false,
    onBoundsCalculated: (BasketBounds) -> Unit = {}
) {
    val basketPainter = painterResource(basketImage)
    val aspectRatio = basketPainter.intrinsicSize.width / basketPainter.intrinsicSize.height
    val totalHeight = basketPainter.intrinsicSize.height
    val totalWidth = basketPainter.intrinsicSize.width
    
    // Definición del área de carga según CartLoadingArea
    val topAreaPercent = 0.24f      // 24% desde el borde superior
    val bottomAreaPercent = 0.26f   // 26% desde el borde inferior
    val topLeftPercent = 0.18f      // 18% desde el borde izquierdo en la parte superior
    val topRightPercent = 0.044f    // 4.4% desde el borde derecho en la parte superior
    val bottomLeftPercent = 0.29f   // 29% desde el borde izquierdo en la parte inferior
    val bottomRightPercent = 0.12f  // 12% desde el borde derecho en la parte inferior

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        // Imagen del carrito
        Image(
            painter = basketPainter,
            contentDescription = "Shopping basket",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .scale(scale)
                .onGloballyPositioned { coordinates ->
                    // Calculamos las dimensiones del carrito
                    val imageWidth = coordinates.size.width.toFloat()
                    val imageHeight = coordinates.size.height.toFloat()
                    
                    // Calculamos el área de carga
                    val topY = coordinates.boundsInWindow().top + topAreaPercent * imageHeight
                    val bottomY = coordinates.boundsInWindow().bottom - bottomAreaPercent * imageHeight
                    
                    val topLeftX = coordinates.boundsInWindow().left + topLeftPercent * imageWidth
                    val topRightX = coordinates.boundsInWindow().right - topRightPercent * imageWidth
                    val bottomLeftX = coordinates.boundsInWindow().left + bottomLeftPercent * imageWidth
                    val bottomRightX = coordinates.boundsInWindow().right - bottomRightPercent * imageWidth
                    
                    // Creamos el objeto BasketBounds
                    val bounds = BasketBounds(
                        left = coordinates.boundsInWindow().left,
                        top = coordinates.boundsInWindow().top,
                        right = coordinates.boundsInWindow().right,
                        bottom = coordinates.boundsInWindow().bottom,
                        innerLeft = topLeftX,
                        innerRight = topRightX,
                        innerTop = topY,
                        innerLoadingAreaBottom = bottomY,
                        bottomLeftX = bottomLeftX,
                        bottomRightX = bottomRightX
                    )
                    
                    // Notificamos al componente padre
                    onBoundsCalculated(bounds)
                }
        )
        
        // Área de carga (debug)
        if (showDebugArea) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .scale(scale)
            ) {
                val topY = topAreaPercent * totalHeight
                val bottomY = (1f - bottomAreaPercent) * totalHeight

                val topLeftX = topLeftPercent * totalWidth
                val topRightX = (1f - topRightPercent) * totalWidth
                val bottomLeftX = bottomLeftPercent * totalWidth
                val bottomRightX = (1f - bottomRightPercent) * totalWidth

                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(topLeftX, topY)
                    lineTo(topRightX, topY)
                    lineTo(bottomRightX, bottomY)
                    lineTo(bottomLeftX, bottomY)
                    close()
                }

                drawPath(
                    path = path,
                    color = Color.Red.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun ParabolicPathBackground(
    modifier: Modifier = Modifier,
    numberOfPaths: Int = 5,
    screenHeight: Float,
    screenWidth: Float,
    basketBounds: BasketBounds? = null,
    productsToAnimate: List<DrawableResource> = emptyList()
) {
    // Estado para controlar el progreso de cada parábola
    val progressStates = remember {
        List(numberOfPaths * 2) { index ->
            Animatable(0f)
        }
    }

    // Estado para almacenar las parábolas disponibles
    val parabolas = remember(basketBounds) {
        basketBounds?.let { bounds ->
            buildList {
                // Parábolas desde la izquierda
                repeat(numberOfPaths) { index ->
                    val vertexY = screenHeight * (0.3f * index / numberOfPaths + 0.1f)
                    val loadingAreaProgress = index / (numberOfPaths - 1f)
                    val endX = bounds.bottomLeftX + (bounds.bottomRightX - bounds.bottomLeftX) * loadingAreaProgress
                    add(ParabolaInfo(
                        startX = 0f,
                        endX = endX,
                        endY = bounds.innerLoadingAreaBottom,
                        vertexY = vertexY,
                        isLeftSide = true
                    ))
                }
                // Parábolas desde la derecha
                repeat(numberOfPaths) { index ->
                    val vertexY = screenHeight * (0.3f * index / numberOfPaths + 0.1f)
                    val loadingAreaProgress = index / (numberOfPaths - 1f)
                    val endX = bounds.bottomRightX - (bounds.bottomRightX - bounds.bottomLeftX) * loadingAreaProgress
                    add(ParabolaInfo(
                        startX = screenWidth,
                        endX = endX,
                        endY = bounds.innerLoadingAreaBottom,
                        vertexY = vertexY,
                        isLeftSide = false
                    ))
                }
            }
        } ?: emptyList()
    }

    // Estado para almacenar la parábola asignada a cada producto
    val productParabolas = remember(parabolas) {
        if (parabolas.isNotEmpty()) {
            List(productsToAnimate.size) {
                parabolas.random()
            }
        } else {
            emptyList()
        }
    }

    // Estado para controlar la animación de productos
    val productAnimations = remember {
        List(productsToAnimate.size) { index ->
            Animatable(0f)
        }
    }

    // Iniciar las animaciones de parábolas
    progressStates.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            val randomDelay = (0..2000).random()
            delay(randomDelay.toLong())
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (numberOfPaths * 300).coerceAtLeast(2000),
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    // Iniciar las animaciones de productos
    productAnimations.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            val randomDelay = (500..3000).random()
            delay(randomDelay.toLong())
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(modifier = modifier) {
        // Dibujamos el Canvas con las parábolas
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Solo dibujamos las parábolas si tenemos las dimensiones del carrito
            basketBounds?.let { bounds ->
                // Función para calcular puntos de una parábola
                fun calculateParabolicPoints(parabola: ParabolaInfo, steps: Int = 20): List<Offset> {
                    return (0..steps).map { step ->
                        val progress = step.toFloat() / steps
                        val x = parabola.startX + (parabola.endX - parabola.startX) * progress
                        val normalizedX = (x - parabola.startX) / (parabola.endX - parabola.startX)
                        val y = parabola.vertexY + (parabola.endY - parabola.vertexY) * normalizedX * normalizedX
                        Offset(x, y)
                    }
                }

                // Dibujar todas las parábolas
                parabolas.forEachIndexed { index, parabola ->
                    val points = calculateParabolicPoints(parabola, 20)
                    val currentProgress = progressStates[index].value
                    val visiblePoints = points.take((points.size * currentProgress).toInt() + 1)
                    
                    visiblePoints.windowed(2) { (start, end) ->
                        val segmentProgress = (visiblePoints.indexOf(start).toFloat() / visiblePoints.size)
                        drawLine(
                            color = Color.Blue.copy(alpha = 0.5f * (1f - segmentProgress * 0.3f)),
                            start = start,
                            end = end,
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                        )
                    }
                }
            }
        }

        // Animamos los productos siguiendo sus parábolas asignadas
        basketBounds?.let {
            // Dibujamos cada producto
            productsToAnimate.forEachIndexed { index, productImage ->
                if (index < productParabolas.size && index < productAnimations.size) {
                    val parabola = productParabolas[index]
                    val progress = productAnimations[index].value
                    val productSize = 40.dp

                    ProductWithParabolicPath(
                        productImage = productImage,
                        parabola = parabola,
                        progress = progress,
                        productSize = productSize,
                        modifier = Modifier.alpha(
                            if (progress > 0.9f) {
                                (1f - (progress - 0.9f) * 10f).coerceIn(0f, 1f)
                            } else {
                                1f
                            }
                        )
                    )
                }
            }
        }
    }
}

// Función para calcular la posición en la parábola
private fun calculateParabolicPosition(parabola: ParabolaInfo, progress: Float): Offset {
    // Calculamos X basado en el progress
    val x = parabola.startX + (parabola.endX - parabola.startX) * progress
    
    // Calculamos Y usando una función cuadrática que pasa por vertexY y endY
    val a = (parabola.endY - parabola.vertexY) / ((parabola.endX - parabola.startX) * (parabola.endX - parabola.startX))
    val y = parabola.vertexY + a * (x - parabola.startX) * (x - parabola.startX)
    
    return Offset(x, y)
}

@Composable
fun ProductWithParabolicPath(
    productImage: DrawableResource,
    parabola: ParabolaInfo,
    progress: Float,
    productSize: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    // Calculamos la posición del centro del producto en la parábola
    val centerPosition = calculateParabolicPosition(parabola, progress)
    
    // Calculamos la inclinación basada en la pendiente de la parábola
    val slope = calculateParabolicSlope(parabola, progress).toDouble()
    val rotation = (atan2(slope, 1.0) * 180 / PI).toFloat()
    
    Box(
        modifier = modifier
            .size(productSize)
            .offset(
                x = (centerPosition.x - productSize.value / 2).dp,
                y = (centerPosition.y - productSize.value / 2).dp
            )
            .rotate(rotation)
    ) {
        Image(
            painter = painterResource(productImage),
            contentDescription = "Product",
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Función auxiliar para calcular la pendiente de la parábola en un punto
private fun calculateParabolicSlope(parabola: ParabolaInfo, progress: Float): Float {
    val x = parabola.startX + (parabola.endX - parabola.startX) * progress
    val a = (parabola.endY - parabola.vertexY) / ((parabola.endX - parabola.startX) * (parabola.endX - parabola.startX))
    return 2f * a * (x - parabola.startX)
}