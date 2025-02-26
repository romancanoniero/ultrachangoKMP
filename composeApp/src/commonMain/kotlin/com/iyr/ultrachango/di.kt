package com.iyr.ultrachango


import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.data.api.cloud.auth.CloudAuthService
import com.iyr.ultrachango.data.api.cloud.familymembers.CloudFamilyMembersService
import com.iyr.ultrachango.data.api.cloud.images.CloudImagesService
import com.iyr.ultrachango.data.api.cloud.location.CloudLocationsService
import com.iyr.ultrachango.data.api.cloud.products.CloudProductsService
import com.iyr.ultrachango.data.api.cloud.shoppinglist.CloudShoppingListService
import com.iyr.ultrachango.data.api.cloud.users.CloudUsersService
import com.iyr.ultrachango.data.api.preciosclaros.PreciosClarosService
import com.iyr.ultrachango.data.database.repositories.FamilyMembersRepository
import com.iyr.ultrachango.data.database.repositories.ImagesRepository
import com.iyr.ultrachango.data.database.repositories.ProductsRepository
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.database.repositories.StoresRepository
import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.database.repositories.UserRepositoryImpl
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.auth.config.profile.RegistrationProfileViewModel
import com.iyr.ultrachango.ui.screens.auth.login.LoginViewModel
import com.iyr.ultrachango.ui.screens.auth.registration.RegisterViewModel
import com.iyr.ultrachango.ui.screens.home.HomeScreenViewModel
import com.iyr.ultrachango.ui.screens.locations.main.LocationsDetailsViewModel
import com.iyr.ultrachango.ui.screens.locations.main.LocationsViewModel
import com.iyr.ultrachango.ui.screens.member.MembersScreenViewModel
import com.iyr.ultrachango.ui.screens.setting.SettingScreen.SettingsScreenViewModel
import com.iyr.ultrachango.ui.screens.setting.profile.ProfileViewModel
import com.iyr.ultrachango.ui.screens.shoppinglist.edition.ShoppingListAddEditViewModel
import com.iyr.ultrachango.viewmodels.InviteViewModel
import com.iyr.ultrachango.ui.screens.shoppinglist.main.ShoppingListViewModel
import com.iyr.ultrachango.ui.screens.shoppinglist.members.ShoppingMembersSelectionViewModel
import com.iyr.ultrachango.utils.firebase.FirebaseAuthRepository
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.SearchWithScannerViewModel
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchService
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchViewModel
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.russhwolf.settings.Settings

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


val appModule = module {


    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    isLenient = true

                })
            }
        }
    }

    single {
        Settings()
    }

/*
    single<ProductsDao> {
        val dbBuilder = get<RoomDatabase.Builder<UltraChangoDatabase>>()
        dbBuilder.setDriver(BundledSQLiteDriver()).build().productsDao()
    }
*/
    single<CloudUsersService> {
        CloudUsersService(
            client = get(),
            settings = get(),
        )
    }

    single<CloudAuthService> {
        CloudAuthService(
            client = get(),
            settings = get(),
        )
    }


    single<CloudImagesService> {
        CloudImagesService(
            client = get(),
            settings = get(),
        )
    }
}

val dataModule = module {


    factoryOf(::ImagesRepository)
    factoryOf(::FamilyMembersRepository)
    factoryOf(::StoresRepository)
    factoryOf(::ShoppingListRepository)


    //   factoryOf(::ProductsRepository)


    factory {
        ProductsRepository(
            authRepository = get(),
         //   productsDao = get(),
            preciosClarosService = get(),
            productsCloudService = get()
        )
    }


    factory {
        UserLocationsRepository(
            //locationsListDao = get(),
            locationsCloudService = get(),
        )
    }

    factory {
        UserRepositoryImpl(
            apiClient = get(),
            settings = get()
        )
    }


    //factoryOf(::PreciosClarosService)
    factory {
        PreciosClarosService(
            client = get()
        )
    }

    single<FirebaseAuthRepository> {
        FirebaseAuthRepository()
    }

    single<AuthRepository> {
        AuthRepository(
            firebaseAuthRepository = get(),
            settings = get(),
            apiClient = get(),
            apiAuth = get(),
            userRepository = get()
        )
    }
/*
    factory {
        AuthViewModel(
            authRepository = get()
        )
    }
*/

    factory {
        RegistrationProfileViewModel(
            authRepository = get(),
            scaffoldVM = get(),
            usersRepository = get(),
            imagesRepository = get()
        )
    }

    factoryOf(::InviteViewModel)

    factory {
        PlacesSearchViewModel(
            placesSearchService = get()
        )
    }

    factory {
        LocationsViewModel(
            scaffoldVM = get(),
            userViewModel = get(),
            placesSearchService = get(),
            locationRepository = get(),
            authRepository = get(),
        )
    }




    factoryOf(::PlacesSearchService)
    factoryOf(::CloudProductsService)
    factoryOf(::CloudLocationsService)
    factoryOf(::CloudFamilyMembersService)
    factoryOf(::CloudShoppingListService)
    // factoryOf(::CloudUsersService)

    factoryOf(::SearchWithScannerViewModel)



    factory {
        ProfileViewModel(
            authService = get(),
            scaffoldVM = get(),
            usersRepository = get(),
            imagesRepository = get()
        )
    }

}

val viewModelsModule = module {
//    viewModelOf(::ScaffoldViewModel)
    single { ScaffoldViewModel() }

    //  viewModelOf(::HomeScreenViewModel)


    viewModel {
        LoginViewModel(
            authRepository = get(),
            scaffoldVM = get()
        )
    }

    viewModel {
        RegisterViewModel(
            authService = get(),
            scaffoldVM = get()
        )
    }

    viewModel {
        HomeScreenViewModel(
            productsRepository = get(),
            shoppingListRepository = get(),
            userLocationsRepository = get(),
            userViewModel = get(),
            authRepositoryImpl = get(),
            scaffoldVM = get()
        )
    }


    //  viewModelOf(::ShoppingListViewModel)
    viewModel {
        ShoppingListViewModel(
            authRepository = get(),
            shoppingListRepository = get(),
            tiendasRepository = get(),
            userViewModel = get(),
            scaffoldVM = get()
        )
    }


    viewModel {
        ShoppingMembersSelectionViewModel(
            shoppingListRepository = get(),
            userViewModel = get(),
        )
    }

/*
    viewModel {
        ShoppingListAddEditViewModel(
            permissionsController = get(),
            userKey = get(),
            shoppingListId = get(),
            authRepository = get(),
            shoppingListRepository = get(),
            productsRepository = get(),
            userViewModel = get(),
            scaffoldVM = get()
        )
    }
    */
    viewModel {
        ShoppingListAddEditViewModel(
            userKey = get(),
            shoppingListId = get(),
            authRepository = get(),
            shoppingListRepository = get(),
            productsRepository = get(),
            userViewModel = get(),
            scaffoldVM = get()
        )
    }


    viewModel {
        MembersScreenViewModel(
            authRepository = get(),
            familyMembersRepository = get(),
        )
    }

    viewModel { LocationsDetailsViewModel(get()) }

    viewModel { SettingsScreenViewModel(get(), get()) }

    viewModel {
        UserViewModel(
            authRepository = get()
        )
    }
}

expect val nativeModule: Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, dataModule, viewModelsModule, nativeModule)
    }
}