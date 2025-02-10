package com.iyr.ultrachango.ui.screens.shoppinglist.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.utils.ui.TwoButtonsDialog

@Composable
fun RenameDialog(
    shoppingList: ShoppingList? = null,
    onAccept: (newName: String) -> Unit,
    onDismiss: () -> Unit,
    title: String = "Renombrar",
    message: String = "Nombre de la lista",
    acceptText: String = "Aceptar",
    cancelText: String = "Cancelar"
) {

    var listName  by remember { mutableStateOf(shoppingList?.listName ?: "".toString()) }
/*
    Column(
        modifier = Modifier.fillMaxSize()
        //    .background(Color.Green)
        ,
        verticalArrangement = Arrangement.Center
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.fillMaxHeight(.70f)
                .height(500.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center
        ) {


            Dialog(
                properties = DialogProperties(
                    usePlatformDefaultWidth = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,

                ),
                onDismissRequest = { /* Do nothing on outside click */ })
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = customShapeBig)
                        //   .border(0.5.dp, Color.Black, shape = customShapeBig)
                        .shadow(elevation = 2.dp, shape = customShapeBig)
                        .clip(customShapeBig)
                        .imePadding()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                    ,

                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                         //   .fillMaxHeight(.30f)
                            .padding(
                                vertical = 20.dp,
                                horizontal = 10.dp
                            )

                    ) {

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(40.dp),
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize

                            ),
                            text = title

                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Box(modifier = Modifier.weight(1f))
                        {
                            OutlinedTextField(
                                value = listName,
                                onValueChange = { it : String -> listName = it },
                                label = {
                                    Text(message)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                        }

                        Spacer(modifier = Modifier.height(20.dp))

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
                                shape = buttonShapeSmall,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    onAccept.invoke(listName)
                                }
                            ) {
                                Text(acceptText)
                            }


                            Spacer(modifier = Modifier.width(2.dp))

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                shape = buttonShapeSmall,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                onClick = onDismiss

                            ) {
                                Text(cancelText)
                            }
                        }
                    }
                }
            }
        }
    }

   */
    //----------

    TwoButtonsDialog(
        title = title,
        message = message,
        acceptText = acceptText,
        cancelText = cancelText,
        onAccept = { onAccept(listName) },
        onDismiss = onDismiss
    ) {
        OutlinedTextField(
            value = listName,
            onValueChange = { listName = it },
            label = { Text(message) },
            modifier = Modifier.fillMaxWidth()
        )
    }


    ///-------




}