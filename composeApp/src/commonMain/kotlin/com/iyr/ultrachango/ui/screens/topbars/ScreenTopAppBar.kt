package com.iyr.ultrachango.ui.screens.topbars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.ui.elements.StyleScreenTitle
import com.iyr.ultrachango.utils.ui.elements.StyleTextBig
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopAppBar(
    modifier: Modifier = Modifier,
    navController : NavController,
    title: String,
    actionIcons:  List<Pair<ImageVector, () -> Unit>> = emptyList()
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        navigationIcon = {
            Icon(
                Icons.Default.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier.size(36.dp)

                    .background(Color.White, CircleShape)
                    .clickable {
                        navController.popBackStack()
                    }
               ,

            )
        },

        title = {
            Spacer( modifier = Modifier.width(16.dp))
            Text(modifier = Modifier.padding(start = 16.dp),
                style = StyleScreenTitle(),
                text = title)
        },

        actions = {
            actionIcons.forEach { (icon, action) ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { action() }
                )
            }

            //     UserImage(getProfileImageURL(userKey, urlImage))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent, // Fondo transparente
            scrolledContainerColor = Color.Transparent // También para scroll
        ),
    )
}

/*
@Composable
private fun UserImage(urlImage: String?) {
    if (urlImage != null) {
        AsyncImage(
            model = urlImage,
            contentDescription = "User Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(resource = Res.drawable.profile_pic),
            contentDescription = "User Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}

 */