@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.iyr.ultrachango.utils.ui.device.shared

enum class SharePlatform {
    FACEBOOK, INSTAGRAM, WHATSAPP, TELEGRAM, SMS, EMAIL
}

expect class ShareManager() {
    fun shareLink(link: String, platform: SharePlatform)
}

