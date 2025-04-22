package com.iyr.ultrachango

import androidx.activity.ComponentActivity
import dev.icerock.moko.permissions.PermissionsController
import org.koin.dsl.module

actual val nativeModule = module {

   // single { PhoneVerificationViewModel() }
      single<PermissionsController> {
         PermissionsController(AppContext.context).apply {
            AppContext.activity?.let { activity ->
               bind(activity as ComponentActivity)
            }
         }
      }


}

/*
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}*/