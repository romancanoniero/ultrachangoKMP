package com.iyr.ultrachango.utils.ui.device.shared

import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareManager actual constructor() {
    actual fun shareLink(link: String, platform: SharePlatform) {
        val url = NSURL.URLWithString(link) ?: return
        val activityItems = listOf(url)
        val activityController = UIActivityViewController(activityItems, null)
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(activityController, animated = true, completion = null)
    }
}