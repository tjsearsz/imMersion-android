package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.FileStackResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IFilestackDataSource {

    @Headers("Content-Type: image/png")
    // @POST("S3?key=AxoscveBQCOyNyjS5xuAgz") //TODO: check how to add query params
    // @POST("S3?key=AUepLfeCbQ2C7fx4LnVr4z") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    // @POST("S3?key=AundePne3QKedafJWPQXSz") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    // @POST("S3?key=ADhIULi4SseHZv7Nfq5dhz") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    // @POST("S3?key=AkCxHMxXUT02yEPM9JHoYz") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    // @POST("S3?key=Aq2HR77UnS1mFwpPUzf2az") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    // @POST("S3?key=AUWs9Lfk8Q2yd2b2MIrOUz") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    @POST("S3?key=AghWBvpMEQBKODe3nRPuLz") //TODO: CHANGED THIS BECAUSE I EXHAUSTED THE OTHER ACCOUNT
    suspend fun uploadImage(@Body image: RequestBody) : FileStackResponse
}