package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback



@Composable
fun ThinButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "Cerrar",
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Black,
        contentColor = Color.White,
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.White
    ),
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .padding(1.dp),
        enabled = enabled,
        shape = buttonShapeMedium,
        colors = colors,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(1.dp),
            style = TextStyle(
                fontFamily = SFProMediumFontFamily(),
                fontWeight = FontWeight.Bold,
                lineHeight = 10.sp,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            text = text
        )
    }
}


@Composable
fun RegularButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = Color.Black,
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.DarkGray
    )
) {
    Button(
        modifier = modifier
            .shadow(4.dp, buttonShapeMedium),
        enabled = enabled,
        shape = buttonShapeMedium,
        colors = colors,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { iconVector ->
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Text(
                style = TextStyle(
                    fontFamily = SFProMediumFontFamily(),
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                text = text
            )
        }
    }
}

@Composable
fun DialogButtonFullWidth(
    text: String = "Cerrar",
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Black, contentColor = Color.White
    ),
    onDismissRequest: (() -> Unit)? = null
) {
    Button(modifier = Modifier.fillMaxWidth(),
        shape = dialogButtonBothtShapeBig,
        colors = buttonColors,
        onClick = {
            triggerHapticFeedback()
            onDismissRequest?.invoke() }) {
        Text(
            modifier = Modifier.padding(10.dp),
            style = TextStyle(
                fontFamily = SFProMediumFontFamily(),
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            text = text
        )
    }
}