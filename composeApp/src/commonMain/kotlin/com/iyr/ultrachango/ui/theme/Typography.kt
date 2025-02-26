package com.iyr.ultrachango.ui.theme


import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    h1 = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
    h3 = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
    h4 = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
    body1 = TextStyle(fontSize = 16.sp),
    body2 = TextStyle(fontSize = 14.sp),
    caption = TextStyle(fontSize = 12.sp),
    button = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
)