package com.immersion.immersionandroid.dataAccess

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.immersionandroid.domain.AugmentedImage
import java.net.URL

fun GetAllAugmentedImagesQuery.GetAllAugmentedImage.toAugmentedImage(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
    val bitmapImageURL: Bitmap = imageURLToBitmap(imageURL)
    return AugmentedImage(imageURL = imageURL, modelURL = modelURL, bitmapImageURL = bitmapImageURL)
}

private fun imageURLToBitmap(imageURL: String): Bitmap{
    val url = URL(imageURL)
    Log.d("debugging", "casi casi")
    return BitmapFactory.decodeStream(url.openStream())
}