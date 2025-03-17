package com.iyr.ultrachango.di.permissions

import com.iyr.ultrachango.utils.permissions.PermissionsController
import org.koin.core.module.Module
import org.koin.dsl.module

actual val permissionsModule: Module = module {
    single<PermissionsController> { PermissionsController() }
}