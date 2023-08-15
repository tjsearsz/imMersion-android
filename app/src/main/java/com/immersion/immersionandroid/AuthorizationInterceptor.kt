package com.immersion.immersionandroid

import android.util.Log
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthorizationInterceptor : HttpInterceptor {

    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {

        //TODO: IMPROVE INTERCEPTOR
        Log.d("TESTING", "interceptor!")
        var token = mutex.withLock {
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImhvbGEyQGhvbGEuY29tIiwic3ViIjoiNjRiNDY2YmFjMTc3ZGJhYWY4Y2I4YmIwIiwiaWF0IjoxNjkyMTA2Nzk2LCJleHAiOjE2OTIxNDI3OTZ9.WkMdNBQecaVgZjWod5tnyOLxWL5G_39gu1ItWlHfLek"
        }

        val response =
            chain.proceed(request.newBuilder().addHeader("Authorization", "Bearer $token").build())

        return response
    }
}