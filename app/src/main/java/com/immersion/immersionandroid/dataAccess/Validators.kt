package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation

fun <D : Operation.Data> unauthenticatedUser(response: ApolloResponse<D>) {

    if (response.hasErrors()) {
        val errorMessage = response.errors!![0].message

        if (errorMessage == "Unauthorized") {
            Log.d("TESTING", "UNAUTHORIZED!")
            throw SecurityException(errorMessage)
        }
    }
}