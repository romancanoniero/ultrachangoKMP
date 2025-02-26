package com.iyr.ultrachango.utils.ui.device

import AppContext.context
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

actual class ClipboardManager {
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


    actual fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copiado", Toast.LENGTH_SHORT).show()
    }
}