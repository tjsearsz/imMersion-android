package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.google.android.gms.maps.model.LatLng
import com.immersion.BranchesWithOpenPositionsNearbyQuery
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.LogInMutation
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.type.LoginUserInput

class AugmentedImageRepository(private val apolloClient:ApolloClient
) : IAugmentedImageRepository {

    //TODO: Change this to hilt
   // private val apolloClient: ApolloClient =
     //   ApolloClient.Builder().serverUrl("http://10.5.48.68:3000/graphql").build()
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

    override suspend fun getAugmentedImagesNearbyCoordinates(coordinates: LatLng): List<AugmentedImage>{

        val query = BranchesWithOpenPositionsNearbyQuery(listOf(coordinates.longitude, coordinates.latitude))

        return try {
            val response = this.executeQuery(query, this.apolloClient)

            if (response != null)
                return response.data!!.branchesWithOpenPositionsNearby.map {
                    it.augmentedImage.toAugmentedImageAndroid()
                }

            emptyList()

        } catch (error: SecurityException) {
            emptyList()
        }
    }
}