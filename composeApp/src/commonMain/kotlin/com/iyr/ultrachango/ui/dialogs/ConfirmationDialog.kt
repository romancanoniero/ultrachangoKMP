package com.iyr.ultrachango.ui.dialogs


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.ui.TwoButtonsDialog
import com.iyr.ultrachango.utils.ui.elements.textSize16


@Composable
fun ConfirmationDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Confirmar",
    message: String = "Nombre de la lista",
    acceptText: String = "Aceptar",
    cancelText: String = "Cancelar"
) {


    TwoButtonsDialog(
        title = title,
        message = message,
        acceptText = acceptText,
        cancelText = cancelText,
        onAccept = { onAccept() },
        onDismiss = onDismiss
    ) {


        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                ,
            style = TextStyle(
                fontFamily = SFProMediumFontFamily(),
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp,
                fontSize = textSize16),
            text = message
        )
    }

    /*
    Column(
        modifier = Modifier.fillMaxSize()
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
                        .shadow(elevation = 2.dp, shape = customShapeBig)
                        .clip(customShapeBig),

                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .background(Color.Green)
                           .fillMaxHeight(.30f)
                            .padding(top = 20.dp,
                                bottom = 2.dp)
                           .padding(
                                horizontal = 4.dp
                            )

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

                        Spacer(modifier = Modifier.height(30.dp))




                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Normal,
                                lineHeight = 24.sp,
                                fontSize = textSize16),
                            text = message
                        )

                        Spacer(modifier = Modifier.weight(1f))


                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = dialogButtonLeftShapeBig,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {
                                onAccept.invoke()
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 10.dp),
                                text =  acceptText
                            )
                        }


                        Spacer(modifier = Modifier.width(3.dp))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = dialogButtonRightShapeBig,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = onDismiss

                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 10.dp),
                                text = cancelText)
                        }
                    }
                }
            }
        }
    }
    */
}