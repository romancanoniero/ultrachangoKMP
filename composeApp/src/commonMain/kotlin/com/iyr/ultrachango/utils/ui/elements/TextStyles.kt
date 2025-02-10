package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily

@Composable
 fun StyleLight() = TextStyle(
    fontFamily = SFProMediumFontFamily(),
    fontSize = textSize12,
    fontWeight = FontWeight.ExtraLight
)

@Composable
 fun StyleBigTitle() = TextStyle(
    fontFamily = SFProMediumFontFamily(),
    fontSize = 64.sp,
    fontWeight = FontWeight.ExtraBold
)


@Composable
fun StyleButton() = TextStyle(
    fontFamily = SFProMediumFontFamily(),
    fontSize = textSize20,
    fontWeight = FontWeight.ExtraBold,
    color = Color.Black
)


@Composable
fun StyleTextRegular() = TextStyle(
    fontFamily = SFProMediumFontFamily(),
    fontSize = textSize16,
    fontWeight = FontWeight.Normal,
    color = Color.Black
)

@Composable
fun StyleTextMedium() = TextStyle(
    fontFamily = SFProMediumFontFamily(),
    fontSize = textSize18,
    fontWeight = FontWeight.Normal,
    color = Color.Black
)

@Composable
fun StyleTextBig() = TextStyle(
    fontFamily = SFProMediumFontFamily(),
    fontSize = textSize20,
    fontWeight = FontWeight.Normal,
    color = Color.Black
)