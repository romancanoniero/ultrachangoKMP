package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil3.compose.AsyncImage
import com.ashampoo.kim.Kim
import com.ashampoo.kim.model.MetadataUpdate
import com.ashampoo.kim.model.TiffOrientation
import com.iyr.ultrachango.utils.ui.camera_gallery.SharedImage
import com.iyr.ultrachango.utils.ui.camera_gallery.rememberCameraManager
import com.iyr.ultrachango.utils.ui.camera_gallery.rememberGalleryManager
import com.iyr.ultrachango.utils.ui.component.ImageOptionDialog
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.profile_pic


@Composable
fun ProfilePicturePicker(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    permissionsController: PermissionsController,
    image: String? = null,
    onImagePicked: (SharedImage?) -> Unit = {},
    onError: (String) -> Unit = {},

    ) {

    val viewModel: ProfilePicturePickerViewModel = remember { ProfilePicturePickerViewModel() }


    var imageUrl = viewModel.imageUrl.collectAsState()

    var imageAsByteArray = viewModel.imageAsBytArray.collectAsState()
    var launchCamera = viewModel.launchCamera.collectAsState()
    var launchGallery = viewModel.launchGallery.collectAsState()
    var showImagePicker = viewModel.showImagePicker.collectAsState()

    if (showImagePicker.value) {
        ImageOptionDialog(onDismissRequest = {
            viewModel.closeDialog()
        }, onGalleryRequest = {
            viewModel.launchGallery()
        }, onCameraRequest = {
            viewModel.launchCamera()
        })
    }

    if (launchCamera.value || launchGallery.value) {
        if (launchCamera.value) {
            val cameraManager = rememberCameraManager {
                //   imageUrl.value = it?.toString()

                viewModel.setImageUrl(it?.getUri() ?: "")
                scope.launch(Dispatchers.Default) {
                    onImagePicked(it)
                }
                viewModel.hide()
            }


            scope.launch {
                try {
                    permissionsController.providePermission(Permission.CAMERA)
                    cameraManager.launch()
                } catch (deniedAlways: DeniedAlwaysException) {
                    // Permission is always denied.
                    permissionsController.openAppSettings()
               } catch (denied: DeniedException) {
                    // Permission was denied.
                }
            }

        } else if (launchGallery.value) {
            val galleryManager = rememberGalleryManager {

                viewModel.setImageUrl(it?.getUri() ?: "")

                scope.launch(Dispatchers.Default) {
                    onImagePicked(it)
                }

                viewModel.hide()
            }

            scope.launch {
                try {
                    permissionsController.providePermission(Permission.GALLERY)
                    galleryManager.launch()
                } catch (deniedAlways: DeniedAlwaysException) {
                    // Permission is always denied.
                    permissionsController.openAppSettings()
                } catch (denied: DeniedException) {
                    // Permission was denied.
                }
            }

        }
    }


    image?.let { data ->
        Column(modifier = Modifier.fillMaxWidth())
        {

            AsyncImage(
                modifier = modifier.fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .shadow(4.dp, CircleShape, clip = true)
                    .clickable {
                        viewModel.showImagePicker()
                    },
                contentScale = ContentScale.Crop,
                model = data,
                contentDescription = ""
            )
        }
    } ?: run {
        Image(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
                .shadow(4.dp, CircleShape)
                .clickable {
                    viewModel.showImagePicker()
                },
            painter = painterResource(Res.drawable.profile_pic),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

    }


}


class ProfilePicturePickerViewModel : ViewModel() {


    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    private val _imageAsBytArray = MutableStateFlow<ByteArray?>(null)
    val imageAsBytArray = _imageAsBytArray.asStateFlow()


    private val _showImagePicker = MutableStateFlow<Boolean>(false)
    val showImagePicker = _showImagePicker.asStateFlow()

    private val _launchCamera = MutableStateFlow<Boolean>(false)
    val launchCamera = _launchCamera.asStateFlow()

    private val _launchGallery = MutableStateFlow<Boolean>(false)
    val launchGallery = _launchGallery.asStateFlow()

    fun launchGallery() {
        _launchGallery.value = true
    }


    fun launchCamera() {
        _launchCamera.value = true
    }

    fun showImagePicker() {
        _showImagePicker.value = true
    }

    fun hide() {
        _launchGallery.value = false
        _launchCamera.value = false
        _showImagePicker.value = false
    }

    fun setByteArray(toByteArray: ByteArray?) {
        _imageAsBytArray.value = toByteArray
    }

    fun closeDialog() {
        _showImagePicker.value = false
    }

    fun setImageUrl(location: String) {
        _imageUrl.value = location
    }
}



