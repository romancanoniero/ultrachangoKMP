package com.iyr.ultrachango

import org.koin.dsl.module

actual val nativeModule = module {
  //  single { getDatabaseBuilder(get()).build().productsDao() }


}

/*
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}*/