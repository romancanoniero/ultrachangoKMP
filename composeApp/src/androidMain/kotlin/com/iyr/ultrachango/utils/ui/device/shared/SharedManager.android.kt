package com.iyr.ultrachango.utils.ui.device.shared

import AppContext
import android.content.Intent
import android.net.Uri

/*
actual class ShareManager actual constructor() {
    private val context = AppContext.context

    actual fun shareLink(link: String, platform: SharePlatform) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }

        val packageName = when (platform) {
            SharePlatform.FACEBOOK -> "com.facebook.katana"
            SharePlatform.INSTAGRAM -> "com.instagram.android"
            SharePlatform.WHATSAPP -> "com.whatsapp"
            SharePlatform.TELEGRAM -> "org.telegram.messenger"
            else -> null
        }

        if (packageName != null) {
            intent.setPackage(packageName)
        } else {
            when (platform) {
                SharePlatform.SMS -> {
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse("sms:")
                    intent.putExtra("sms_body", link)
                }
                SharePlatform.EMAIL -> {
                    intent.action = Intent.ACTION_SENDTO
                    intent.data = Uri.parse("mailto:")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Mira este enlace")
                    intent.putExtra(Intent.EXTRA_TEXT, link)
                }
                else -> {}
            }
        }
        context.startActivity(Intent.createChooser(intent, "Compartir con"))
    }
}

 */


actual class ShareManager actual constructor() {
    private val context = AppContext.context


    actual fun shareLink(link: String, platform: SharePlatform) {

        val context = AppContext.context
        val activity = AppContext.activity  ?: return  // Obtener la actividad actual


        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }

        val packageName = when (platform) {
            SharePlatform.FACEBOOK -> "com.facebook.katana"
            SharePlatform.INSTAGRAM -> "com.instagram.android"
            SharePlatform.WHATSAPP -> "com.whatsapp"
            SharePlatform.TELEGRAM -> "org.telegram.messenger"
            else -> null
        }

        if (packageName != null) {
            intent.setPackage(packageName)
        } else {
            when (platform) {
                SharePlatform.SMS -> {
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse("sms:")
                    intent.putExtra("sms_body", link)
                }
                SharePlatform.EMAIL -> {
                    intent.action = Intent.ACTION_SENDTO
                    intent.data = Uri.parse("mailto:")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Mira este enlace")
                    intent.putExtra(Intent.EXTRA_TEXT, link)
                }
                else -> {}
            }
        }

        // Agregar FLAG_ACTIVITY_NEW_TASK para evitar el error
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        activity.startActivity(Intent.createChooser(intent, "Compartir con"))
    }
}