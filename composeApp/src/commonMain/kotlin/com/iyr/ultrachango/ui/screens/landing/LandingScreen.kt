package com.iyr.ultrachango.ui.screens.landing

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.ui.screens.landing.pages.Page1
import com.iyr.ultrachango.ui.screens.landing.pages.Page2
import com.iyr.ultrachango.ui.screens.landing.pages.Page3
import com.iyr.ultrachango.ui.screens.landing.pages.Page4
import com.iyr.ultrachango.utils.ui.device.getScreenWidth
import com.iyr.ultrachango.utils.ui.elements.CustomButton
import com.iyr.ultrachango.utils.ui.pagerindicator.PagerIndicator
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.market_bg_1

@Composable
fun LandingScreen(
    navController: NavController,
    permissionsController: PermissionsController
) {

    /*
        modifier: Modifier = Modifier,
        pages: List<@Composable () -> Unit>,
        onLastPageButtonClick: () -> Unit

     */
    var showGoToLogin by remember { mutableStateOf(false) }
    var pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 4 }
    )

    Box(modifier = Modifier.fillMaxSize().padding(0.dp).background(Color.LightGray))
    {
        Image(
            painter = painterResource( Res.drawable.market_bg_1),
            contentDescription = "Logo",
            contentScale = ContentScale.FillHeight
            ,
            modifier = Modifier
                .fillMaxSize()
//                .padding(16.dp)
                .align(Alignment.TopCenter)
        )

        Box(modifier = Modifier.fillMaxSize())
        {


            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                pageContent = { page ->
                    // page composable
                    when (page) {
                        0 -> {
                            Page1()
                        }

                        1 -> {
                            Page2()
                        }

                        2 -> {
                            Page3()
                        }

                        3 -> {
                            Page4()
                        }
                    }
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        )
        {

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                )
                {
                    PagerIndicator(
                        modifier = Modifier
                            .background(Color.Transparent),

                        pagerState = pagerState,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (showGoToLogin) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                    )
                    {

                        CustomButton(
                            onClick = {
                                triggerHapticFeedback()
                                navController.navigate(RootRoutes.LoginRoute.route)
                            },
                            modifier = Modifier
                                .padding(16.dp),
                            enabled = true,
                            content = {
                                Text("Go to Login")
                            },

                        )
                    }
                }
            }


        }
    }


    /*
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 1f,
        pageCount = { 4 }
    )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            pages[page]()
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            pages.forEachIndexed { index, _ ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .padding(4.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                )
            }
        }

        if (pagerState.currentPage == pages.size - 1) {
            Button(
                onClick = onLastPageButtonClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Go to Login")
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            if (pagerState.currentPage == pages.size - 1) {
                coroutineScope.launch {
                    pagerState.scrollToPage(0)
                }
            }
        }
    }
*/

    LaunchedEffect(Unit) {
        while (pagerState.canScrollForward) {
            delay(500)
            pagerState.animateScrollToPage(pagerState.currentPage + 1) // Cambia a la siguiente página
            if (pagerState.currentPage == pagerState.pageCount - 1) {
                showGoToLogin = true
                delay(4000)
                navController.navigate(RootRoutes.LoginRoute.route)
            }
        }
    }
}

@Composable
fun AnimateFromRightToLeft(
    content: @Composable () -> Unit
) {

    val screenWidth: Dp = getScreenWidth()
    val initialOffset = 0.8f * screenWidth.value
    var offset by remember { mutableStateOf(initialOffset) }
    val animatedOffset by animateDpAsState(targetValue = Dp(offset))

    LaunchedEffect(Unit) {
        offset = 100f // Change this value to move the composable
    }
    Box(modifier = Modifier.offset(x = animatedOffset)) {
        content()
    }

}