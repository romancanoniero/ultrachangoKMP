package com.iyr.ultrachango.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.iyr.ultrachango.ui.theme.AppTheme
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.ui.elements.buttonShapeSmall
import com.iyr.ultrachango.utils.ui.elements.customShapeBig


@Composable
fun LoadingDialog(
) {
    var selectedItems by remember { mutableStateOf(listOf<CreditEntities>()) }


            Dialog(

                properties = DialogProperties(
                    usePlatformDefaultWidth = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true

                ),
                onDismissRequest = { /* Do nothing on outside click */ })
            {
                CircularProgressIndicator()

                /*
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
                            text = "title"

                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize

                            ),
                            text = "paragraoh1")

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize

                            ),
                            text = "paragraoh2")


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = buttonShapeSmall,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                ),
                                onClick = { }
                            ) {
                                Text("Cerrar")
                            }
                            Spacer(modifier = Modifier.width(8.dp))


                        }
                    }
                }
                */
            }
        }


