package com.immersion.immersionandroid.dataAccess

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.immersionandroid.domain.AugmentedImage
import kotlinx.coroutines.delay
import java.net.URL

class ApolloAugmentedImageClient(//private val apolloClient:ApolloClient
) : IAugmentedImageClient {

    //TODO: Change this to hilt
    private val apolloClient: ApolloClient =
        ApolloClient.Builder().serverUrl("http://10.5.50.146:3000/graphql").build()
    override suspend fun getAugmentedImages(): List<AugmentedImage> {
        Log.d("debugging", "consultado el ar!")
        // delay(5000L)
        /*return listOf(
            AugmentedImage(
                modelURL = "https://cdn.filestackcontent.com/ziq9D8gWRYiJkdGbzW9w",
                imageURL = "https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb",
                bitmapImageURL = imageURLToBitmap("https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb")
            ),
            AugmentedImage(
                modelURL = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW",
                imageURL = "https://cdn.filestackcontent.com/KNit0crBQSm9RrT2bvX9",
                bitmapImageURL = imageURLToBitmap("https://cdn.filestackcontent.com/KNit0crBQSm9RrT2bvX9")
            )
        )*/
        return this.apolloClient.query(GetAllAugmentedImagesQuery())
            .execute().data?.getAllAugmentedImages?.map {
                it.toAugmentedImage()
            } ?: emptyList()
    }

   /* private fun imageURLToBitmap(imageURL: String): Bitmap {
        val url = URL(imageURL)
        Log.d("debugging", "casi casi")
        return BitmapFactory.decodeStream(url.openStream())
    }*/
}