package com.iyr.ultrachango

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import com.iyr.ultrachango.domain.Language
import com.iyr.ultrachango.domain.Localization
import com.iyr.ultrachango.domain.auth.AuthRepository
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.rootnavigation.RootNavGraph
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.ui.screens.navigation.bottombar.BottomNavigationBar
import com.iyr.ultrachango.ui.screens.navigation.bottombar.NavigationItem
import com.iyr.ultrachango.ui.screens.navigation.navigationItemsLists
import com.iyr.ultrachango.ui.screens.qrscanner.QRTypes
import com.iyr.ultrachango.ui.screens.shoppingcart.ShoppingCartMaintenanceViewModel
import com.iyr.ultrachango.ui.screens.topbars.HomeTopAppBar
import com.iyr.ultrachango.ui.screens.topbars.ScreenTopAppBar
import com.iyr.ultrachango.domain.auth.models.AppUser
import com.iyr.ultrachango.presentation.auth.AuthViewModel
import com.iyr.ultrachango.utils.sound.AudioPlayer
import com.iyr.ultrachango.utils.ui.LoadingDialog
import com.iyr.ultrachango.utils.ui.capitalizeFirstLetter
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.jordond.compass.Location
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.shopping
import ultrachango2.composeapp.generated.resources.shopping_cart

@Composable
fun App(
    authRepository: AuthRepository = koinInject(),
    authViewModel: AuthViewModel = koinInject(),
) {
    // MaterialTheme {
    // FirebaseInit().initialize()

    val navController = rememberNavController()
    val settings = Settings()
    val localization = koinInject<Localization>()
  /*
    val languageIso by rememberStringSetting(
        key = "savedLanguageIso",
        defaultValue = Language.SPANISH
    ) {
        localization.applyLanguage(it)
    }

    val selectedLanguage by derivedStateOf {
        Language.entries.first { it.iso == languageIso }
    }
*/

    ImageLoader.Builder(LocalPlatformContext.current).memoryCachePolicy(CachePolicy.ENABLED)


    /*
        var serverId = "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = serverId
            )
        )
    */

    var loginStatusChecked by remember { mutableStateOf<Boolean?>(null) }






    if (loginStatusChecked == null) {
        LoadingDialog()
    }


    var user: AppUser? = null
    LaunchedEffect(Unit) {
        println("Reviso el Login")
        authRepository.signOut()
        if (authRepository.isUserSignedIn()) {
            println("Usuario Logueado")
            val authToken = authRepository.getAuthToken(forceRefresh = true)
            settings.setAuthToken(authToken!!)
            val it = authRepository.getCurrentUser()
            if (it == null) {
                authViewModel.signOut()
                //                    authRepository.logout()
            } else {
                user = it
            }
            loginStatusChecked = true
        } else {
            println("No hay Usuario Logueado")
            loginStatusChecked = true
        }
    }
    loginStatusChecked?.let {
        if (it) {
            NavHostMain(
                navController = navController,
                onNavigate = { rootName ->
                    navController.navigate(rootName)
                }
            )
        }
    } ?: run {
        LoadingDialog()
    }
}

@Composable
fun NavHostMain(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta el tema del sistema
    authRepository: AuthRepository = koinInject(),
    authViewModel: AuthViewModel = koinInject(),
    userViewModel: UserViewModel = koinInject(),
    navController: NavHostController = rememberNavController(),
    onNavigate: (rootName: String) -> Unit,
) {

    println("NavhostMain")

    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController: PermissionsController =
        remember(factory) { factory.createPermissionsController() }


    val LocalUserViewModel = staticCompositionLocalOf<UserViewModel> {
        error("No UserViewModel provided")
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }

// Proveer el ViewModel a la jerarquía de Composables
    CompositionLocalProvider(LocalUserViewModel provides userViewModel) {
        val isBottomBarVisible by remember {
            derivedStateOf {
                true
            }
        }
        val LightColors = lightColorScheme(
            primary = Color(0xFF6200EE),
            background = Color.LightGray.copy(alpha = 0.4f),
            surface = Color(0xFFF5F5F5),
            onPrimary = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black
        )

        val DarkColors = darkColorScheme(
            primary = Color(0xFFBB86FC),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )

        val colors = if (darkTheme) DarkColors else LightColors

        MaterialTheme(
            colorScheme = colors,
        ) {

            val backgroundColor = Color(0xFF121212)
            val statusBarValues = WindowInsets.safeDrawing.asPaddingValues()
            val scope = rememberCoroutineScope()

            val scaffoldVM: ScaffoldViewModel = koinInject<ScaffoldViewModel>()

            KoinContext {


                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0), // Esto es clave

                    topBar = {

                        DynamicTopBar(
                            authRepository,
                            authViewModel,
                            currentRoute,
                            navController
                        )


                    },
                    bottomBar = {
                        if (isBottomBarVisible) {
                            DynamicBottomBar(currentRoute, navController)
                        }
                    }) { innerPadding ->


                    RootNavGraph(
                        modifier = Modifier,
                        innerPadding,
                        navController,
                        permissionsController,
                        scaffoldVM,

                        authRepository
                    )
                }
            }
        }
