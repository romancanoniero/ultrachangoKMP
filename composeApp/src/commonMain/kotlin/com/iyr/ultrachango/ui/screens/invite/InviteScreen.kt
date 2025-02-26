package com.iyr.ultrachango.ui.screens.invite


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iyr.ultrachango.ui.screens.qrscanner.QRTypes
import com.iyr.ultrachango.utils.ui.device.CopyableLink
import com.iyr.ultrachango.utils.ui.device.shared.ShareButtons
import com.iyr.ultrachango.utils.ui.elements.Body1Text
import com.iyr.ultrachango.utils.ui.elements.H1Text
import com.iyr.ultrachango.utils.ui.elements.H3Text
import com.iyr.ultrachango.viewmodels.InviteViewModel
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.rememberQrKitPainter


@Composable
fun InviteScreen(
    navController: NavController,
    qrType: QRTypes,
    inputText: String = "Ultrachango.app.link",
    viewModel: InviteViewModel = koinViewModel(),
) {
    val inviteLink by viewModel.inviteLink.collectAsState()

    val painter = rememberQrKitPainter(inputText)


    LaunchedEffect(Unit) {
        viewModel.generateInviteLink("12345")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(Modifier.height(8.dp))

        when (qrType) {
            QRTypes.USER -> {
                H1Text("Invita a tus amigos")
            }
            QRTypes.FAMILY_MEMBER -> {
                H1Text("Invita a tus familiares")
            }
            QRTypes.SHOPPING_LIST -> {
                H1Text("Invita a tus amigos")
            }
        }

        Spacer(Modifier.height(8.dp))

        when (qrType) {
            QRTypes.USER -> {
                H3Text(text = "Invita a tus amigos a Ultrachango mediante la camara de tu celular; Para ello , deben activar su camara y escanear el codigo QR que se muestra a continuación")
            }
            QRTypes.FAMILY_MEMBER -> {
                H3Text(text ="Invita a tus familiares a formar parte de tu Grupo Familiar")
            }
           else -> {}
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        )
        {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(.70f)
                    .aspectRatio(1f)
            )

        }

        Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

        Text("Comparte tu enlace de invitación")

        Spacer(Modifier.height(8.dp))

        CopyableLink(inputText)




        if (inviteLink.isNotEmpty()) {
            val qrPainter = rememberQrKitPainter(data = inviteLink)
            Image(
                painter = qrPainter,
                contentDescription = "Código QR de invitación",
                modifier = Modifier.size(200.dp)
            )
        }



        Spacer(modifier = Modifier.fillMaxWidth().weight(1f))


        SharedButtonsSection(inputText)


    }
}

@Composable
fun SharedButtonsSection(inputText: String) {

    Column(
        verticalArrangement = Arrangement.Center,
    ) {


        Row {
            androidx.compose.material.Divider(
                modifier = Modifier.weight(1f).align(
                    Alignment.CenterVertically
                ),
                color = Color.Gray,
                thickness = 1.dp,
            )

            Body1Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                "O invita mediante "
            )

            androidx.compose.material.Divider(
                modifier = Modifier.weight(1f).align(
                    Alignment.CenterVertically
                ),
                color = Color.Gray,
                thickness = 1.dp,
            )

        }




        ShareButtons(inputText)

    }

}
