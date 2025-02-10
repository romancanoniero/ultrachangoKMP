package com.iyr.ultrachango.ui.screens.locations.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iyr.ultrachango.ui.screens.locations.main.LocationsDetailsViewModel
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.utils.ui.capitalizeFirstLetter
import com.iyr.ultrachango.utils.ui.elements.MyTexField
import com.iyr.ultrachango.utils.ui.elements.customShapeBig
import com.iyr.ultrachango.utils.ui.elements.dialogButtonLeftShapeBig
import com.iyr.ultrachango.utils.ui.elements.dialogButtonRightShapeBig
import com.iyr.ultrachango.utils.ui.places.PlacesAutocomplete2
import com.iyr.ultrachango.utils.ui.places.models.CustomPlace
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LocationDialog(
    onAccept: (name: String, location : CustomPlace) -> Unit,
    onDismiss: () -> Unit,
    title: String = "Ubicacion",
    acceptText: String = "Aceptar",
    cancelText: String = "Cancelar",
    vm: LocationsDetailsViewModel = koinViewModel(),
) {

    var locationText by remember { mutableStateOf("".toString()) }

    val focusRequester = remember { FocusRequester() }

    val state = vm.state

    var selectedPlace: CustomPlace? by remember { mutableStateOf(null) }
    var locationName: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxSize()
        //    .background(Color.Green)
        , verticalArrangement = Arrangement.Center
    ) {


        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(.70f),
            verticalArrangement = Arrangement.Center
        ) {
            Dialog(properties = DialogProperties(
                usePlatformDefaultWidth = true,
                dismissOnClickOutside = true,
                dismissOnBackPress = true

            ),

                onDismissRequest = { /* Do nothing on outside click */ },

            )
            {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.White, shape = customShapeBig)
                        //   .border(0.5.dp, Color.Black, shape = customShapeBig)
                        .shadow(elevation = 2.dp, shape = customShapeBig).clip(customShapeBig),

                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier

                            .padding(
                                vertical = 1.dp, horizontal = 10.dp
                            )

                    ) {

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = TextStyle(
                                fontFamily = SFProMediumFontFamily(),
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize

                            ),
                            text = title

                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        MyTexField(
                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                            value = locationName,
                            onValueChange = { it: TextFieldValue ->
                                locationName = it
                            },
                            placeHolderText = "Nombre de la Ubicacion",

                            keyboardOptions = null,
                            keyboardActions = null
                        )


                        PlacesAutocomplete2(modifier = Modifier.fillMaxWidth()
                            .focusRequester(focusRequester),
                            placeholderText = "Ingresa la direccion ",
                            onPlaceSelected = { place ->
                                selectedPlace = place
                                locationText = if (selectedPlace != null) selectedPlace.toString()
                                else ""
                            })
//                        PlacesAutocomplete()
                        /*
                        SearchableDropdown(

                            onSearch = { it->
                                locationText = it },

                        )


                        Box(modifier = Modifier.weight(1f))
                        {

                            OutlinedTextField(
                                value = locationText,
                                onValueChange = { it : String -> locationText = it },
                                label = {
                                    Text(locationText)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                        }
*/
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            verticalAlignment = Alignment.Bottom,
                        ) {

                            Button(modifier = Modifier.fillMaxWidth().weight(1f),
                                shape = dialogButtonLeftShapeBig,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black, contentColor = Color.White
                                ),
                                enabled = locationText.isNotEmpty() && selectedPlace != null,
                                onClick = {
                                    triggerHapticFeedback()
                                    selectedPlace?.let{
                                        onAccept(locationName.text.capitalizeFirstLetter(),
                                            it)
                                    }
                                }) {
                                Text(acceptText)
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
                                Text(cancelText)
                            }
                        }
                    }
                }
            }
        }
    }
}