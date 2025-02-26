package com.iyr.ultrachango.ui.screens.shoppinglist.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListMemberComplete
import com.iyr.ultrachango.ui.ScaffoldViewModel

import com.iyr.ultrachango.ui.dialogs.ConfirmationDialog
import com.iyr.ultrachango.ui.dialogs.ErrorDialog
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.navigation.AppRoutes
import com.iyr.ultrachango.ui.screens.shoppinglist.dialogs.RenameDialog
import com.iyr.ultrachango.ui.screens.shoppinglist.edition.RoundMemberItem
import com.iyr.ultrachango.ui.theme.screenBackground
import com.iyr.ultrachango.utils.ui.elements.ItemListContainer
import com.iyr.ultrachango.utils.ui.elements.ItemListTextHeader
import com.iyr.ultrachango.utils.ui.elements.ItemListTextRegular
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
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.no_products
import ultrachango2.composeapp.generated.resources.products


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingListScreen(
    navController: NavHostController? = null,
    vm: ShoppingListViewModel = koinViewModel(),
    scaffoldVM: ScaffoldViewModel? = null,
) {
    if (scaffoldVM != null) {
        scaffoldVM.updateState(ScaffoldViewModel.UiState(title = "Listas de Compras"))
    }

    val state = vm.state.collectAsState()

    var showCreateDialog by remember { mutableStateOf<Boolean>(false) }
    var showRenameDialog by remember { mutableStateOf<Pair<Boolean, ShoppingList?>?>(null) }

    var isRefreshing by remember {
        mutableStateOf(false)
    }
    var pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        vm.onRefreshRequested()
    })


    /*
    LaunchedEffect(Unit)
    {
        vm.fetchLists()
    }
*/

    if (state.value.showErrorMessage) {
        ErrorDialog(
            title = "Error",
            message = state.value.errorMessage.toString(),
            onDismissRequest = {
                vm.closeErrorDialogRequest()
            }
        )
    }


    if (showCreateDialog == true) {
        RenameDialog(
            title = "Nueva Lista",
            message = "Nombre de la lista",
            acceptText = "Crear",
            cancelText = "Cancelar",
            onAccept = { newName ->
                vm.onCreationRequested(newName)
                showCreateDialog = false
            },
            onDismiss = {
                showCreateDialog = false
            },
        )
    }

    if (state.value.showDeletionConfirmationDialog != null) {

        val entity = state.value.showDeletionConfirmationDialog!!.get("entity") as ShoppingList?

        ConfirmationDialog(
            title = "Eliminar Lista",
            message = "Estas seguro que deseas eliminar la lista ${entity?.listName?.uppercase()}?",
            onAccept = {
                entity?.let {
                    vm.deleteShoppingList(it)
                }

            },
            onDismiss = {
                vm.onResetDialogsRequested()
            }
        )
    }


    if (showRenameDialog?.first == true) {
        RenameDialog(
            shoppingList = showRenameDialog?.second!!,
            onAccept = { newName ->
                var pepe = newName
                vm.onRenameRequested(showRenameDialog?.second!!, newName)
                showRenameDialog = Pair<Boolean, ShoppingList?>(false, null)
            },
            onDismiss = {
                showRenameDialog = Pair<Boolean, ShoppingList?>(false, null)
            }
        )
    }


    val showDeleteConfirmationDialog by remember { mutableStateOf(null) }


  //  val nav by remember { mutableStateOf(navController) }
    val scope = rememberCoroutineScope()


    fun onItemClicked(shoppingList: ShoppingList): Unit {
        val route =
            RootRoutes.ShoppingListEditRoute.createRoute(shoppingList.userId, shoppingList.listId, shoppingList.listName)
//            AppRoutes.ShoppingListEditRoute.createRoute(shoppingList.userId, shoppingList.listId)
        navController?.navigate(route)
    }

    fun onAddButtonClicked(): Unit {
        showCreateDialog = true
    }


    fun onEditClicked(shoppingList: ShoppingList): Unit {
        showRenameDialog = Pair(true, shoppingList)
    }

    fun onDeleteClicked(shoppingList: ShoppingList): Unit {
        vm.onDeleteButtonClicked(shoppingList)
    }


    PullRefreshLayout(
        modifier = Modifier.fillMaxWidth(),
        state = pullRefreshState
    ) {

        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(screenOuterPadding)
        )
        {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)

                    .weight(1f)
            )
            {
                state.value.list.forEach { shoppingList ->
                    item {
                        val swipeableState: SwipeableState<Int> =
                            rememberSwipeableState(initialValue = 0)

                        SwipeToRevealItem(
                            swipeableState = swipeableState,
                            onEditClick = {
                                scope.launch {
                                    swipeableState.animateTo(0)
                                    onEditClicked(shoppingList)
                                }

                            },
                            onDeleteClick = {
                                scope.launch {
                                    //                            swipeableState.animateTo(0)
                                    onDeleteClicked(shoppingList)
                                }
                            },
                            content = {
                                ShoppingListItem(
                                    shoppingList,
                                    onClick = {
                                        triggerHapticFeedback()
                                        onItemClicked(shoppingList)
                                    },


                                    )
                            })
                    }
                }
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, buttonShapeSmall)
                .padding(2.dp),
                colors = androidx.compose.material.ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black, contentColor = Color.White
                ),

                onClick = {
                    triggerHapticFeedback()
                    onAddButtonClicked()
                },
                content = {
                    androidx.compose.material.Text(
                        color = Color.White, text = "Nueva Lista"
                    )
                })
        }
    }
}


@Composable
fun ShoppingListItem(
    shoppingList: ShoppingList,
    onClick: (ShoppingList) -> Unit,

    ) {


    Box(
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable {
                triggerHapticFeedback()
                onClick(shoppingList)
            })
    {

        Column() {
            Row {
                ItemListTextHeader(
                    modifier = Modifier.weight(1f),
                    text = shoppingList.listName.toString()
                )
                Spacer(modifier = Modifier.width(8.dp))

                val members = shoppingList.members ?: emptyList()
                Row() {
                    members.forEach { member ->
                        //  items(members) { user ->
                        RoundMemberItem(
                            avatarSize = 36.dp,
                            member = member
                        )
                    }
                }

            }


            val productQtyLegend =
                if (shoppingList.items?.size ?: 0 > 0)
                    (shoppingList.items?.size
                        ?: 0).toString() + " " + stringResource(Res.string.products)
                else
                    stringResource(Res.string.no_products)
            ItemListTextRegular(text = productQtyLegend)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRevealItem(
    content: @Composable () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    swipeableState: SwipeableState<Int>
) {

    ///-------
    val coroutineScope = rememberCoroutineScope()
    SwipeBox(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 14.dp),
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
                    onDeleteClick.invoke()
                }
            }
        }
    ) { _, _, _ ->

        ItemListContainer(modifier = Modifier.height(IntrinsicSize.Min)) {
            content()
        }
    }
}