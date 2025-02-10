package com.iyr.ultrachango.ui.dialogs

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iyr.ultrachango.utils.ui.elements.customShapeBig
import com.iyr.ultrachango.utils.ui.elements.dialogButtonLeftShapeBig
import com.iyr.ultrachango.utils.ui.elements.dialogButtonRightShapeBig
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.LocalDate

@Composable
fun DateSelectionDialog(
    title: String,
    message: String,
    acceptText: String,
    cancelText: String,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,

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
                        WheelDatePicker(
                            startDate = LocalDate(
                                year = 2025,
                                monthNumber = 10,
                                dayOfMonth = 20,
                            ),
                            minDate = LocalDate(
                                year = 2025,
                                monthNumber = 10,
                                dayOfMonth = 20,
                            ),
                            maxDate = LocalDate(
                                year = 2025,
                                monthNumber = 10,
                                dayOfMonth = 20,
                            ),
                            size = DpSize(200.dp, 100.dp),
                            rowCount = 5,
                            textStyle = MaterialTheme.typography.titleSmall,
                            textColor = Color(0xFFffc300),
                            selectorProperties = WheelPickerDefaults.selectorProperties(
                                enabled = true,
                                shape = RoundedCornerShape(0.dp),
                                color = Color(0xFFf1faee).copy(alpha = 0.2f),
                                border = BorderStroke(2.dp, Color(0xFFf1faee))
                            )
                        ) { snappedDateTime -> }



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