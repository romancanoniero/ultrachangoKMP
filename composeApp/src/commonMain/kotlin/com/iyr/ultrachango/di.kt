package com.iyr.ultrachango

import com.iyr.fbauthentication.auth.FirebaseAuthImpl
import com.iyr.ultrachango.data.api.cloud.auth.CloudAuthService
import com.iyr.ultrachango.data.api.cloud.buy.prepare.CloudShoppingCartService
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
import com.iyr.ultrachango.data.database.repositories.ShoppingCartRepository
import com.iyr.ultrachango.data.database.repositories.ShoppingListRepository
import com.iyr.ultrachango.data.database.repositories.StoresRepository
import com.iyr.ultrachango.data.database.repositories.UserLocationsRepository
import com.iyr.ultrachango.data.database.repositories.UserRepositoryImpl
import com.iyr.ultrachango.data.repository.AuthRepositoryImpl
import com.iyr.ultrachango.di.permissions.permissionsModule
import com.iyr.ultrachango.di.platformAuthModule
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.presentation.auth.AuthViewModel
import com.iyr.ultrachango.services.LocationService
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.auth.config.profile.RegistrationProfileViewModel
import com.iyr.ultrachango.ui.screens.auth.login.LoginViewModel
import com.iyr.ultrachango.ui.screens.auth.otp.OtpViewModel
import com.iyr.ultrachango.ui.screens.auth.registration.RegisterViewModel
import com.iyr.ultrachango.ui.screens.home.HomeScreenViewModel
import com.iyr.ultrachango.ui.screens.locations.main.LocationsDetailsViewModel
import com.iyr.ultrachango.ui.screens.locations.main.LocationsViewModel
import com.iyr.ultrachango.ui.screens.member.MembersScreenViewModel
import com.iyr.ultrachango.ui.screens.setting.SettingScreen.SettingsScreenViewModel
import com.iyr.ultrachango.ui.screens.setting.profile.ProfileViewModel
import com.iyr.ultrachango.ui.screens.shopping.ProductPriceDisplayViewModel
import com.iyr.ultrachango.ui.screens.shopping.ProductsSuggestionsByTextViewModel
import com.iyr.ultrachango.ui.screens.shoppingcart.ShoppingCartMaintenanceViewModel
import com.iyr.ultrachango.ui.screens.shoppingcart.ShoppingCartViewModel
import com.iyr.ultrachango.ui.screens.shoppinglist.edition.ShoppingListAddEditViewModel
import com.iyr.ultrachango.ui.screens.shoppinglist.main.ShoppingListViewModel
import com.iyr.ultrachango.ui.screens.shoppinglist.members.ShoppingMembersSelectionViewModel
import com.iyr.ultrachango.utils.firebase.FirebaseAuthRepository
import com.iyr.ultrachango.utils.ui.elements.searchwithscanner.SearchWithScannerViewModel
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchService
import com.iyr.ultrachango.utils.ui.places.borrar.PlacesSearchViewModel
import com.iyr.ultrachango.viewmodels.InviteViewModel
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.russhwolf.settings.Settings
import com.ultrachango2.features.location.presentation.LocationOptionsViewModel
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


val locationModule = module {
    single {
        Geolocator.mobile()
    }

    single {
        LocationService(
            permissionsController = get(),
            userLocationsRepository = get(),
            settings = Settings()
        )
    }

    viewModel {
        LocationOptionsViewModel(
            userLocationsRepository = get(),
            authRepository = get(),
            locationService = get(),
            permissionsController = get()
        )
    }
}

val baseModule = module {
    includes(locationModule)

    single {
        Settings()
    }

    single {
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000 // 60 segundos
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    isLenient = true
                })
            }
        }
    }
}