//        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopBar(
    authRepository: AuthRepository,
    authViewModel: AuthViewModel,
    currentRoute: String?,
    navController: NavController,

    ) {
    val shareButton = {
        navController.navigate(
            RootRoutes.SharingRoute.createRoute(
                QRTypes.USER,
                authRepository.getUserKey()!!
            )
        )
    }



    when (currentRoute?.substringBefore("/")) {
        "home" -> {
            var me: AppUser? = null
            var name: String? = null
            me = authRepository.getCurrentUser()
            name = (me?.displayName?.capitalizeFirstLetter()
                ?: me?.firstName.toString()).capitalizeFirstLetter()
            HomeTopAppBar(me?.uid!!, name ?: "??????", me?.profilePicturePath, shareButton)

        }

        RootRoutes.MembersRoute.route,
        AppRoutes.SettingRoute.route,
        RootRoutes.ProductPricesDetailRoute.route.substringBefore("/"),
        RootRoutes.ProductsSuggestedByTextDetailRoute.route.substringBefore("/"),
        RootRoutes.SharingRoute.route.substringBefore("/"),
        RootRoutes.BuyRoute.route,
        RootRoutes.ShoppingListRoute.route,
        RootRoutes.ShoppingListEditRoute.route.substringBefore("/"),
        RootRoutes.PreparationRoute.route,
        RootRoutes.PreparationDetailRoute.route.substringBefore("/"),
        RootRoutes.LocationRoute.route,
        RootRoutes.SettingDetail.route,
        RootRoutes.FidelizationRoute.route,
        RootRoutes.QRScannerScreenRoute.route.substringBefore("/")
            -> {

            when (currentRoute?.substringBefore("/")) {

                RootRoutes.ShoppingListRoute.route -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛒 Listas de Compras",

                        )
                }

                RootRoutes.PreparationRoute.route -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛒 Preparacion",

                        )
                }


                RootRoutes.SharingRoute.route.substringBefore("/") -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "🔊 Invitar amigos ",
                        //stringResource(Res.string.invite)
                    )
                }


                RootRoutes.ShoppingListEditRoute.route.substringBefore("/") -> {
                    val listName =
                        navController.currentBackStackEntry?.arguments?.getString("listName")
                            ?: "Lista de Compras"

                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛒 " + listName,

                        )
                }

                RootRoutes.ProductPricesDetailRoute.route.substringBefore("/") -> {
                    val userKey =
                        navController.currentBackStackEntry?.arguments?.getString("user_key")
                            ?: "Producto"

                    val ean =
                        navController.currentBackStackEntry?.arguments?.getString("ean")
                            ?: "Producto"

                    val name =
                        navController.currentBackStackEntry?.arguments?.getString("name")
                            ?: "Producto"

                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛍️ " + name,

                        )
                }

                RootRoutes.ProductsSuggestedByTextDetailRoute.route.substringBefore("/") -> {
                    val userKey =
                        navController.currentBackStackEntry?.arguments?.getString("user_key")
                            ?: "Producto"

                    val ean =
                        navController.currentBackStackEntry?.arguments?.getString("ean")
                            ?: "Producto"

                    val name =
                        navController.currentBackStackEntry?.arguments?.getString("name")
                            ?: "Producto"

                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛍️ " + name,

                        )
                }


                RootRoutes.PreparationDetailRoute.route.substringBefore("/") -> {
                    // Lógica que se ejecuta cuando el icono es presionado
                    val shoppingCartMaintenanceViewModel: ShoppingCartMaintenanceViewModel =
                        koinInject()
                    val actionEmptyTrash = Pair(
                        Icons.Default.Delete
                    ) {
                        shoppingCartMaintenanceViewModel.deleteAllItems()
                    }

                    val actions = listOf(actionEmptyTrash)


                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛒 " + stringResource(Res.string.shopping_cart),
                        actionIcons = actions,
                    )
                }


                RootRoutes.BuyRoute.route -> {
                    // Lógica que se ejecuta cuando el icono es presionado
                    val shoppingCartMaintenanceViewModel: ShoppingCartMaintenanceViewModel =
                        koinInject()
                    val actionEmptyTrash = Pair(
                        Icons.Default.Delete
                    ) {
                        shoppingCartMaintenanceViewModel.deleteAllItems()
                    }

                    val actions = listOf(actionEmptyTrash)


                    ScreenTopAppBar(
                        navController = navController,
                        title = "🛒 " + stringResource(Res.string.shopping),
                        actionIcons = actions,
                    )
                }


                RootRoutes.LocationRoute.route -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "📍 Ubicación",

                        )
                }

                RootRoutes.SettingDetail.route -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "⚙️ Configuración",

                        )
                }

                RootRoutes.SettingRoute.route -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "⚙️ Configuración",

                        )
                }

                RootRoutes.MembersRoute.route -> {
                    val actionScanQR = Pair(
                        Icons.Default.QrCodeScanner,
                        {
                            navController.navigate(RootRoutes.QRScannerScreenRoute.route)
                        })

                    val actions = listOf(actionScanQR)

                    ScreenTopAppBar(
                        navController = navController,
                        title = "👥 Grupo Familiar",
                        actionIcons = actions,

                        )
                }

                RootRoutes.FidelizationRoute.route -> {


                    ScreenTopAppBar(
                        navController = navController,
                        title = "🎁 Fidelizacion",

                        )
                }

                RootRoutes.QRScannerScreenRoute.route.substringBefore("/") -> {
                    ScreenTopAppBar(
                        modifier = Modifier.background(Color.Transparent),
                        navController = navController, title = ""
                    )
                }

                else -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = "🏠 Inicio",

                        )
                }

            }


