package com.iyr.ultrachango.utils.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.ui.dialogs.EmptyDialog
import com.iyr.ultrachango.utils.ui.elements.CustomButton
import org.koin.dsl.module


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageOptionDialog(
    onDismissRequest: () -> Unit,
    onGalleryRequest: () -> Unit = {},
    onCameraRequest: () -> Unit = {}
) {

    EmptyDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
        ) {

            CustomButton(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                onClick = {
                    onGalleryRequest()
                    onDismissRequest()
                },
                enabled = true,

                colors = ButtonDefaults.buttonColors(),
              ) {

                Text(
                    text = "Gallery",

                )

            }

                Spacer(modifier = Modifier.height(16.dp))


            CustomButton(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                onClick = {
                    onCameraRequest()
                    onDismissRequest()
                },
                enabled = true,
                colors = ButtonDefaults.buttonColors(),
            ) {

                Text(
                    text = "Camera",

                )

            }

            }

        }


    }