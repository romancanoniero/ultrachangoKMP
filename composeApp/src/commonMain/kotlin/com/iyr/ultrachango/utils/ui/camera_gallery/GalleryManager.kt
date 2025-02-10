package com.iyr.ultrachango.utils.ui.camera_gallery

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryManager(onResult:  (SharedImage?) -> Unit): GalleryManager


expect class GalleryManager(
    onLaunch: () -> Unit
) {
    fun launch()
}

