package com.iyr.ultrachango.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.iyr.ultrachango.utils.ui.elements.dialogButtonLeftShapeBig
import com.iyr.ultrachango.utils.ui.elements.dialogButtonRightShapeBig

@Composable
fun TwoButtonsDialog(
    title: String,
    message: String,
    acceptText: String,
    cancelText: String,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background( Color.White)
                //   .height(500.dp)
                .imePadding(), verticalArrangement = Arrangement.Center
        ) {
            Dialog(
                properties = DialogProperties(
                    usePlatformDefaultWidth = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                ), onDismissRequest = onDismiss
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()

                        .background(Color.White, shape = customShapeBig)
                        .shadow(elevation = 2.dp, shape = customShapeBig).clip(customShapeBig)
                        .imePadding().padding(WindowInsets.systemBars.asPaddingValues()),
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(Color.White)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            modifier = Modifier

                                .align(Alignment.Center).height(40.dp), style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize
                            ), text = title
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth()
                        .height(10.dp)
                        .background(Color.White).alpha(.0f))
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth().height(IntrinsicSize.Min)
                            .padding(top = 20.dp, bottom = 0.dp).padding(horizontal = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier.height(IntrinsicSize.Min)
                                .weight(1f)
                        ) {
                            content()
                        }

                    }

                    Box(modifier = Modifier.fillMaxWidth()
                        .height(10.dp)
                        .background(Color.White))


                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .height(IntrinsicSize.Min).fillMaxWidth()
                                          .padding(top = 16.dp)
                        ,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            shape = dialogButtonLeftShapeBig,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, contentColor = Color.White
                            ),
                            onClick = onAccept
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 10.dp), text = acceptText
                            )
                        }



                        Spacer(modifier = Modifier.width(2.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            shape = dialogButtonRightShapeBig,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White, contentColor = Color.Black
                            ),
                            onClick = onDismiss
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 10.dp), text = cancelText
                            )
                        }

                    }


                }
            }
        }
    }
}