package com.immersion.immersionandroid.dataAccess

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.immersion.BranchesWithOpenPositionsNearbyQuery
//import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetBranchesQuery
import com.immersion.immersionandroid.domain.AugmentedImage
import dev.romainguy.kotlin.math.scale
import java.net.URL

/*fun GetAllAugmentedImagesQuery.GetAllAugmentedImage.toAugmentedImage(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
    val bitmapImageURL: Bitmap = imageURLToBitmap(imageURL)
    return AugmentedImage(
        imageURL = imageURL,
        modelURL = modelURL,
        bitmapImageURL = bitmapImageURL,
        redirectURL = null
    )
}*/

fun GetBranchesQuery.AugmentedImage.toAugmentedImageAndroid(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
    // val bitmapImageURL: Bitmap = imageURLToBitmap(imageURL)
    return AugmentedImage(
        imageURL = imageURL,
        modelURL = modelURL,
        // bitmapImageURL = bitmapImageURL,
        redirectURL = null,
        0f,
        0f,
        0f,
        0f
    )
}

//TODO: IMPROVE CODE TO AVOID DUPLICATION
fun BranchesWithOpenPositionsNearbyQuery.AugmentedImage.toAugmentedImageAndroid(): AugmentedImage {
    Log.d("debugging", "${modelURL} ${imageURL}")
   // val bitmapImageURL: Bitmap = imageURLToBitmap(imageURL)
    val augmentedImage = AugmentedImage(
        imageURL = imageURL,
        modelURL = modelURL,
        // bitmapImageURL = bitmapImageURL,
        redirectURL = null,
        scale= scale.toFloat(),
        summaryScale = summaryScale.toFloat(),
        summaryX = summaryX.toFloat(),
        summaryZ = summaryZ.toFloat()
    )

    augmentedImage.initializeImageURLtoBitmap()
    return augmentedImage
}

/*public fun imageURLToBitmap(imageURL: String): Bitmap {
    val url = URL(imageURL)
    Log.d("debugging", "casi casi")
    val openStream = url.openStream()
    return BitmapFactory.decodeStream(openStream)
}*/