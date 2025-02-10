package com.iyr.ultrachango.ui.dialogs


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.ui.elements.customShapeBig


@Composable
fun EmptyDialog(
    modifier: Modifier = Modifier,
    onAccept: (() -> Unit)? = null,
    onDismissRequest: () -> Unit = {},
    title: String = "Confirmar",
    message: String = "Nombre de la lista",
    content: @Composable () -> Unit,
) {

    Column(
        modifier = modifier.fillMaxSize()
        ,
        verticalArrangement = Arrangement.Center
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.70f),
            verticalArrangement = Arrangement.Center
        ) {
            Dialog(

                properties = DialogProperties(
                    usePlatformDefaultWidth = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true

                ),
                onDismissRequest = { /* Do nothing on outside click */ })
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = customShapeBig)
                        //   .border(0.5.dp, Color.Black, shape = customShapeBig)
                        .shadow(elevation = 2.dp, shape = customShapeBig)
                        .clip(customShapeBig),

                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier.padding(
                            vertical = 20.dp,
                            horizontal = 10.dp
                        )
                    ) {

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize

                            ),
                            text = title

                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        content()

                    }
                }
            }
        }
    }
}