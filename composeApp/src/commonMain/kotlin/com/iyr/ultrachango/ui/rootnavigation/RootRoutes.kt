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

    object OtpVerificationRoute : RootRoutes("otp_verification/{verificationId}/{phoneNumber}") {
        fun createRoute(verificationId: String, phoneNumber: String): String {
            val encodedPhone = URLEncoder.encode(phoneNumber)
            return "otp_verification/$verificationId/$encodedPhone"
        }
    }


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


    object PreparationRoute : RootRoutes("preparation")

    object BuyRoute : RootRoutes("buy")



    object PreparationDetailRoute : RootRoutes("preparationdetail")
   /*
    {
        fun createRoute(
            userKey: String? = null,
            preparationListId: Int? = null,
            preparationListName: String? = null
        ): String {
            val preparationName = '"'+preparationListName.toString()+'"'

            return "preparationdetail/$userKey/$preparationListId/$preparationName"
        }
    }

    */


    object IntroAnimationRoute : RootRoutes("introanimation")

    object ShoppingListRoute : RootRoutes("shoppinglist")

    object ShoppingListAddRoute : RootRoutes("shoppinglistadd")

    object ShoppingListEditRoute : RootRoutes("shoppinglistedit/{userKey?}/{listId?}/{listName?}") {
        fun createRoute(
            userKey: String? = null,
            shoppingListId: Int? = null,
            listName: String? = null
        ): String {
            return "shoppinglistedit/$userKey/$shoppingListId/$listName"
        }
    }


    object ProductPricesDetailRoute : RootRoutes("productpricesdetail/{userKey?}/{ean?}/{name?}/{productAsJson?}") {
        fun createRoute(
            entityId:Int? = null,
            userKey: String,
            ean: String,
            name: String,
            productAsJson: String
        ): String {
            return "productpricesdetail/$entityId/$userKey/$ean/$name/$productAsJson"
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

