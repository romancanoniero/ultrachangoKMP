package com.iyr.ultrachango

import com.iyr.ultrachango.auth.PhoneVerificationViewModel
import com.iyr.ultrachango.utils.firebase.FirebaseAuthRepository
import org.koin.dsl.module

actual val nativeModule = module {

   // single { PhoneVerificationViewModel() }
}

/*
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}*/