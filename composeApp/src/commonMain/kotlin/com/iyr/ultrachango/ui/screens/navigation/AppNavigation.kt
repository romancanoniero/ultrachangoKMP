package com.iyr.ultrachango.ui.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.iyr.ultrachango.data.models.sampleLocations
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.fidelization.FidelizationScreen
import com.iyr.ultrachango.ui.screens.home.HomeScreen
import com.iyr.ultrachango.ui.screens.locations.dialogs.LocationDialog
import com.iyr.ultrachango.ui.screens.locations.main.LocationsDetailsScreen
import com.iyr.ultrachango.ui.screens.locations.main.LocationsScreen
import com.iyr.ultrachango.ui.screens.member.MembersScreen
import com.iyr.ultrachango.ui.screens.navigation.bottombar.NavigationItem
import com.iyr.ultrachango.ui.screens.setting.SettingDetailScreen.SettingDetailScreen
import com.iyr.ultrachango.ui.screens.setting.SettingScreen.SettingScreen
import com.iyr.ultrachango.ui.screens.shoppinglist.edition.ShoppingListAddEditScreen
import com.iyr.ultrachango.ui.screens.shoppinglist.main.ShoppingListScreen
import com.iyr.ultrachango.ui.screens.shoppinglist.members.ShoppingMembersSelectionScreen
import com.iyr.ultrachango.utils.modules.BarcodeScannerScreen
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


object Graph {
    const val NAVIGATION_BAR_SCREEN_GRAPH = "navigationBarScreenGraph"
}

val navigationItemsLists = listOf(
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "Home",
        route = AppRoutes.HomeRoute.route,
    ),
    NavigationItem(
        unSelectedIcon = Icons.AutoMirrored.Outlined.ListAlt,
        selectedIcon = Icons.AutoMirrored.Filled.ListAlt,
        title = "Listas",
        route = AppRoutes.ShoppingListRoute.route,
    ),
    NavigationItem(
        unSelectedIcon = Icons.Outlined.ShoppingCart,
        selectedIcon = Icons.Filled.ShoppingCart,
        title = "Preparar",
        route = AppRoutes.SettingRoute.route,
    ),
    NavigationItem(
        unSelectedIcon = Icons.Outlined.LocationCity,
        selectedIcon = Icons.Filled.LocationCity,
        title = "Ubicaciones",
        route = AppRoutes.LocationRoute.route,
    ),

    NavigationItem(
        unSelectedIcon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        title = "Setting",
        route = AppRoutes.SettingRoute.route,
    ),
)




@Composable
fun AppNavGraph(
    navController: NavHostController,
    permissionsController: PermissionsController,
    scaffoldVM: ScaffoldViewModel
) {

    scaffoldVM.setTitle("Miembros de la Lista")
    NavHost(
        navController = navController,
        startDestination = Graph.NAVIGATION_BAR_SCREEN_GRAPH,
    ) {

        composable(route = AppRoutes.ShoppingListRoute.route) {
            ShoppingListScreen(
                navController, scaffoldVM = scaffoldVM
            )
        }

        composable(
            route = AppRoutes.ShoppingListMembersRoute.route,
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0
            ShoppingMembersSelectionScreen(
                listId = listId,
                navController,
                scaffoldVM = scaffoldVM
            )
        }

        mainNavGraph(
            rootNavController = navController,
            permissionsController = permissionsController,
            scaffoldVM = scaffoldVM

        )

        composable(
            route = AppRoutes.SettingDetail.route,
        ) {
            SettingDetailScreen(navController = navController)
        }

        composable(
            route = AppRoutes.MembersRoute.route,
        ) {
            MembersScreen(
                appNavController = navController,
                //       permissionsController = permissionsController,
                scaffoldVM = scaffoldVM
            )
        }


    }
}


