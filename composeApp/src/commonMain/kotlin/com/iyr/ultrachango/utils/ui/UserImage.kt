package com.iyr.ultrachango.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic

@Composable
fun UserImage(
    modifier: Modifier = Modifier,
    urlImage: String?,
    onClick: () -> Unit = {  },
    ) {

    if (urlImage != null) {
        AsyncImage(
            model = urlImage,
            contentDescription = "User Image",
            modifier = modifier
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(resource = Res.drawable.profile_pic),
            contentDescription = "User Image",
            modifier = modifier
                .clip(CircleShape)
        )
    }
}