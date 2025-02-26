package com.iyr.ultrachango.utils.ui.device

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


@Composable
fun rememberClipboardManager(): ClipboardManager {
    return remember {
       ClipboardManager()
    }
}