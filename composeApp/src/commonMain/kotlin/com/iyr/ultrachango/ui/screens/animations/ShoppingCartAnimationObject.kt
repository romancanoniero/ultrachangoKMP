package com.iyr.ultrachango.ui.screens.animations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class CartLoadingArea(
    val top: Float = 0.24f,      // 15% desde el borde superior
    val bottom: Float = 0.26f,   // 51% desde el borde inferior
    val topLeft: Float = 0.18f,  // 10% desde el borde izquierdo en la parte superior
    val topRight: Float = 0.044f, // 10% desde el borde derecho en la parte superior
    val bottomLeft: Float = 0.29f,  // 20% desde el borde izquierdo en la parte inferior
    val bottomRight: Float = 0.12f  // 20% desde el borde derecho en la parte inferior
)

@Composable
fun ShoppingCartAnimationObject(
    basketImage: DrawableResource,
    scale: Float = 1f,
    modifier: Modifier = Modifier,
    loadingArea: CartLoadingArea = CartLoadingArea(),
    showDebugArea: Boolean = true
) {
    val basketPainter = painterResource(basketImage)
    val aspectRatio = basketPainter.intrinsicSize.width / basketPainter.intrinsicSize.height
    val totalHeight = basketPainter.intrinsicSize.height
    val totalWidth = basketPainter.intrinsicSize.width

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
        )

        // Área de carga (debug)
        if (showDebugArea) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .scale(scale)
            ) {
                val topY = loadingArea.top * totalHeight
                val bottomY = (1f - loadingArea.bottom) * totalHeight

                val topLeftX = loadingArea.topLeft * totalWidth
                val topRightX = (1f - loadingArea.topRight) * totalWidth
                val bottomLeftX = loadingArea.bottomLeft * totalWidth
                val bottomRightX = (1f - loadingArea.bottomRight) * totalWidth

                val path = Path().apply {
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