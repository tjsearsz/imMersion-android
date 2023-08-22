package com.immersion.immersionandroid.dataAccess

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetJobsQuery
import com.immersion.immersionandroid.domain.AugmentedImage
import java.net.URL

fun GetAllAugmentedImagesQuery.GetAllAugmentedImage.toAugmentedImage(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
    val bitmapImageURL: Bitmap = imageURLToBitmap(imageURL)
    return AugmentedImage(
        imageURL = imageURL,
        modelURL = modelURL,
        bitmapImageURL = bitmapImageURL,
        redirectURL = null
    )
}

fun GetJobsQuery.AugmentedImage.toAugmentedImageAndroid(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
    val bitmapImageURL: Bitmap = imageURLToBitmap(imageURL)
    return AugmentedImage(
        imageURL = imageURL,
        modelURL = modelURL,
        bitmapImageURL = bitmapImageURL,
        redirectURL = null
    )
}

public fun imageURLToBitmap(imageURL: String): Bitmap {
    val url = URL(imageURL)
    Log.d("debugging", "casi casi")
    val openStream = url.openStream()
    return BitmapFactory.decodeStream(openStream)
}