package com.iyr.ultrachango.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun ShowKeyboard(showKeyboard : Boolean,focusRequester : FocusRequester? = null)
{
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(showKeyboard) {
        if (showKeyboard) {
            focusRequester?.requestFocus()
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }
}