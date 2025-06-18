package com.iyr.ultrachango.config

import platform.Foundation.NSBundle

actual object BuildConfig {
    private val bundle = NSBundle.mainBundle

    actual val GOOGLE_WEB_CLIENT_ID: String
        get() = bundle.objectForInfoDictionaryKey("GIDServerClientID") as? String
            ?: throw IllegalStateException("GOOGLE_WEB_CLIENT_ID no encontrado en Info.plist")

    actual val FACEBOOK_APP_ID: String
        get() = bundle.objectForInfoDictionaryKey("FacebookAppID") as? String
            ?: throw IllegalStateException("FACEBOOK_APP_ID no encontrado en Info.plist")

    actual val FACEBOOK_CLIENT_TOKEN: String
        get() = bundle.objectForInfoDictionaryKey("FacebookClientToken") as? String
            ?: throw IllegalStateException("FACEBOOK_CLIENT_TOKEN no encontrado en Info.plist")
}