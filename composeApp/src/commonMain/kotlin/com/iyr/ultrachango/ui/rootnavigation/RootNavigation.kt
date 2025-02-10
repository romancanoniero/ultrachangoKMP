package com.iyr.ultrachango.ui.rootnavigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.iyr.ultrachango.auth.AuthRepositoryImpl
import com.iyr.ultrachango.auth.AuthViewModel
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.onItemClick
import com.iyr.ultrachango.ui.MainScreen
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.screens.auth.config.profile.RegistrationProfileScreen
import com.iyr.ultrachango.ui.screens.fidelization.FidelizationScreen
import com.iyr.ultrachango.ui.screens.home.HomeScreen
import com.iyr.ultrachango.ui.screens.landing.LandingScreen
import com.iyr.ultrachango.ui.screens.locations.main.LocationsScreen
import com.iyr.ultrachango.ui.screens.auth.forgot.ForgotPasswordScreen

import com.iyr.ultrachango.ui.screens.auth.login.LoginScreen
import com.iyr.ultrachango.ui.screens.auth.registration.RegisterScreen
import com.iyr.ultrachango.ui.screens.member.MembersScreen
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.ui.screens.navigation.bottombar.BottomNavigationBar
import com.iyr.ultrachango.ui.screens.navigation.navigationItemsLists
import com.iyr.ultrachango.ui.screens.setting.SettingScreen.SettingScreen
import com.iyr.ultrachango.ui.screens.setting.profile.ProfileScreen
import com.iyr.ultrachango.ui.screens.shoppinglist.edition.ShoppingListAddEditScreen
import com.iyr.ultrachango.ui.screens.shoppinglist.main.ShoppingListScreen
import com.iyr.ultrachango.ui.screens.shoppinglist.members.ShoppingMembersSelectionScreen
import com.iyr.ultrachango.validateForm
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


object Graph {
    //  const val ROOT_NAVIGATION_SCREEN_GRAPH = "login"
    const val ROOT_NAVIGATION_SCREEN_LANDING = "landing"
    const val ROOT_NAVIGATION_SCREEN_MAIN = "home"
    const val ROOT_NAVIGATION_SCREEN_SETUP_PROFILE = "setup_profile"
}

