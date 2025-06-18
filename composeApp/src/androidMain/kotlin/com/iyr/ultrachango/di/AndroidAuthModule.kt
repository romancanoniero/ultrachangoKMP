package com.iyr.ultrachango.di
// shared/androidMain/di/AndroidAuthModule.kt
import androidx.activity.ComponentActivity
import com.iyr.ultrachango.App
import com.iyr.ultrachango.domain.Localization
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformAuthModule() = module {

   single<Localization> { Localization(AppContext.context) }
    /*
    single {
        // Obtenemos la Activity del contexto de Koin
        val activity = get<ComponentActivity>()
        val webClientId = get<String>(qualifier = named("webClientId"))

        GoogleSignInAuth(activity = activity,
            clientId = webClientId)

    }
    */
}