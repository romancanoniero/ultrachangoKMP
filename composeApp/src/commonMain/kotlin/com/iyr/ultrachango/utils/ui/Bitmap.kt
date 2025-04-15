package com.iyr.ultrachango.utils.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter


fun Painter.toGrayscale(): Painter {
    return object : Painter() {
        override val intrinsicSize: Size
            get() = this@toGrayscale.intrinsicSize

        override fun DrawScope.onDraw() {
            val colorMatrix = ColorMatrix().apply {
                setToSaturation(0f) // Convierte a escala de grises
            }

            with(this@toGrayscale) {
                draw(
                    size = size,
                    colorFilter = ColorFilter.colorMatrix(colorMatrix)
                )
            }
        }
    }
}