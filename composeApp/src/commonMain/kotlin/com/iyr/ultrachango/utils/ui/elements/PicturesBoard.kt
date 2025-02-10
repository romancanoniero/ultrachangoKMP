package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.runBlocking

@Composable
fun PicturesBoard(
    modifier: Modifier,
    imagesList: List<String>,
    contentDescriptionList: List<String>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {

        when (imagesList.size) {
            1 -> {
                AsyncImage(

                    model = imagesList[0],
                    contentDescription = contentDescriptionList[0],
                    contentScale = ContentScale.Crop,
                    onError = {
                        var error = it
                    })
            }

            2 -> {
                Row(modifier = Modifier.fillMaxSize()) {

                    var index = 0
                    imagesList.forEach { image ->
                        AsyncImage(
                            modifier = modifier
                                .fillMaxWidth(.50f)
                                .fillMaxHeight()
                                .weight(1f),
                            model = imagesList[index],
                            contentDescription = contentDescriptionList[index],
                            contentScale = ContentScale.Crop,
                            onError = {
                                var error = it
                            }, onSuccess = {
                                index++
                            }
                        )

                    }
                }
            }

            3 -> {
                var index = 0

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(1f)) {
                        imagesList.take(2).forEach { image ->
                            AsyncImage(
                                modifier = modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                model = imagesList[index],
                                contentDescription = contentDescriptionList[index],
                                contentScale = ContentScale.Crop,
                                onError = {
                                    var error = it
                                })
                            index++

                        }
                    }
                    AsyncImage(
                        modifier = modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        model = imagesList[2],
                        contentDescription = contentDescriptionList[2],
                        contentScale = ContentScale.Crop,
                        onError = {
                            var error = it
                        })

                }
            }

            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    var index = 0
                    for (i in 0..1) {
                        Row(modifier = Modifier.weight(1f)) {
                            imagesList.drop(i * 2).take(2).forEach { painter ->
                                AsyncImage(
                                    modifier = modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    model = imagesList[index],
                                    contentDescription = contentDescriptionList[2],
                                    contentScale = ContentScale.Crop,
                                    onError = {
                                        var error = it
                                    })

                                index++

                            }
                        }
                    }
                }
            }
        }
    }
}
