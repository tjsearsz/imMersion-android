package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.immersionandroid.domain.AugmentedImage
import kotlinx.coroutines.delay

class ApolloAugmentedImageClient(//private val apolloClient:ApolloClient
): IAugmentedImageClient {

    private val apolloClient: ApolloClient =
        ApolloClient.Builder().serverUrl("http://10.5.50.146:3000/graphql").build()

    override suspend fun getAugmentedImages(): AugmentedImage {
        Log.d("debugging", "consultado el ar!")
        return this.apolloClient.query(GetAllAugmentedImagesQuery()).execute().data?.getAllAugmentedImages!!.toAugmentedImage()
    }
}