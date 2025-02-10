package com.iyr.ultrachango.ui.screens.navigation.bottombar

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val unSelectedIcon: ImageVector /* or  DrawableResource*/,
    val selectedIcon: ImageVector /* or  DrawableResource*/,
    val title: String /* or  StringResource  */,
    val route: String
)