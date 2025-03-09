package com.iyr.ultrachango.di
// shared/androidMain/di/AndroidAuthModule.kt
import androidx.activity.ComponentActivity
import com.iyr.ultrachango.utils.auth_by_cursor.auth.GoogleSignInAuth
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformAuthModule() = module {
    single {
        // Obtenemos la Activity del contexto de Koin
        val activity = get<ComponentActivity>()
        val webClientId = get<String>(qualifier = named("webClientId"))

        GoogleSignInAuth(activity = activity,
            clientId = webClientId)

    }
}