@Composable
fun RootNavGraph(
    rootNavController: NavHostController,
    permissionsController: PermissionsController,
    scaffoldVM: ScaffoldViewModel,
    authViewModel: AuthViewModel,
    authRepository: AuthRepositoryImpl
) {

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Obtengo el usuario actual

    NavHost(
        modifier = Modifier.padding(0.dp),
        navController = rootNavController,
        startDestination = if (isLoggedIn) {
            val me = authRepository.getCurrentUser()

            val isProfileComplete = validateForm(
                validateImage = false,
                firstName = me?.firstName,
                lastName = me?.lastName,
                gender = me?.gender,
                birthDate = me?.birthDate,
            )


            if (isProfileComplete) Graph.ROOT_NAVIGATION_SCREEN_MAIN
            else {

                val route = RootRoutes.SetupProfileRoute.createRoute(me)
                route
            }
        } else Graph.ROOT_NAVIGATION_SCREEN_LANDING,
    ) {

        composable(route = RootRoutes.LandingRoute.route) {
            LandingScreen(rootNavController, permissionsController)
        }

        composable(route = RootRoutes.LoginRoute.route) {
            LoginScreen(rootNavController, permissionsController)
        }

        composable(route = RootRoutes.ForgotPasswordRoute.route) {
            ForgotPasswordScreen(rootNavController, permissionsController)
        }


        composable(route = RootRoutes.RegisterRoute.route) {
            RegisterScreen(rootNavController, permissionsController)
        }




        composable(
            route = "setup_profile/{userAsJson}", arguments = listOf(
                navArgument("userAsJson") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val user = Json.decodeFromString<User>(
                backStackEntry.arguments?.getString("userAsJson").toString()
            )
            RegistrationProfileScreen(
                navController = rootNavController,
                permissionsController = permissionsController,
                currentUser = user,

                )
        }
//----------
        composable(
            route = "shoppinglistedit/{userKey}/{listId}",
            arguments = listOf(navArgument("userKey") { type = NavType.StringType },
                navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userKey: String = backStackEntry.arguments?.getString("userKey").toString()
            val shoppingListId = backStackEntry.arguments?.getInt("listId")
            ShoppingListAddEditScreen(userKey,
                shoppingListId,
                rootNavController,
                scaffoldVM = scaffoldVM,
                vm = koinViewModel(parameters = { parametersOf(userKey, shoppingListId) }),
                permissionsController = permissionsController,
                onGroupButtonClicked = { listId ->

                    rootNavController.navigate(AppRoutes.MembersRoute.route)
                })
        }


        //------------

        composable(route = RootRoutes.MainScreenRoute.route) {
            Scaffold(modifier = Modifier.padding(0.dp).background(Color.Green), bottomBar = {
                BottomNavigationBar(items = navigationItemsLists,
                    currentRoute = route,
                    onItemClick = { currentNavigationItem ->
                        onItemClick(
                            rootNavController, currentNavigationItem
                        )
                    })
            }) { innerPadding ->
                Box(
                    modifier = Modifier.padding(40.dp).background(Color.Blue)
                ) {
                    MainScreen(rootNavController, permissionsController)
                }
            }
        }


        composable(route = AppRoutes.HomeRoute.route) {

            Scaffold(modifier = Modifier.padding(0.dp), bottomBar = {
                BottomNavigationBar(items = navigationItemsLists,
                    currentRoute = route,
                    onItemClick = { currentNavigationItem ->
                        onItemClick(
                            rootNavController, currentNavigationItem
                        )
                    })
            }) { innerPadding ->
                Box(

                ) {
                    HomeScreen(
                       navController =   rootNavController,
                        permissionsController = permissionsController,
                        scaffoldVM = scaffoldVM
                    )
                }
            }




        }
/*
        composable(route = AppRoutes.HomeRoute.route) {
            HomeScreen(
                rootNavController,

                scaffoldVM = scaffoldVM
            )
        }
*/
        composable(route = AppRoutes.ShoppingListRoute.route) {
            ShoppingListScreen(rootNavController, scaffoldVM = scaffoldVM)
        }

        composable(
            route = "shoppinglistadd"
        ) { backStackEntry ->
            ShoppingListAddEditScreen("",
                null,
                rootNavController,
                scaffoldVM = scaffoldVM,
                permissionsController = permissionsController,
                vm = koinViewModel(parameters = { parametersOf("") }

                )


            )
        }

        composable(
            route = AppRoutes.ShoppingListMembersRoute.route,
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0
            ShoppingMembersSelectionScreen(
                listId = listId, rootNavController, scaffoldVM = scaffoldVM
            )
        }



        composable(
            route = "shoppinglistedit/{userKey}/{listId}",
            arguments = listOf(navArgument("userKey") { type = NavType.StringType },
                navArgument("listId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userKey: String = backStackEntry.arguments?.getString("userKey").toString()
            val shoppingListId = backStackEntry.arguments?.getInt("listId")
            ShoppingListAddEditScreen(userKey,
                shoppingListId,
                rootNavController,
                scaffoldVM = scaffoldVM,
                vm = koinViewModel(parameters = { parametersOf(userKey, shoppingListId) }),
                permissionsController = permissionsController,
                onGroupButtonClicked = { listId ->

                    rootNavController.navigate(AppRoutes.MembersRoute.route)
                })
        }

        composable(route = AppRoutes.LocationRoute.route) {
            LocationsScreen(
                onAddLocation = {
                    rootNavController.navigate(AppRoutes.LocationsetailsRoute.route)
                }, vm = koinViewModel(), scaffoldVM = scaffoldVM
            )
        }

        composable(route = AppRoutes.LocationsetailsRoute.route) {

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



        composable(route = RootRoutes.ProfileScreenRoute.route) {
            ProfileScreen(rootNavController, permissionsController)
        }

        composable(
            route = AppRoutes.MembersRoute.route,
        ) {
            MembersScreen(
                appNavController = rootNavController,
                //       permissionsController = permissionsController,
                scaffoldVM = scaffoldVM
            )
        }


    }


}

