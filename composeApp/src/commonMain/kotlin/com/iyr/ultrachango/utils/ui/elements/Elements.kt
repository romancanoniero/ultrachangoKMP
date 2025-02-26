package com.iyr.ultrachango.utils.ui.elements


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.ui.theme.textIntense


val screenOuterPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)

val customShapeBig = RoundedCornerShape(0.dp, 45.dp, 45.dp, 45.dp)
val customShapeMedium = RoundedCornerShape(0.dp, 25.dp, 25.dp, 25.dp)
val customShape = RoundedCornerShape(0.dp, 15.dp, 15.dp, 15.dp)
val customShapeSmall = RoundedCornerShape(0.dp, 5.dp, 5.dp, 5.dp)


val buttonShapeSmall = RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
val buttonShapeMedium = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
val buttonShapeBig = RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)

val dialogButtonLeftShapeSmall = RoundedCornerShape(0.dp, 0.dp, 0.dp, 15.dp)
val dialogButtonLeftShapeMedium = RoundedCornerShape(0.dp, 0.dp, 0.dp, 25.dp)
val dialogButtonLeftShapeBig = RoundedCornerShape(0.dp, 0.dp, 0.dp, 45.dp)

val dialogButtonRightShapeSmall = RoundedCornerShape(0.dp, 0.dp, 15.dp, 0.dp)
val dialogButtonRightShapeMedium = RoundedCornerShape(0.dp, 0.dp, 25.dp, 0.dp)
val dialogButtonRightShapeBig = RoundedCornerShape(0.dp, 0.dp, 45.dp, 0.dp)


val dialogButtonBothShapeSmall = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
val dialogButtonBothShapeMedium = RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp)
val dialogButtonBothtShapeBig = RoundedCornerShape(0.dp, 0.dp, 45.dp, 45.dp)


val searchShape = RoundedCornerShape(2.dp, 15.dp, 15.dp, 15.dp)

var customCardColors = CardColors(
    containerColor = Color.White,
    contentColor = Color.Black,
    disabledContainerColor = Color.LightGray,
    disabledContentColor = Color.Gray
)


val itemListHeightStandard = 60.dp


val textSizeLandingTitle: TextUnit
    @Composable
    get() = 30.sp

val textSizeLandingMessage: TextUnit
    @Composable
    get() = 24.sp


val textSize28: TextUnit
    @Composable
    get() = 28.sp

val textSize26: TextUnit
    @Composable
    get() = 26.sp

val textSize24: TextUnit
    @Composable
    get() = 24.sp

val textSize20: TextUnit
    @Composable
    get() = MaterialTheme.typography.titleMedium.fontSize


val textSize18: TextUnit
    @Composable
    get() = MaterialTheme.typography.titleSmall.fontSize


val textSize16: TextUnit
    @Composable
    get() = MaterialTheme.typography.bodyLarge.fontSize

val textSize14: TextUnit
    @Composable
    get() = MaterialTheme.typography.bodyMedium.fontSize

val textSize12: TextUnit
    @Composable
    get() = MaterialTheme.typography.labelSmall.fontSize


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,

    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Black,
        contentColor = Color.White,
        disabledContainerColor = Color.LightGray,
        disabledContentColor = Color.Gray
    ),
            content: @Composable () -> Unit?,
) {
    Button(
        modifier = modifier.height(50.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = colors,
        enabled = enabled
    ) {
        content()
    }
}


@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SwitchColors = SwitchDefaults.colors()
) {
    Switch(
        checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        colors = SwitchDefaults.colors()
    )
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit? = {},
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)
            .clickable { onClick() }

    ) {
        icon.let {
            Icon(
                imageVector = icon!!, contentDescription = "icon", modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            text = text,
            style = StyleTextMedium(),
            color = textIntense,

            )



    }

    Divider(
        modifier = Modifier.padding(horizontal = 10.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}


@Composable
fun MenuTitle(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    content: @Composable () -> Unit? = {},
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)

    ) {
        icon?.let { it ->
            Icon(
                imageVector = it, contentDescription = "icon", modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.padding(vertical = 14.dp),
            text = text,
            style =  StyleTextMedium().copy(fontWeight= FontWeight.Bold),
            color = textIntense,

            )
    }

    Divider(
        modifier = Modifier.padding(horizontal = 10.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}


