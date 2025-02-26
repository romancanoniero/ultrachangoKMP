package com.iyr.ultrachango.ui.screens.locations.main


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.iyr.ultrachango.data.models.Location
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.dialogs.ConfirmationDialog
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.screens.locations.dialogs.LocationDialog
import com.iyr.ultrachango.ui.theme.screenBackground
import com.iyr.ultrachango.utils.ui.capitalizeFirstLetter
import com.iyr.ultrachango.utils.ui.elements.ItemListContainer
import com.iyr.ultrachango.utils.ui.elements.ItemListTextHeader
import com.iyr.ultrachango.utils.ui.elements.ItemListTextSubHeader
import com.iyr.ultrachango.utils.ui.elements.buttonShapeSmall
import com.iyr.ultrachango.utils.ui.elements.itemListHeightStandard
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import com.kevinnzou.swipebox.SwipeBox
import com.kevinnzou.swipebox.SwipeDirection
import com.kevinnzou.swipebox.widget.SwipeIcon
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationsScreen(
    vm: LocationsViewModel,
    onAddLocation: (() -> Unit?)? = null,
    scaffoldVM: ScaffoldViewModel,
) {

    val state = vm.state

    scaffoldVM.updateState(ScaffoldViewModel.UiState(title = "Ubicaciones"))



    var isRefreshing by remember {
        mutableStateOf(false)
    }
    var pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        vm.onRefreshRequested()
        /* Refresh some data here */
    })


    fun onDeleteRequested(locationID: Int) {
        vm.onDeleteRequested(locationID)
    }


    if (state.showDeletionDialog!=null)
    {
        ConfirmationDialog(
            title = "Delete Location",
            message = "Are you sure you want to delete this location?",
            onAccept = {
                val map : HashMap<String, Any> = state.showDeletionDialog as HashMap<String, Any>
                val locationID = map.get("ID") as Int
                vm.deleteLocation(locationID)
                vm.closeDialogs()
            },
            onDismiss = {
                vm.closeDialogs()
            }
        )
    }

    if (state.showErrorMessage) {
        ErrorDialog(
            title = "Error",
            message = state.errorMessage.toString(),
            onDismissRequest = {
                vm.closeErrorDialogRequest()
            }
        )
    }


    if (state.showLocationSearchDialog) {
        LocationDialog(
            title = "Ubicacion",
            onAccept = { name, location ->
                vm.onLocationUpdateRequested(name, location)

            },
            onDismiss = {
         //       vm.closeLocationSearchDialogRequest()
                vm.closeDialogs()
            },
        )
    }


    PullRefreshLayout(
        modifier = Modifier.fillMaxWidth()

        ,
        state = pullRefreshState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(screenOuterPadding)
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)
                .background(Color.Transparent)) {
                items(state.list) { location ->
                    LocationItem(location,
                        onDelete = { locationID ->
                            onDeleteRequested(locationID)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, buttonShapeSmall)
                    .padding(2.dp),
                onClick = {
                    triggerHapticFeedback()
                    //       onAddLocation?.invoke()
                    vm.onAddLocationRequested()
                },

                content = {
                    Text(
                        color = Color.White,
                        text = "Add Location"
                    )
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black, contentColor = Color.White
                ),

                )
        }

    }


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationItem(
    location: Location,
    onDelete: ((locationID: Int) -> Unit)
) {
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 60.dp,
        endContent = { swipeableState, endSwipeProgress ->
            SwipeIcon(

                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                background = Color(0xFFFA1E32),
                weight = 1f,
                iconSize = 20.dp,

                ) {
                coroutineScope.launch {
                    swipeableState.animateTo(0)
                    onDelete.invoke(location.id)
                }
            }
        }
    ) { _, _, _ ->

        ItemListContainer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
            ,
            contentAlignment = Alignment.Center
        ) {
            // Contenido
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(itemListHeightStandard)

            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    ItemListTextHeader(text = location.title.capitalizeFirstLetter())
                    Spacer(modifier = Modifier.width(10.dp))
                    ItemListTextSubHeader(text = location.toString())
                }
            }
        }
    }


}
