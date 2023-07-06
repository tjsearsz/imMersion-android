package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.immersionandroid.domain.AugmentedImage

fun GetAllAugmentedImagesQuery.GetAllAugmentedImages.toAugmentedImage(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
    return AugmentedImage(imageURL = imageURL, modelURL = modelURL)
}