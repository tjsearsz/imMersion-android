package com.immersion.immersionandroid

import android.util.Log
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.immersion.immersionandroid.dataAccess.IDataStoreRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthorizationInterceptor(private val dataStoreRepository: IDataStoreRepository) :
    HttpInterceptor {

    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {


        Log.d("TESTING", "interceptor!")
        var token = mutex.withLock {
            // "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImhvbGEyQGhvbGEuY29tIiwic3ViIjoiNjRiNDY2YmFjMTc3ZGJhYWY4Y2I4YmIwIiwiaWF0IjoxNjkyMjM0OTA1LCJleHAiOjE2OTIyNzA5MDV9.XU-jWDi_46EGUsFQHNEHcy6wn_N34Ia-wmrilAWYQx4"
            val token = dataStoreRepository.get("token")

            Log.d("TESTING", "THE TOKEN $token")

            token
        }

        val response =
            chain.proceed(request.newBuilder().addHeader("Authorization", "Bearer $token").build())

        Log.d("TESTING", response.statusCode.toString())
        Log.d("TESTING", response.body.toString())

        /* return if(response.statusCode == 401){ Since we are usign graphQL, we do not return status codes, so we have to handle errors in the Repositories
             /* token = mutex.withLock {
                 dataStoreRepository.save("token", "")
             }*/

             chain.proceed(request.newBuilder().addHeader("Authorization", "Bearer $token").build())
         }else*/
        return response
    }
}