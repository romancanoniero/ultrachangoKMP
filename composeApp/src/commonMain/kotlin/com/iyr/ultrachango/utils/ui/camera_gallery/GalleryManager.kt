@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.iyr.ultrachango.utils.ui.camera_gallery

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryManager(onResult:  (SharedImage?) -> Unit): GalleryManager


expect class GalleryManager(
    onLaunch: () -> Unit
) {
    fun launch()
}

