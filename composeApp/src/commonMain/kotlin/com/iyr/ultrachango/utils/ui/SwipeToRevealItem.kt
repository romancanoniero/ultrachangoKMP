package com.iyr.ultrachango.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRevealItem(content: @Composable () -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val size = with(LocalDensity.current) { 50.dp.toPx() }
    val anchors = mapOf(0f to 0, -size to 1)  // 0 is the original position, -size is the revealed position
    Box(
        modifier = Modifier
            .fillMaxWidth()
           // .height(80.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.9f) },
                orientation = Orientation.Horizontal
            )
    ) {
        // Reveal area behind the item
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(IntrinsicSize.Min)
                .align(Alignment.CenterEnd)

        ) {
            Row(modifier = Modifier
                .fillMaxHeight()
                .height(IntrinsicSize.Min)

                , verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null, modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = {} ),tint = Color.DarkGray)

                Spacer(modifier = Modifier.width(24.dp))
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = {


                    }),tint = Color.DarkGray)
            }
        }

        // Main content which moves with swipe
        Box(
            modifier = Modifier
               // .fillMaxSize()
                .offset(x = swipeableState.offset.value.dp)
        ) {
            content()
        }
    }
}