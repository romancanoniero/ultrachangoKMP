package com.iyr.ultrachango.ui.screens.landing.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iyr.ultrachango.ui.screens.landing.AnimateFromRightToLeft
import com.iyr.ultrachango.utils.ui.device.getScreenWidth
import com.iyr.ultrachango.utils.ui.elements.StyleBigTitle
import com.iyr.ultrachango.utils.ui.elements.textSizeLandingMessage
import com.iyr.ultrachango.utils.ui.elements.textSizeLandingTitle
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.landing_emocion
import ultrachango2.composeapp.generated.resources.logo_icbc

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Page1() {

    var targetState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500) // Delay for 2 seconds
        targetState.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)

    ) {
        Box(modifier = Modifier.fillMaxSize())
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                )
            {

                Image(
                    painter = painterResource(Res.drawable.landing_emocion),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.BottomCenter),


                    )
            }

            Box(
                modifier = Modifier.fillMaxSize().background(Color.Transparent).alpha(1f)
                    .padding(top = 20.dp, start = 20.dp)
            )
            {

                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedContent(
                        targetState = targetState,
                        transitionSpec = {
                            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
                        }
                    ) { targetText ->
                        Text(
                            modifier = Modifier.shadow(4.dp),
                            text = "YA LLEGO!!",
                            style = StyleBigTitle().copy(
                                fontSize = textSizeLandingTitle,
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            ),
                            color = Color.White,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    AnimatedContent(
                        targetState = targetState,
                        transitionSpec = {
                            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
                        }
                    ) { targetText ->
                        Text(

                            text = "Una nueva forma de ahorrar ",
                            style = StyleBigTitle().copy(
                                fontSize = textSizeLandingTitle,
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            ),
                            color = Color.White,
                        )
                    }




                    Spacer(modifier = Modifier.weight(1f))

                }

            }
        }


    }


}
