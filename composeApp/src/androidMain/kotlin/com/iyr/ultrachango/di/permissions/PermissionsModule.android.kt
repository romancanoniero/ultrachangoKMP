package com.iyr.ultrachango.di.permissions

import AppContext
import dev.icerock.moko.permissions.PermissionsController
import org.koin.core.module.Module
import org.koin.dsl.module

actual val permissionsModule: Module = module {
    single<PermissionsController> { PermissionsController(AppContext.context) }
}