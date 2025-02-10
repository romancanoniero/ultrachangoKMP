package com.iyr.ultrachango.ui.screens.navigation.topappbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.iyr.ultrachango.ui.ScaffoldViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    scaffoldVM: ScaffoldViewModel,
    canNavigateBack : Boolean = false
) = TopAppBar(
    title = {
//        val currentRoute = navController.currentBackStackEntry?.arguments?.getString("route")

        var title by remember { mutableStateOf("") }

        val state by scaffoldVM.state.collectAsState()


        LaunchedEffect(scaffoldVM.state) {
            scaffoldVM.state.collect { it ->
                title = it.title
            }
        }

        if (state.title.isNotEmpty()) {
            Text(
                text = state.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    },
    actions = {
        val state by scaffoldVM.state.collectAsState()
        state.barButtons()
    },
    navigationIcon = {
     //   var showBackButton by remember { mutableStateOf(false) }

/*
        LaunchedEffect(navController.currentBackStackEntryFlow) {
            navController.currentBackStackEntryFlow.collect {

                if (!it.destination.route.equals("home")) {
                    showBackButton = true
                }

            }

        }
        */


        if (canNavigateBack)
            IconButton(onClick = {
                navController.navigateUp()
            })
            {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
    }
)