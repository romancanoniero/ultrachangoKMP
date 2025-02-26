package com.iyr.ultrachango.utils.ui.device.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.logo_email
import ultrachango2.composeapp.generated.resources.logo_facebook
import ultrachango2.composeapp.generated.resources.logo_instagram
import ultrachango2.composeapp.generated.resources.logo_sms
import ultrachango2.composeapp.generated.resources.logo_telegram
import ultrachango2.composeapp.generated.resources.logo_whatsapp

@Composable
fun rememberShareManager(): ShareManager {
    return remember { ShareManager() }
}

@Composable
fun ShareButtons(link: String) {
    val shareManager = rememberShareManager()

    val size =56.dp

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {

        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    shareManager.shareLink(link, SharePlatform.FACEBOOK)
                }
            ,
            painter = painterResource(Res.drawable.logo_facebook),
            contentDescription = "Compartir en Facebook"
        )


        Spacer(modifier = Modifier.height(8.dp))
        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    shareManager.shareLink(link, SharePlatform.INSTAGRAM)
                }
            ,
            painter = painterResource(Res.drawable.logo_instagram),
            contentDescription = "Compartir en Instagram"
        )


        Spacer(modifier = Modifier.height(8.dp))

        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    shareManager.shareLink(link, SharePlatform.WHATSAPP)
                }
            ,
            painter = painterResource(Res.drawable.logo_whatsapp),
            contentDescription = "Compartir en WhatsApp"
        )

        Spacer(modifier = Modifier.height(8.dp))


        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    shareManager.shareLink(link, SharePlatform.TELEGRAM)
                }
            ,
            painter = painterResource(Res.drawable.logo_telegram),
            contentDescription = "Compartir en Telegram"
        )

        Spacer(modifier = Modifier.height(8.dp))


        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    shareManager.shareLink(link, SharePlatform.SMS)
                }
            ,
            painter = painterResource(Res.drawable.logo_sms),
            contentScale = ContentScale.Fit,
            contentDescription = "Enviar por SMS"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    shareManager.shareLink(link, SharePlatform.EMAIL)
                }
            ,
            painter = painterResource(Res.drawable.logo_email),
            contentDescription = "Enviar por Email"
        )

    }
}