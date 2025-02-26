package com.iyr.ultrachango.ui.screens.topbars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.ui.UserImage
import com.iyr.ultrachango.utils.ui.elements.ShareButton
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(userKey: String,
                  userName: String,
                  urlImage: String?,
                  onSharedClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column ( modifier = Modifier.fillMaxWidth()){
                Text(text = "Hola, $userName", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Bienvenido a UltraChango", style = MaterialTheme.typography.bodyMedium)
            }
        },
        actions = {
            ShareButton(
                modifier = Modifier.size(40.dp),
                onClick = onSharedClick
            )

            Spacer(modifier = Modifier.size(10.dp))

            UserImage(
                modifier = Modifier.size(40.dp),
                getProfileImageURL(userKey, urlImage) )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent, // Fondo transparente
            scrolledContainerColor = Color.Transparent // También para scroll
        ),

    )
}


