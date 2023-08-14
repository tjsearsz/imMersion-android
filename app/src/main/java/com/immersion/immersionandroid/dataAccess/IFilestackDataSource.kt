package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.FileStackResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IFilestackDataSource {

    @Headers("Content-Type: image/png")
    @POST("S3?key=AxoscveBQCOyNyjS5xuAgz") //TODO: check how to add query params
    suspend fun uploadImage(@Body image: RequestBody) : FileStackResponse
}