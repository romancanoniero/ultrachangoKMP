package com.iyr.ultrachango.ui.rootnavigation

import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.ui.screens.qrscanner.QRTypes
import com.iyr.ultrachango.utils.auth_by_cursor.models.AppUser
import com.iyr.ultrachango.utils.expect.URLEncoder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


sealed class RootRoutes(val route: String) {


    object SharingRoute : RootRoutes("sharing/{qrType}/{refererId}") {
        fun createRoute(
            qrType: QRTypes = QRTypes.USER,
            refererId: String
        ): String {
            return "sharing/${qrType.name}/$refererId"
        }
    }


    object LandingRoute : RootRoutes("landing")


    object LoginRoute : RootRoutes("login")

    object ForgotPasswordRoute : RootRoutes("forgot_password")

    object RegisterRoute : RootRoutes("register")

//    object SetupProfileRoute : RootRoutes("setup_profile")


    object SetupProfileRoute : RootRoutes("setup_profile/{userAsJson}") {
        fun createRoute(user: AppUser? = null): String {
            val userAsJson = Json.encodeToString(user)
            val encoded = URLEncoder.encode(userAsJson)
            return "setup_profile/$encoded"
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
        fun createRoute(
            userKey: String? = null,
            shoppingListId: Int? = null,
            listName: String? = null
        ): String {
            return "shoppinglistedit/$userKey/$shoppingListId/$listName"
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

    object QRScannerScreenRoute : RootRoutes("shopping_list_membersqr_scanner")


    object FidelizationRoute : RootRoutes("fidelization")


}

