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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.iyr.ultrachango.data.models.CreditEntities
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.ui.elements.DialogButtonFullWidth
import com.iyr.ultrachango.utils.ui.elements.customShapeBig



@Composable
fun ErrorDialog(
    title: String, message: String,
    onDismissRequest: (() -> Unit)? = null

) {


    Column(
        modifier = Modifier.fillMaxSize()
        //    .background(Color.Green)
        , verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(.70f),
            verticalArrangement = Arrangement.Center
        ) {
            Dialog(
                properties = DialogProperties(
                    usePlatformDefaultWidth = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true

                ), onDismissRequest = { /* Do nothing on outside click */ }) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.White, shape = customShapeBig)
                        .shadow(elevation = 2.dp, shape = customShapeBig).clip(customShapeBig),

                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier.padding(
                            horizontal = 2.dp
                        ).padding(top = 20.dp, bottom = 1.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Black,
                                lineHeight = 24.sp,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize
                            ),
                            text = title

                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize

                            ), text = message
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
/*
                            Button(modifier = Modifier.fillMaxWidth(),
                                shape = dialogButtonBothtShapeBig,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black, contentColor = Color.White
                                ),
                                onClick = { onDismissRequest?.invoke() }) {
                                Text(
                                    modifier = Modifier.padding(10.dp),
                                    style = TextStyle(
                                        fontFamily = SFProMediumFontFamily(),
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 20.sp,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                                    ),
                                    text = "Cerrar"
                                )
                            }
*/
                            DialogButtonFullWidth(
                                text = "Putear",
                                buttonColors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black, contentColor = Color.White
                                ),
                                onDismissRequest = onDismissRequest
                            )

                            Spacer(modifier = Modifier.width(8.dp))


                        }
                    }
                }
            }
        }
    }
}


