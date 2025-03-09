package com.iyr.ultrachango.di

import org.koin.dsl.module

actual fun platformAuthModule() = module {
    single {
        val clientId = get<String>(qualifier = named("clientId"))
        GoogleSignInAuth.create(clientId = clientId)
    }
}