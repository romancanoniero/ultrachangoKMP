package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import com.iyr.ultrachango.ui.theme.Typography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun H1Text(text: String) {
    Text(
        text = text,
        style = Typography.h1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun H2Text(text: String) {
    Text(
        text = text,
        style = Typography.h2,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun H3Text(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    textDecoration : TextDecoration = TextDecoration.None,
    color : Color = Color.Unspecified,
    shadow: Boolean = false,
    shadowColor : Color = Color.Unspecified,
) {

    Text(
        modifier = modifier,
        text = text,
        style = Typography.h3.copy(
            fontWeight = fontWeight,
            textDecoration = textDecoration,
            shadow = if (shadow) Shadow(color = shadowColor, blurRadius = 7.5f) else Shadow.None
        ),
        color = color,

        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Body1Text(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    textDecoration : TextDecoration = TextDecoration.None
) {
    Text(
        modifier = modifier,
        text = text,
        style = Typography.body1.copy(
            fontWeight = fontWeight,
            textDecoration = textDecoration
        ),
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Body2Text(text: String) {
    Text(
        text = text,
        style = Typography.body2,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CaptionText(text: String) {
    Text(
        text = text,
        style = Typography.caption,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ButtonText(text: String) {
    Text(
        text = text,
        style = Typography.button,
        maxLines = 1
    )
}

@Preview
@Composable
fun PreviewTypography() {
    H1Text("Este es un título H1")
}