//     ScreenTopAppBar(title = )
        }

        "profile", "" -> TopAppBar(title = { Text("👤 Perfil") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        })


    }
}


@Composable
fun DynamicBottomBar(currentRoute: String?, navController: NavController) {
    val screensWithBottomBar = listOf(
        RootRoutes.SharingRoute.route.substringBefore("/"),
        RootRoutes.HomeRoute.route,
        RootRoutes.ShoppingListRoute.route,
        RootRoutes.ShoppingListRoute.route,
        RootRoutes.ProductPricesDetailRoute.route.substringBefore("/"),
        RootRoutes.ProductsSuggestedByTextDetailRoute.route.substringBefore("/"),
        RootRoutes.ShoppingListEditRoute.route.substringBefore("/"),
        RootRoutes.PreparationRoute.route.substringBefore("/"),
        RootRoutes.PreparationDetailRoute.route.substringBefore("/"),
        RootRoutes.LocationRoute.route,
        RootRoutes.SettingDetail.route,
        RootRoutes.BuyRoute.route
    )
    if (currentRoute?.substringBefore("/") in screensWithBottomBar) {

        BottomNavigationBar(
            items = navigationItemsLists,
            onItemClick = { item -> onItemClick(navController, item) },
            currentRoute = currentRoute
        )
    }
}

fun onItemClick(navController: NavController, currentNavigationItem: NavigationItem) {
    navController.navigate(currentNavigationItem.route) {
/*
popUpTo(navController.graph.startDestinationRoute ?: "") {
 saveState = true
}*/
        launchSingleTop = true
        restoreState = true
    }
}

fun validateForm(
    validateImage: Boolean = true,
    imageProfile: String? = null,

    firstName: String?,
    lastName: String?,
    gender: String?,
    birthDate: String?
): Boolean {
    return (!validateImage || !imageProfile.isNullOrBlank())

            && !firstName.isNullOrBlank() && !lastName.isNullOrBlank() && gender != null && !birthDate.isNullOrEmpty()
}

@Composable
fun getCurrentLocation(
    onLocationObtained: (Location?) -> Unit, onError: (String) -> Unit
) {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController: PermissionsController =
        remember(factory) { factory.createPermissionsController() }


    val geolocator: Geolocator = Geolocator.mobile()
    val scope = rememberCoroutineScope()

    scope.launch {
        when (val result: GeolocatorResult = geolocator.current()) {
            is GeolocatorResult.Success -> {
                onLocationObtained(result.data)
            }

            is GeolocatorResult.Error -> when (result) {
                is GeolocatorResult.NotSupported -> TODO()
                is GeolocatorResult.NotFound -> {
                    var pp = 6
                }

                is GeolocatorResult.PermissionError -> TODO()
                is GeolocatorResult.GeolocationFailed -> TODO()
                is GeolocatorResult.Error -> TODO()
                is GeolocatorResult.Success -> TODO()
            }
        }
    }
}

fun beep() {
    AudioPlayer.getInstance()
        .playSound(0) // Assuming 0 is the id for "files/scanner.mp3"
    triggerHapticFeedback()
}


fun Settings.getUserLocally(): AppUser? {
    return try {
        Json.decodeFromString(this.getStringOrNull("user").toString())
    } catch (ex: Exception) {
        null
    }
}


fun Settings.storeUserLocally(user: AppUser) {
    var EntityAsJson = Json.encodeToString(user)
    this.set("user", EntityAsJson)
}

fun Settings.getAuthToken(): String {
    return this.getStringOrNull("auth_token").toString()
}

fun Settings.setAuthToken(token: String) {
    return this.putString("auth_token", token)
}