package com.immersion.immersionandroid.domain

import android.graphics.Bitmap
import android.net.Uri

data class AugmentedImage(
    val imageURL: String,
    val modelURL: String,
    val bitmapImageURL: Bitmap,
    val redirectURL: Uri?
)
