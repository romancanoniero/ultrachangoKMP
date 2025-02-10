package com.iyr.ultrachango.utils.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import com.iyr.ultrachango.utils.ui.elements.textSize14

@Composable
fun MyText(modifier: Modifier = Modifier, text: String, fontSize: TextUnit = textSize14)
{
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize)
}