package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import de.drick.compose.hotpreview.HotPreview


@HotPreview(name = "phone dark", widthDp = 400, heightDp = 800, fontScale = 1f, darkMode = true)
@HotPreview(name = "phone", widthDp = 400, heightDp = 800, fontScale = 1.5f, darkMode = false, density = 1f)
@Composable
fun IncDecSelectorPreview()
{
    MaterialTheme {
        IncDecSelector(
            counter = remember { mutableStateOf(0.0) },
            prevValue = remember { mutableStateOf(0.0) },
            onIncrement = {},
            onDecrement = {}
        )
    }
}

@Composable
fun IncDecSelector(
    counter: MutableState<Double>,
    prevValue: MutableState<Double>,
//    qtyRecord: PreparationProduct,
    onIncrement: (Double) -> Unit,
    onDecrement: (Double) -> Unit
) {
    Row {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    triggerHapticFeedback()
                    counter?.let {
                        if (it.value > 0) {
                            it.value--
                        }
                    }
                    try {
                        onDecrement(
                            counter?.value ?: 0.0
                        )
                        prevValue?.value = counter?.value ?: 0.0
                    } catch (e: Exception) {
                        counter?.value = prevValue?.value ?: 0.0
                    }
                },
                modifier = Modifier.size(CircleSize.MEDIUM.value).clip(CircleShape).shadow(4.dp, CircleShape)
                    .background(Color.Gray).clickable(onClick = {

                    },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() })
            ) {
                Text("-", color = Color.White, style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${counter?.value}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    triggerHapticFeedback()
                    counter?.let {
                        it.value++

                    }
                    try {
                        onIncrement(
                            counter?.value ?: 0.0
                        )
                        prevValue?.value = counter?.value ?: 0.0
                    } catch (e: Exception) {
                        counter?.value = prevValue?.value ?: 0.0
                    }
                },
                modifier = Modifier.size(CircleSize.MEDIUM.value).clip(CircleShape).shadow(4.dp, CircleShape)
                    .background(Color.Gray).clickable(onClick = {

                    },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() })
            ) {
                Text("+", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
