package com.iyr.ultrachango.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic

@Composable
fun UserImage(
    modifier: Modifier = Modifier,
    urlImage: String?,
    onClick: () -> Unit = {  },
    ) {

    var status = remember { mutableStateOf<String>(
        "Loading"
    ) }

    if (urlImage != null) {

        Box(

        )
        {
            when(status.value){
                "Loading" -> {
                    // Show a loading spinner
                    LoadingIndicator(true)
                }
                "Error" -> {
                    LoadingIndicator(false)
                    Image(
                        painter = painterResource(resource = Res.drawable.profile_pic),
                        contentDescription = "User Image",
                        modifier = modifier
                            .clip(CircleShape)
                    )
                    // Show an error message
                }
                "Success" -> {
                    LoadingIndicator(false)
                    // The image has been loaded
                }
                "Empty" -> {}
            }


            AsyncImage(
                model = urlImage,
                contentDescription = "User Image",
                modifier = modifier
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onState = {
                    when (it) {
                        is AsyncImagePainter.State.Loading -> {
                            // Show a loading spinner
                            status.value = "Loading"
                        }
                        is AsyncImagePainter.State.Error -> {
                            // Show an error message
                            status.value = "Error"


                        }
                        is AsyncImagePainter.State.Success -> {
                            // The image has been loaded
                            status.value = "Success"
                        }

                        AsyncImagePainter.State.Empty -> {}
                    }
                }
            )



        }

    } else {
        Image(
            painter = painterResource(resource = Res.drawable.profile_pic),
            contentDescription = "User Image",
            modifier = modifier
                .clip(CircleShape)
        )
    }
}