val configModule: Module = module {
    // Configuración de versiones
    /*
    single<String>(qualifier = named("google_web_client_id")) {
        BuildConfig.GOOGLE_WEB_CLIENT_ID

    }

    single<String>(qualifier = named("firebase_auth_version")) {
        BuildConfig.FIREBASE_AUTH_VERSION
    }

    single<String>(qualifier = named("google_sign_in_version")) {
        BuildConfig.GOOGLE_SIGN_IN_VERSION
    }

    single<String>(qualifier = named("facebook_sdk_version")) {
        BuildConfig.FACEBOOK_SDK_VERSION
    }

    single<String>(qualifier = named("phone_number_kit_version")) {
        BuildConfig.PHONE_NUMBER_KIT_VERSION
    }
*/
}

val authModule = module {

      includes(platformAuthModule())

//    single<FirebaseInit> { FirebaseInit() }

    single<CloudAuthService> {
        CloudAuthService(
            client = get(),
            settings = get(),
        )
    }

  //  single { FirebaseAuth() }


    single<FirebaseAuthRepository> {
        FirebaseAuthRepository()
    }


    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    //single<AuthRepositoryImpl> { AuthRepositoryImpl(get(), get(), get()) }


    single { AuthViewModel(get(), get()) }


}

val appModule = module {
    includes(locationModule)

    single<CloudUsersService> {
        CloudUsersService(
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



   single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single {
        FirebaseAuthImpl(
            platform = get(),
            phoneAuthHelper = get()
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

    factory<RegistrationProfileViewModel<AppUser?>> { (currentUser: AppUser?) ->

        RegistrationProfileViewModel(
            initialData = currentUser,
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



    factory {
        CloudShoppingCartService(
            client = get(),
        )
    }
    factoryOf(::PlacesSearchService)
    factoryOf(::CloudProductsService)
    factoryOf(::CloudLocationsService)
    factoryOf(::CloudFamilyMembersService)
    factoryOf(::CloudShoppingListService)
    factory {
        ShoppingCartRepository(
            authRepository = get(),
            shoppingCartCloudService = get(),
        )
    }

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

    single {
        ProductPriceDisplayViewModel(
            permissionsController = get(),
            authRepository = get(),
            shoppingListRepository = get(),
            shoppingCartRepository = get(),
            productsRepository = get()
        )
    }

    single {
        ProductsSuggestionsByTextViewModel(
            permissionsController = get(),
            authRepository = get(),
            shoppingListRepository = get(),
            shoppingCartRepository = get(),
            productsRepository = get()
        )
    }



    viewModel { OtpViewModel(get()) }

    single {
        LocationOptionsViewModel(
            userLocationsRepository = get(),
            authRepository = get(),
            locationService = get(),
            permissionsController = get()
        )
    }

    viewModel {
        LoginViewModel(
            authRepository = get(),
            scaffoldVM = get(),
            authViewModel = get(),
            firebaseAuthPlatform = get(),
        )
    }

    viewModel {
        RegisterViewModel(
            authService = get(),
            scaffoldVM = get(),
            authViewModel = get()
        )
    }

    viewModel {
        HomeScreenViewModel(
            productsRepository = get(),
            shoppingListRepository = get(),
            userLocationsRepository = get(),
            locationOptionsViewModel = get(),
            locationService = get(),
            userViewModel = get(),
            authRepository = get(),
            scaffoldVM = get(),
            permissionsController = get()
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
        ShoppingCartViewModel(
            authRepository = get(),
            shoppingListRepository = get(),
            shoppingCartRepository = get(),
            tiendasRepository = get(),
            userViewModel = get(),
            scaffoldVM = get()
        )
    }

    single {
        ShoppingCartMaintenanceViewModel(
            permissionsController = get(),
            authRepository = get(),
            productsRepository = get(),
            shoppingListRepository = get(),
            shoppingCartRepository = get(),
            //  userViewModel = get(),
            scaffoldVM = get()
        )
    }

    viewModel {
        ShoppingMembersSelectionViewModel(
            shoppingListRepository = get(),
            userViewModel = get(),
        )
    }

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
        modules(
            baseModule,
            permissionsModule,
            appModule,
            configModule,
            authModule,
            dataModule,
            viewModelsModule,
            nativeModule
        )
    }
}