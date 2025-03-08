package com.iyr.ultrachango


import AppContext
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.utils.firebase.GoogleAuth
import com.iyr.ultrachango.utils.firebase.provideGoogleAuth
import com.iyr.ultrachango.voice.handleVoiceCommand

class MainActivity : ComponentActivity() {

    private lateinit var googleAuth: GoogleAuth
    private lateinit var auth: FirebaseAuth

    /*
        val firebaseOptions = FirebaseOptions(
            applicationId = "1:1077576417175:android:fb6b276fc563beb4fe8ed2",
            apiKey = "AIzaSyDauycmAcLD-0Tbmo0_KgpDvlKKTV-k4sI",
            projectId = "ultrachango-23d46",
            storageBucket = "ultrachango-23d46.firebasestorage.app"

        )*/
    val firebaseOptions = FirebaseOptions.Builder().setProjectId("ultrachango-23d46")
        .setApplicationId("1:1077576417175:android:fb6b276fc563beb4fe8ed2")
        .setApiKey("AIzaSyDauycmAcLD-0Tbmo0_KgpDvlKKTV-k4sI")
        .setStorageBucket("ultrachango-23d46.firebasestorage.app")
        .build()


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContext.activity = this

        val firebaseApp = FirebaseApp.initializeApp(AppContext.context)
        AppContext.firebaseAuth = FirebaseAuth.getInstance(firebaseApp!!)

        /*
              Firebase.initialize(
                  AppContext.context,
                  options = firebaseOptions
              )


              auth = Firebase.auth

      */


        val signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                AppContext.handlerSignInResult.invoke(result.data)

                //      val culo = (googleAuth as? GoogleAuthAndroid)?.handleSignInResult(result.data)
            } else {
                Log.e("GoogleAuth", "Error en resultado de Sign-In")
            }
        }

        googleAuth = provideGoogleAuth(
            scope = lifecycleScope,
            activity = this,
            webClientId = "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com",
            signInLauncher = signInLauncher,
            onSuccess = {
                val pp = 3
            },
            onFailure = {
                val pp = 3
            }
        )

        AppContext.googleAuth = googleAuth



        registerAssistantShortcut(this)
        enableEdgeToEdge()
        installSplashScreen()

        /*aca

        */
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        val shortcut = ShortcutInfoCompat.Builder(this, "add_product")
            .setShortLabel("Agregar producto")
            .setIntent(
                Intent(Intent.ACTION_VIEW, Uri.parse("ultrachango://add_item"))
            )
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)


        setContent {


            EnableTransparentStatusBar()
            App()
        }
    }


    fun registerAssistantShortcut(context: Context) {
        val shortcut = ShortcutInfoCompat.Builder(context, "open_ultrachango")
            .setShortLabel("Abrir UltraChango")
            .setLongLabel("Abrir la aplicación UltraChango")
            .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("ultrachango://home")))
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val data = intent.data
        val name = data?.getQueryParameter("name")
        val list = data?.getQueryParameter("list")
        if (name != null && list != null) {
            handleVoiceCommand("Agrega $name a la lista $list")
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

fun verifyWebClientId() {
    println("WebClientId: ${BuildConfig.GOOGLE_WEB_CLIENT_ID}")
    // Verifica que termina en .apps.googleusercontent.com
    require(BuildConfig.GOOGLE_WEB_CLIENT_ID.endsWith(".apps.googleusercontent.com")) {
        "Invalid Web Client ID format"
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
    /*
        DateSelectionDialog(
            title = "Titulo",
            message = "Mensaje",
            acceptText = "Aceptar",
            cancelText = "Cancelar",
            onAccept = {},
            onDismiss = {}
        )
    */
    val user = User(
        uid = "3TydQxH8kSXSSjjbKhvWBtYtvhj2",
        nick = "1",
        firstName = "1",
        lastName = "1",
        fileName = "IMG_1739231148.jpg",
        email = "romuriopatigno@gmail.com",
        phoneNumber = "1",
        isAnonymous = true,
        birthDate = "1",
        gender = 1

    )
    /*
        UserWithHand(
            modifier = Modifier.size(100.dp),

            qtyRecord = ShoppingListQuantities(
                listId = 1,
                ean = "11111111",
                userId = "3TydQxH8kSXSSjjbKhvWBtYtvhj2",
                qty = 1.0,
                user = user

            ), onClick = {})
    */

    // ShareButton()

    // InviteScreen(InviteViewModel())

  //  LoginScreen()
}
