package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.FileStackResponse
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class FileStackDataSource(private val fileStackAPI: IFilestackDataSource) {

    suspend fun uploadImage(file: ByteArray): FileStackResponse {
        try {

            val requestBody: RequestBody = file.toRequestBody()
            return fileStackAPI.uploadImage(
                requestBody
            )

        } catch (ex: IOException) {
            ex.printStackTrace()
            throw ex
        } catch (ex: HttpException) {
            ex.printStackTrace()
            throw ex
        }
    }
}