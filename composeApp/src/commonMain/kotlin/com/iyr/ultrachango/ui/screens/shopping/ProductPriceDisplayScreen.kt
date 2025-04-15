package com.iyr.ultrachango.ui.screens.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.Sucursale
import com.iyr.ultrachango.data.models.ShoppingCartProduct
import com.iyr.ultrachango.ui.screens.shoppingcart.ItemPriceInPos
import com.iyr.ultrachango.ui.screens.shoppingcart.ShoppingCartProductItemContent
import com.iyr.ultrachango.utils.formatCurrency
import com.iyr.ultrachango.utils.ui.LoadingDialog
import com.iyr.ultrachango.utils.ui.elements.H3Text
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.viewmodel.koinViewModel
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.sort_distance_2
import ultrachango2.composeapp.generated.resources.sort_price


@Composable
fun ProductPriceDisplayScreen(
    entityId : Int? = null,
    product: ShoppingCartProduct,
    vm: ProductPriceDisplayViewModel = koinViewModel(),

    ) {



    val state = vm.state.collectAsState()

    vm.setEntityId(entityId)

    val listState = rememberLazyListState()

    var onPriceSelected: (Sucursale) -> Unit = {
        vm.setSelectedPrice(product.ean.toString(), it)
        vm.sortList()
    }


    LaunchedEffect(product.ean) {
        try {
            vm.fetchData(product.ean.toString())
        } catch (ex: Exception) {
            println("Error fetching data: ${ex.message}")
        }
    }

    if (state.value.scrollToTop) {
        LaunchedEffect(Unit) {
            listState.scrollToItem(0)
            vm.setScrollToStart(false)
        }
    }


    Column(

    )
    {
        Header(product, vm.state.value.currentPrice)

        SortSection(
            vm = vm
        )


        if (state.value.loading)
        {
            LoadingDialog()
        }
        else
        {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
            {

                state.value.prices.forEach { sucursal ->
                    item(key = (sucursal.comercioId.toString() + "-" + sucursal.banderaId + "-" + sucursal.id))
                    {
                        ItemPriceInPos(
                            pos = sucursal,
                            onPriceSelected = onPriceSelected,
                            selected = vm.getSelectedPrice(),
                        )
                    }
                }
            }

        }
    }

}

@Composable
fun SortSection(
    modifier: Modifier = Modifier,
    vm: ProductPriceDisplayViewModel,
    onSortSelected: (String) -> Unit = {},
    selectedKey: String = "1",
) {
    var (selected, setSelected) = remember { mutableStateOf("") }
    val listOptions = arrayListOf<Pair<Int, DrawableResource>>(
        Pair(1, Res.drawable.sort_distance_2),
        Pair(2, Res.drawable.sort_price),
    )

    if (selected.isEmpty()) {
        selected = selectedKey
        vm.setSortList(selected.toInt())
    }

    Row()
    {
        listOptions.forEach { item ->

            Button(
                modifier = Modifier
                    .background(Color.White).padding(horizontal = 2.dp)
                    .weight(1f),
                onClick = {
                    setSelected(item.first.toString())
                    vm.setSortList(selected.toInt())
                },
                shape = RoundedCornerShape(
                    corner = CornerSize(2.dp),
                ),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (selected == item.first.toString()) Color.Gray else Color.LightGray,
                    contentColor = if (selected == item.first.toString()) Color.Black else Color.DarkGray,
                )

            )
            {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 4.dp),
                    bitmap = imageResource(item.second),
                    contentDescription = null,
                )
            }

        }
    }

}


@Composable
fun Header(
    product: ShoppingCartProduct,
    currentPrice: Sucursale?,
) {


    var itemQuantity = remember { mutableStateOf(product.qty!!) }




    ShoppingCartProductItemContent(
        modifier = Modifier.fillMaxWidth(),
        product = product,
        onItemClick = {},
        onDecrement = { ean, quantity ->
            println("Incremented $ean to $quantity")
            itemQuantity.value = quantity
        },
        onIncrement = { ean, quantity ->
            println("Incremented $ean to $quantity")
            itemQuantity.value = quantity
        },
        currentPrice = currentPrice

    )

    if (currentPrice != null) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            H3Text(
                text = formatCurrency(
                    currentPrice?.preciosProducto?.getBestPrice()
                        ?.times(itemQuantity.value) ?: 0.0
                ),
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 2.dp)
                    .clickable { },
                fontWeight = FontWeight.ExtraBold,
                shadow = true,
            )

        }


    }
}


