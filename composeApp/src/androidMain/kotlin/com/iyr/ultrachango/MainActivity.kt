package com.iyr.ultrachango

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.iyr.ultrachango.ui.dialogs.DateSelectionDialog
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    //    enableEdgeToEdge()
        installSplashScreen()


        val firebaseOptions = FirebaseOptions(
            applicationId = "1:1077576417175:android:fb6b276fc563beb4fe8ed2",
            apiKey = "AIzaSyDauycmAcLD-0Tbmo0_KgpDvlKKTV-k4sI",
            projectId = "ultrachango-23d46",
            storageBucket = "ultrachango-23d46.firebasestorage.app"

        )

        Firebase.initialize(
            applicationContext,
            options = firebaseOptions
        )


        setContent {
            EnableTransparentStatusBar()
            App()
        }
    }
}

@Composable
private fun EnableTransparentStatusBar() {
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    //  FidelizationScreen(NavController(LocalView.current.context))


    // App()
    /*
        RenameDialog(
            ShoppingListModel(
                userId = "1",
                nombre = "dddd",
                imageUrl = ""
            ), {},
            onDismiss = {}
        )

     */

    //  ShoppingListScreen(NavHostController(LocalView.current.context))
    /*
        ConfirmationDialog(
            onAccept = {},
            onDismiss = {},
            title = "Eliminar Lista",
            message = "Estas seguro que deseas eliminar la lista CUBA?",
            acceptText = "Eliminar",
            cancelText = "Cancelar"
        )
        */


    // InfoDialog("Titulo", "Mensaje", {})
    //  ShoppingListScreen(NavController(LocalView.current.context))
    //  ShoppingListAddEditScreen("1", 1, NavController(LocalView.current.context), ScaffoldViewModel(), {})
    /*
        PagerWithIndicator(modifier = androidx.compose.ui.Modifier,
            pages = listOf({
                Page1()},{
                Page2()},{
                Page3()},{
                Page4()}
            ),
            onLastPageButtonClick = {})
    */
    /*
     LandingScreen(
         NavController(LocalView.current.context), PermissionsController(LocalView.current.context))
  */
    /*
        LoginScreen(
            NavHostController(LocalView.current.context),
            PermissionsController(LocalView.current.context),
            LoginViewModel(ScaffoldViewModel()),
            UserViewModel()
        )
    */

    /*
    ForgotPasswordScreen(
        NavHostController(LocalView.current.context),
        PermissionsController(LocalView.current.context), UserViewModel()
    )
    */
    /*
        RegisterScreen(
            NavHostController(LocalView.current.context),
            PermissionsController(LocalView.current.context),
            RegisterViewModel(
                authService = get,
                ,ScaffoldViewModel()),

            UserViewModel()
        )
        */
    /*
        ProfileScreen(
            NavHostController(LocalView.current.context),

                )
        */

    DateSelectionDialog(
        title = "Titulo",
        message = "Mensaje",
        acceptText = "Aceptar",
        cancelText = "Cancelar",
        onAccept = {},
        onDismiss = {}
    )


}
