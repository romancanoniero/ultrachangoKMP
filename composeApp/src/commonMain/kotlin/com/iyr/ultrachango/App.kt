package com.iyr.ultrachango

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.MaterialTheme
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext

import coil3.request.CachePolicy
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.auth.AuthViewModel

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.preferences.managers.Persistence.Companion.KEY_USER_ID
import com.iyr.ultrachango.preferences.managers.Persistence.Companion.KEY_USER_NAME
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.rootnavigation.RootNavGraph
import com.iyr.ultrachango.ui.screens.navigation.bottombar.NavigationItem
import com.iyr.ultrachango.utils.ui.LoadingDialog
import com.iyr.ultrachango.viewmodels.UserViewModel
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.jordond.compass.Location
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App(
   // database: UltraChangoDatabase? = null
) {
    // MaterialTheme {


    val navController = rememberNavController()
    val settings = Settings()

    settings[KEY_USER_NAME] = "USERNAME"
    settings[KEY_USER_ID] = "XXXX"

   ImageLoader.Builder(LocalPlatformContext.current).memoryCachePolicy(CachePolicy.ENABLED)

        //   LaunchedEffect(Unit) {
        var serverId = "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = serverId
            )
        )

//    }


    val authRepository: AuthRepositoryImpl = koinInject()
    var loginStatusChecked by remember { mutableStateOf<Boolean>(false) }


    if (!loginStatusChecked) {
        LoadingDialog()
    }

    var user: User? = null
    LaunchedEffect(Unit) {
        Firebase.auth.currentUser?.let {

            // User is signed in
            authRepository.fetchCurrentUser {
                user = it

            }
            loginStatusChecked = true
        } ?:
        run {
            // No user is signed in
            println("No user is signed in")
            loginStatusChecked = true
        }
    }

    /*
      Firebase.auth.authStateChanged.collect() { user ->
          if (user != null) {
              // User is signed in
              authRepository.fetchCurrentUser{
                  _user = it
              }

          } else {
              // No user is signed in
              println("No user is signed in")

          }
      }

       */

    loginStatusChecked?.let {
        if (loginStatusChecked) {
            NavHostMain(navController = navController, onNavigate = { rootName ->
                navController.navigate(rootName)
            })
        }
    }
}

@Composable
fun NavHostMain(
    authRepository: AuthRepositoryImpl = koinInject(),
    authViewModel: AuthViewModel = koinInject(),
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

        MaterialTheme {

            val statusBarValues = WindowInsets.safeDrawing.asPaddingValues()
            val scope = rememberCoroutineScope()
            val auth = remember { Firebase.auth }
            val firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }

            if (firebaseUser == null) {
                var pp = 3
            } else {
                var pp = 4
            }
            val scaffoldVM: ScaffoldViewModel = koinInject<ScaffoldViewModel>()

            KoinContext {

                RootNavGraph(
                    navController,
                    permissionsController,
                    scaffoldVM,
                    authViewModel,
                    authRepository
                )


            }
        }
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
    onLocationObtained: (Location?) -> Unit,
    onError: (String) -> Unit
) {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController: PermissionsController =
        remember(factory) { factory.createPermissionsController() }


    val geolocator: Geolocator = Geolocator.mobile()
    val scope = rememberCoroutineScope()

    scope.launch {
        val result: GeolocatorResult = geolocator.current()
        when (result) {
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

