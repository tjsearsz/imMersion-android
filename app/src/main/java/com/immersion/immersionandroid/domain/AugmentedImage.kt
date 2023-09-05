package com.immersion.immersionandroid.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.net.URL

@Parcelize
data class AugmentedImage(
    val imageURL: String,
    val modelURL: String,
    val redirectURL: Uri?,
    val scale: Float,
    val summaryScale: Float,
    val summaryX: Float,
    val summaryZ: Float
): Parcelable {

    @IgnoredOnParcel
    lateinit var bitmapImageURL: Bitmap

    fun initializeImageURLtoBitmap(){
        val url = URL(imageURL)
        Log.d("debugging", "casi casi")
        val openStream = url.openStream()
        this.bitmapImageURL = BitmapFactory.decodeStream(openStream)
    }
}
