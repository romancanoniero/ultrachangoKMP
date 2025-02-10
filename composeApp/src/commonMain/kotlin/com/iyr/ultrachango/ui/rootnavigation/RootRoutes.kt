package com.iyr.ultrachango.ui.rootnavigation

import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


sealed class RootRoutes(val route: String) {


    object LandingRoute : RootRoutes("landing")


    object LoginRoute : RootRoutes("login")

    object ForgotPasswordRoute : RootRoutes("forgot_password")

    object RegisterRoute : RootRoutes("register")

//    object SetupProfileRoute : RootRoutes("setup_profile")


    object SetupProfileRoute : RootRoutes("setup_profile/{userAsJson}") {
        fun createRoute(user: User? = null): String {
            val userAsJson = Json.encodeToString(user)
            return "setup_profile/$userAsJson"
        }
    }


    object LandScreen : RootRoutes("land")

    object MainScreenRoute : RootRoutes("mainscreen")

    object HomeRoute : RootRoutes("home")

    object BarcodeScanner : RootRoutes("barcodeScanner/{onResult}")


    object SettingRoute : RootRoutes("settings")


    object ShoppingListRoute : RootRoutes("shoppinglist")

    object ShoppingListAddRoute : RootRoutes("shoppinglistadd")

    object ShoppingListEditRoute : RootRoutes("shoppinglistedit/{userKey?}/{listId?}") {
        fun createRoute(userKey: String? = null, shoppingListId: Int? = null): String {
            return "shoppinglistedit/$userKey/$shoppingListId"
        }
    }

    object ShoppingListDetailRoute : RootRoutes("shoppinglistdetail/{listId?}") {
        fun createRoute(shoppingList: ShoppingList? = null): String {
            val shoppingListId = shoppingList?.id ?: ""
            return "shoppinglistdetail/$shoppingListId"
        }
    }


    object LocationRoute : RootRoutes("location")
    object LocationsetailsRoute : RootRoutes("locationsdetails")


    object SettingDetail : RootRoutes("settingdetails")

    object ProfileScreenRoute : RootRoutes("profileScreen")

    object ProvidersList : RootRoutes("providersList/{specialty}") {
        fun createRoute(specialty: String) = "providersList/$specialty"
    }

    object ServiceDetail : RootRoutes("serviceDetails/{providerKey}") {
        fun createRoute(providerKey: String) = "serviceDetails/$providerKey"
    }

    object MembersRoute : RootRoutes("members")

    object ShoppingListMembersRoute : RootRoutes("shopping_list_members/{listId}") {
        fun createRoute(listId: Int) = "shopping_list_members/$listId"
    }


    object FidelizationRoute : RootRoutes("fidelization")


}

