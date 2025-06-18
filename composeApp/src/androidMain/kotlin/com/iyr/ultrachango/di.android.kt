package com.iyr.ultrachango

import android.app.Activity
import androidx.activity.ComponentActivity
import com.iyr.fbauthentication.auth.PhoneAuthHelper
import com.iyr.fbauthentication.platform.FirebaseAuthPlatform
import com.iyr.ultrachango.data.preferences.UserPreferences
import com.iyr.ultrachango.data.preferences.UserPreferencesImpl
import dev.icerock.moko.permissions.PermissionsController
import org.koin.dsl.module

actual val nativeModule = module {
    single {
        FirebaseAuthPlatform(
            activity = AppContext.activity,
            googleClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        )
    }
    // single { PhoneVerificationViewModel() }
    single<PermissionsController> {
        PermissionsController(AppContext.context).apply {
            AppContext.activity?.let { activity ->
                bind(activity as ComponentActivity)
            }
        }
    }

    single<UserPreferences> { UserPreferencesImpl(AppContext.context) }

    single { (activity: Activity) ->
        PhoneAuthHelper(activity, 60)
    }
}

