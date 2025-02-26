package com.iyr.ultrachango.utils.ui.device

import platform.UIKit.UIPasteboard

actual class ClipboardManager {
    actual fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard().string = text
        // No hay un equivalente directo a Toast en iOS
        println("Copiado al portapapeles: $text")
    }
}