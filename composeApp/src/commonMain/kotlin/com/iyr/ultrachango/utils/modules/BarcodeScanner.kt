package com.iyr.ultrachango.utils.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
 expect fun RunBarcodeScanning( onBarcodeScanned: (String?) -> Unit)

@Composable
fun BarcodeScannerScreen(
    navController: NavHostController, onResult: (String) -> Unit
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    Box(modifier = Modifier.fillMaxSize()) {

        RunBarcodeScanning( onBarcodeScanned = { barcode ->
            onResult(barcode.toString())
        })

        IconButton(modifier = Modifier.align(Alignment.Center)
            .background(Color.White, shape = CircleShape)
            //.clickable {   navController.popBackStack() }
            ,
            onClick = {



                navController.popBackStack()
            })
        {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black


            )
        }

    }

}


