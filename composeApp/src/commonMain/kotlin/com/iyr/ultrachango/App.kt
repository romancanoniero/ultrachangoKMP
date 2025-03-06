package com.iyr.ultrachango

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.iyr.ultrachango.auth.AuthManager
import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.auth.AuthenticatedUser
import com.iyr.ultrachango.auth.IFirebaseAuthRepository
import com.iyr.ultrachango.utils.firebase.FirebaseAuthRepository

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.preferences.managers.Persistence.Companion.KEY_USER_ID
import com.iyr.ultrachango.preferences.managers.Persistence.Companion.KEY_USER_NAME
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.rootnavigation.RootNavGraph
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.ui.screens.navigation.bottombar.BottomNavigationBar
import com.iyr.ultrachango.ui.screens.navigation.bottombar.NavigationItem
import com.iyr.ultrachango.ui.screens.navigation.navigationItemsLists
import com.iyr.ultrachango.ui.screens.qrscanner.QRTypes
import com.iyr.ultrachango.ui.screens.topbars.HomeTopAppBar
import com.iyr.ultrachango.ui.screens.topbars.ScreenTopAppBar
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
import ultrachango2.composeapp.generated.resources.invite

@Composable
fun App(
    authRepository: AuthRepository = koinInject(),
    firebaseRepository: FirebaseAuthRepository = FirebaseAuthRepository()
    // database: UltraChangoDatabase? = null
) {
    // MaterialTheme {

println("Voy a llamarlo")
    val authManager = AuthManager(IFirebaseAuthRepository())
    println("Voy a llamarlo 1")
    authManager.iniciarSesion("dfdfdfd","fdfdfd")


    val navController = rememberNavController()
    val settings = Settings()

    settings[KEY_USER_NAME] = "USERNAME"
    settings[KEY_USER_ID] = "XXXX"

    ImageLoader.Builder(LocalPlatformContext.current).memoryCachePolicy(CachePolicy.ENABLED)

    /*aca
        var serverId = "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = serverId
            )
        )
    */


    // val authRepository: AuthRepositoryImpl = koinInject()
    var loginStatusChecked by remember { mutableStateOf<Boolean?>(null) }


    if (loginStatusChecked == null) {
        LoadingDialog()
    }

    var user: AuthenticatedUser? = null
    LaunchedEffect(Unit) {

        println("Reviso el Login")
        if (authRepository.isLoggedIn) {
            val authToken = authRepository.getAuthToken(refresh = true)
            settings.setAuthToken(authToken!!)
            authRepository.fetchCurrentUser(forceRefresh = true) {
                if (it == null) {
                    authRepository.logout()
                } else {
                    user = it
                }
                loginStatusChecked = true
            }
        } else {
            loginStatusChecked = true
        }

    }



    loginStatusChecked?.let {
        if (it) {
            NavHostMain(navController = navController, onNavigate = { rootName ->
                navController.navigate(rootName)
            })
        }
    }?:
    run {

        println("Putazo muestro")

        LoadingDialog()
    }
}

