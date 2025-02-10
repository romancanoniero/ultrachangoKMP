package com.iyr.ultrachango.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.ui.screens.navigation.AppNavGraph
import com.iyr.ultrachango.ui.screens.navigation.bottombar.BottomNavigationBar
import com.iyr.ultrachango.ui.screens.navigation.bottombar.NavigationItem
import com.iyr.ultrachango.ui.screens.navigation.navigationItemsLists
import com.iyr.ultrachango.ui.screens.navigation.sidenavigationbar.NavigationSideBar
import com.iyr.ultrachango.ui.screens.navigation.topappbar.TopBar
import com.iyr.ultrachango.ui.theme.screenBackground
import com.iyr.ultrachango.utils.ui.LoadingDialog
import com.iyr.ultrachango.utils.ui.ShowKeyboard
import com.iyr.ultrachango.utils.ui.showLoader
import com.iyr.ultrachango.viewmodels.UserViewModel
import dev.icerock.moko.permissions.PermissionsController
import org.koin.compose.viewmodel.koinViewModel


// Define el CompositionLocal


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    permissionsController: PermissionsController,
    userViewModel: UserViewModel = koinViewModel()
) {

    val windowSizeClass = calculateWindowSizeClass()
    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }

    val appNavController = rememberNavController()

    val navBackStackEntry by appNavController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    val navigationItem by remember {
        derivedStateOf {
            navigationItemsLists.find { it.route == currentRoute }
        }
    }
    val isMainScreenVisible by remember(isMediumExpandedWWSC) {
        derivedStateOf {
            navigationItem != null
        }
    }
    val isBottomBarVisible by remember(isMediumExpandedWWSC) {
        derivedStateOf {
            if (!isMediumExpandedWWSC) {
                navigationItem != null
            } else {
                false
            }
        }
    }

    var hideVirtualKeyboard by remember { mutableStateOf(false) }
    if (hideVirtualKeyboard) {
        ShowKeyboard(false)
        hideVirtualKeyboard = false
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)

    )

    /*
    Scaffold(
        topBar = {
            TopBar(
                appNavController = appNavController,
                permissionsController = permissionsController,
                currentRoute = currentRoute,
                isMediumExpandedWWSC = isMediumExpandedWWSC
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavigationBar(
                    appNavController = appNavController,
                    permissionsController = permissionsController,
                    currentRoute = currentRoute,
                    isMediumExpandedWWSC = isMediumExpandedWWSC,
                    onItemClick = { currentNavigationItem ->
                        appNavController.navigate(currentNavigationItem.route) {
                            popUpTo(appNavController.graph.startDestinationRoute ?: "") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        sideBar = {
            NavigationSideBar(
                appNavController = appNavController,
                permissionsController = permissionsController,
                currentRoute = currentRoute,
                isMediumExpandedWWSC = isMediumExpandedWWSC,
                onItemClick = { currentNavigationItem ->
                    appNavController.navigate(currentNavigationItem.route) {
                        popUpTo(appNavController.graph.startDestinationRoute ?: "") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        AppNavGraph(
            appNavController = appNavController,
            permissionsController = permissionsController,
            isMediumExpandedWWSC = isMediumExpandedWWSC,
            isBottomBarVisible = isBottomBarVisible,
            isMainScreenVisible = isMainScreenVisible
        )
    }
    ) {  }

    /*
    MainScaffold(
        internalNavController = appNavController,
        permissionsController = permissionsController,
        currentRoute = currentRoute,
        isMediumExpandedWWSC = isMediumExpandedWWSC,
        isBottomBarVisible = isBottomBarVisible,
        isMainScreenVisible = isMainScreenVisible,
        onItemClick = { currentNavigationItem ->
            appNavController.navigate(currentNavigationItem.route) {
                popUpTo(appNavController.graph.startDestinationRoute ?: "") {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

    )
*/
}
*/

}



data class MyScaffoldState(
    var showTitle: Boolean = true,
    var showBackIcon: Boolean = true,
    var titleText: String = "xxxxxxx",
    // ... otros elementos del Scaffold que quieras controlar
)