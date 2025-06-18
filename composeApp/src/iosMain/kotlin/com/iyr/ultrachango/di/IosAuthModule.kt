package com.iyr.ultrachango.di
import com.iyr.ultrachango.domain.Localization
import org.koin.dsl.module

actual fun platformAuthModule() = module {

    single<Localization> { Localization() }

    /*
        single {
            val clientId = get<String>(qualifier = named("clientId"))
            GoogleSignInAuth.create(clientId = clientId)
        }

     */
}