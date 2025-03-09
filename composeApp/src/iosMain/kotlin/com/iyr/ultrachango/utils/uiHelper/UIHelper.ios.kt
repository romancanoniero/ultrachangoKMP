package com.iyr.ultrachango.utils.uiHelper

// shared/iosMain/platform/UIHelper.kt
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

actual class UIHelper {
    actual fun getRootController(): Any? {
        val keyWindow = UIApplication.sharedApplication.keyWindow
        return keyWindow?.rootViewController
    }
}