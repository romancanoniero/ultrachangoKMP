package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ShareButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    Box(
        modifier = modifier
            .size(40.dp)

            .background(Color.White, CircleShape)
            .clickable(onClick = onClick)
            ,
        contentAlignment = Alignment.Center
    )
    {
        Icon(
            Icons.Filled.IosShare,
            contentDescription = "Share",
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }


}
