package com.iyr.ultrachango.data.models.app

import com.iyr.ultrachango.Constants.SECTION_SHOPPING_LIST
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes


data class Section(
    val sectionKey: String,
    val title: String,
    val photo: String,
    val route: String?,
    val params: ArrayList<Any>,
    val data : Any? = null
)


val sections = listOf<Section>(
    Section(
        sectionKey = "by_market",
        title = "Carrefour",
        photo = "https://i0.wp.com/www.quelapaseslindo.com.ar/wp-content/uploads/que-significa-logo-carrefour.jpg?w=700&ssl=1",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Carrefour")
    ),
    Section(
        sectionKey = "by_market",
        title = "Walmart",
        photo = "https://1000marcas.net/wp-content/uploads/2020/02/Walmart-logo-1.jpg",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Walmart")
    ), Section(
        sectionKey = "by_market",
        title = "Dia",
        photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvZ6v9nc6Mjq9BR_QRdClqCuntOAgsCF69LA&s",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )
    , Section(
        sectionKey = SECTION_SHOPPING_LIST,
        title = "Mendoza",
        photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvZ6v9nc6Mjq9BR_QRdClqCuntOAgsCF69LA&s",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )
    , Section(
        sectionKey = SECTION_SHOPPING_LIST,
        title = "Mexico",
        photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvZ6v9nc6Mjq9BR_QRdClqCuntOAgsCF69LA&s",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )
    , Section(
        sectionKey = SECTION_SHOPPING_LIST,
        title = "Argerich",
        photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvZ6v9nc6Mjq9BR_QRdClqCuntOAgsCF69LA&s",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )
    , Section(
        sectionKey = SECTION_SHOPPING_LIST,
        title = "Beba",
        photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvZ6v9nc6Mjq9BR_QRdClqCuntOAgsCF69LA&s",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )
    , Section(
        sectionKey = "rewards_list",
        title = "Modo",
        photo = "https://assets.mobile.playdigital.com.ar/images/banks/modo.png",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )
    , Section(
        sectionKey = "rewards_list",
        title = "Uala",
        photo = "https://comeet-euw-app.s3.amazonaws.com/1115/01b7d1420966eafbbd3add5f607ec1d479cd998e",
        route = AppRoutes.ProvidersList.route,
        params = arrayListOf("Dia%")
    )



)