@Composable
fun NavHostMain(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta el tema del sistema
    authRepository: AuthRepository = koinInject(),

    userViewModel: UserViewModel = koinInject(),
    navController: NavHostController = rememberNavController(),
    onNavigate: (rootName: String) -> Unit,
) {


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

                Box(
                    modifier = Modifier
                    //   .background(backgroundColor)
                )
                {
                    Scaffold(modifier = Modifier
                        // .padding(statusBarValues.calculateTopPadding())
                        .padding(0.dp),
                        topBar = {
                            DynamicTopBar(authRepository, currentRoute, navController)
                        },
                        bottomBar = {
                            if (isBottomBarVisible) {
                                DynamicBottomBar(currentRoute, navController)
                            }
                        }) { innerPadding ->


                        RootNavGraph(
                            modifier = Modifier
                                .padding(horizontal = 0.dp),
                            innerPadding,
                            navController,
                            permissionsController,
                            scaffoldVM,

                            authRepository
                        )
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopBar(
    authRepository: AuthRepository,
    currentRoute: String?,
    navController: NavController
) {
    val shareButton = {
        navController.navigate(
            RootRoutes.SharingRoute.createRoute(
                QRTypes.USER,
                authRepository.currentUserId
            )
        )
    }


    when (currentRoute?.substringBefore("/")) {
        "home" -> {
            val me = authRepository.getCurrentUser()!!
            val name = (me.firstName?.capitalizeFirstLetter() ?: " ????? ").split(" ")[0]

            HomeTopAppBar(me.uid!!, name, me.photoUrl, shareButton)
        }

        RootRoutes.MembersRoute.route,
        AppRoutes.SettingRoute.route,
        RootRoutes.SharingRoute.route,
        RootRoutes.ShoppingListRoute.route,
        RootRoutes.ShoppingListEditRoute.route.substringBefore("/"),
        RootRoutes.LocationRoute.route,
        RootRoutes.SettingDetail.route,
        RootRoutes.QRScannerScreenRoute.route.substringBefore("/")
            -> {

            when (currentRoute?.substringBefore("/")) {
                RootRoutes.ShoppingListRoute.route -> {
                    ScreenTopAppBar(navController = navController, title = "🛒 Listas de Compras")
                }

                RootRoutes.SharingRoute.route -> {
                    ScreenTopAppBar(
                        navController = navController,
                        title = stringResource(Res.string.invite)
                    )
                }


                RootRoutes.ShoppingListEditRoute.route.substringBefore("/") -> {
                    val listName =
                        navController.currentBackStackEntry?.arguments?.getString("listName")
                            ?: "Lista de Compras"

                    ScreenTopAppBar(navController = navController, title = "🛒 " + listName)
                }


                RootRoutes.LocationRoute.route -> {
                    ScreenTopAppBar(navController = navController, title = "📍 Ubicación")
                }

                RootRoutes.SettingDetail.route -> {
                    ScreenTopAppBar(navController = navController, title = "⚙️ Configuración")
                }

                RootRoutes.SettingRoute.route -> {
                    ScreenTopAppBar(navController = navController, title = "⚙️ Configuración")
                }

                RootRoutes.MembersRoute.route -> {
                    val actionScanQR = Pair(
                        Icons.Default.QrCodeScanner,
                        {
                            navController.navigate(RootRoutes.QRScannerScreenRoute.route)
                        })

                    val actions = listOf(actionScanQR)

                    ScreenTopAppBar(
                        modifier = Modifier,
                        navController = navController,
                        title = "👥 Grupo Familiar",
                        actions
                    )
                }

                RootRoutes.QRScannerScreenRoute.route.substringBefore("/") -> {
                    ScreenTopAppBar(
                        modifier = Modifier.background(Color.Transparent),
                        navController = navController, title = ""
                    )
                }

                else -> {
                    ScreenTopAppBar(navController = navController, title = "🏠 Inicio")
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
        RootRoutes.SharingRoute.route,
        RootRoutes.HomeRoute.route,
        RootRoutes.ShoppingListRoute.route,
        RootRoutes.ShoppingListEditRoute.route.substringBefore("/"),
        RootRoutes.LocationRoute.route,
        RootRoutes.SettingDetail.route
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
    imageProfile: ByteArray? = null,
    firstName: String?,
    lastName: String?,
    gender: Int?,
    birthDate: String?
): Boolean {
    return (!validateImage || imageProfile != null) && !firstName.isNullOrBlank() && !lastName.isNullOrBlank() && gender != null && !birthDate.isNullOrEmpty()
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


fun Settings.getUserLocally(): AuthenticatedUser {
    return Json.decodeFromString(this.getStringOrNull("user").toString())
}


fun Settings.storeUserLocally(user: AuthenticatedUser) {
    var EntityAsJson = Json.encodeToString(user)
    this.set("user", EntityAsJson)
}

fun Settings.getAuthToken(): String {
    return this.getStringOrNull("auth_token").toString()
}

fun Settings.setAuthToken(token: String) {
    return this.putString("auth_token", token)
}