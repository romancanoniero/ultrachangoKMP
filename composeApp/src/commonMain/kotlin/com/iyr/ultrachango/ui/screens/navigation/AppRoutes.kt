package com.iyr.ultrachango.ui.screens.navigation

import com.iyr.ultrachango.data.models.ShoppingList


sealed class AppRoutes(val route: String) {


    object HomeRoute : AppRoutes("home")

    object BarcodeScanner : AppRoutes("barcodeScanner/{onResult}")


    object SettingRoute : AppRoutes("settings")


    object ShoppingListRoute : AppRoutes("shoppinglist")

    object ShoppingListAddRoute : AppRoutes("shoppinglistadd")

    object ShoppingListEditRoute : AppRoutes("shoppinglistedit/{userKey?}/{listId?}") {
        fun createRoute(userKey: String? = null, shoppingListId: Int? = null): String {
            return "shoppinglistedit/$userKey/$shoppingListId"
        }
    }

    object ShoppingListDetailRoute : AppRoutes("shoppinglistdetail/{listId?}") {
        fun createRoute(shoppingList: ShoppingList? = null): String {
            val shoppingListId = shoppingList?.id ?: ""
            return "shoppinglistdetail/$shoppingListId"
        }
    }


    object LocationRoute : AppRoutes("location")
    object LocationsetailsRoute : AppRoutes("locationsdetails")


    object SettingDetail : AppRoutes("settingdetails")


    object ProvidersList : AppRoutes("providersList/{specialty}") {
        fun createRoute(specialty: String) = "providersList/$specialty"
    }

    object ServiceDetail : AppRoutes("serviceDetails/{providerKey}") {
        fun createRoute(providerKey: String) = "serviceDetails/$providerKey"
    }

    object MembersRoute : AppRoutes("members")

    object ShoppingListMembersRoute : AppRoutes("shopping_list_members/{listId}") {
        fun createRoute(listId: Int) = "shopping_list_members/$listId"
    }


    object FidelizationRoute : AppRoutes("fidelization")


}

fun getTitleForRoute(route: String): String {

    val module = route.substringBefore("/")
    return when (module) {
        "home" -> "UltraChango"
        "shoppinglist" -> "Listas"
        "shoppinglistaddedit" -> "Lista de Compras"
        "location" -> "Ubicaciones"
        "members" -> "Colaboradores"
        "fidelization" -> "Beneficios"
        "settings" -> "Configuración"
        else -> ""
    }
}