package com.iyr.ultrachango.ui.screens.qrscanner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iyr.ultrachango.beep
import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.RecordType
import com.iyr.ultrachango.data.models.UserAsFamilyMember
import com.iyr.ultrachango.ui.ScaffoldViewModel
import com.iyr.ultrachango.ui.rootnavigation.RootRoutes
import com.iyr.ultrachango.utils.extensions.isEmail
import com.iyr.ultrachango.utils.extensions.isValidMobileNumber
import com.iyr.ultrachango.utils.helpers.getProfileImageURL
import com.iyr.ultrachango.utils.ui.elements.UserPictureRegular
import com.iyr.ultrachango.utils.ui.elements.MySearchTexField
import com.iyr.ultrachango.utils.ui.elements.buttonShapeBig
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner


enum class QRTypes {
    SHOPPING_LIST,
    FAMILY_MEMBER,
    USER
}
@Composable
fun QRScannerScreen(
    listId: Int? = null,
    navController: NavHostController,
    scaffoldVM: ScaffoldViewModel,
    userViewModel: UserViewModel = koinViewModel(),
    onSuccess: (QRTypes,String) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        QrScanner(
            modifier = Modifier.fillMaxSize(),
            flashlightOn = false,
            cameraLens = CameraLens.Back,
            openImagePicker = false,
            onCompletion = { result ->
                beep()
                navController.popBackStack()
                onSuccess(QRTypes.USER,result)
                println("QR Code result: $result")


            },
            imagePickerHandler ={false} ,
            onFailure = {
                println("QR Code scan failed")
            },
            overlayShape = OverlayShape.Square

        )
    }


}



