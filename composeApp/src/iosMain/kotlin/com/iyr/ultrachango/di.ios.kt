package com.iyr.ultrachango

import com.iyr.fbauthentication.auth.PhoneAuthHelper
import com.iyr.fbauthentication.platform.FirebaseAuthPlatform
import com.iyr.ultrachango.config.BuildConfig
import com.iyr.ultrachango.data.preferences.UserPreferences
import com.iyr.ultrachango.data.preferences.UserPreferencesImpl
import dev.icerock.moko.permissions.ios.PermissionsController
import org.koin.dsl.module
import platform.UIKit.UIApplication
actual val nativeModule = module {
    single { FirebaseAuthPlatform(
        viewController =  UIApplication.sharedApplication().keyWindow!!.rootViewController!!,
        googleClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID ) }

//    single { FirebaseAuthPlatform( viewController = ) }
    single<PermissionsController> {
        PermissionsController()
    }
    single { PhoneAuthHelper() }
    single<UserPreferences> { UserPreferencesImpl() }
}