fun NavGraphBuilder.mainNavGraph(
    rootNavController: NavHostController,
    permissionsController: PermissionsController,
    scaffoldVM: ScaffoldViewModel

) {

    navigation(
        startDestination = AppRoutes.HomeRoute.route, route = Graph.NAVIGATION_BAR_SCREEN_GRAPH
    ) {


        composable(route = AppRoutes.HomeRoute.route) {
            HomeScreen(
                navController = rootNavController,
                permissionsController = permissionsController,
                scaffoldVM = scaffoldVM
            )
        }

        composable(route = AppRoutes.ShoppingListRoute.route) {
            ShoppingListScreen(rootNavController, scaffoldVM = scaffoldVM)
        }

        composable(
            route = "shoppinglistadd"
        ) { backStackEntry ->
            ShoppingListAddEditScreen("", null,
                rootNavController,
                scaffoldVM = scaffoldVM,
                permissionsController = permissionsController,
                vm = koinViewModel(parameters = { parametersOf("") }

                )


            )
        }

        composable(
            route = "shoppinglistedit/{userKey}/{listId}",
            arguments = listOf(
                navArgument("userKey") { type = NavType.StringType },
                navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userKey: String = backStackEntry.arguments?.getString("userKey").toString()
            val shoppingListId = backStackEntry.arguments?.getInt("listId")
            ShoppingListAddEditScreen(
                userKey,
                shoppingListId,
                rootNavController,
                scaffoldVM = scaffoldVM,
                vm = koinViewModel(parameters = { parametersOf(userKey, shoppingListId) }),
                permissionsController = permissionsController,
                onGroupButtonClicked = { listId ->

                    rootNavController.navigate(AppRoutes.MembersRoute.route)
                })
        }


        /*
                composable(route = "members/{listId}",
                    arguments = listOf(
                        navArgument("listId") { type = NavType.IntType })) { backStackEntry ->
                    val shoppingListId = backStackEntry.arguments?.getInt("listId")
                    MembersScreen(
                        shoppingListId,
                        rootNavController,
                        scaffoldVM = scaffoldVM,
                        vm = koinViewModel(parameters = { parametersOf(shoppingListId) }),

                }

        */

        composable(route = AppRoutes.LocationRoute.route) {
            LocationsScreen(
                onAddLocation = {
                    rootNavController.navigate(AppRoutes.LocationsetailsRoute.route)
                },
                vm = koinViewModel(),
                scaffoldVM = scaffoldVM
            )
        }

        composable(route = AppRoutes.LocationsetailsRoute.route) {
            /*
              LocationDialog(
                  title = "Ubicacion",
                  onAccept = {},
                  onDismiss = {

                  },
              )
          */
        }

        composable(route = AppRoutes.FidelizationRoute.route) {
            FidelizationScreen(navController = rootNavController)
        }

        composable(route = AppRoutes.SettingRoute.route) {
            SettingScreen(
                navController = rootNavController,
                authRepository = koinInject(),
                permissionController = permissionsController,
                viewModel = koinViewModel()
            )
        }
    }
}


@Composable
fun Navigation(
    modifier: Modifier = Modifier
    // ,
    // productsDao: ProductsDao
) {
    val navController = rememberNavController()

    val client = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    isLenient = true
                })
            }
        }
    }

    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController =
        remember(factory) { factory.createPermissionsController() }

    /*
        val viewModel = viewModel {
            HomeScreenViewModel( permissionsController)
        }
    */


    NavHost(navController, startDestination = "home") {


        composable(AppRoutes.HomeRoute.route) {

            //HomeScreen(navController, viewModel) }


            composable(AppRoutes.BarcodeScanner.route) { backStackEntry ->

                BarcodeScannerScreen(navController, onResult = { result ->
                    //devolver barcode a la pantalla anterior
                    backStackEntry.savedStateHandle["onBarcodeScanned"] = result
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "onBarcodeScanned", result
                    )
                    navController.popBackStack()
                })
            }

        }
    }
}