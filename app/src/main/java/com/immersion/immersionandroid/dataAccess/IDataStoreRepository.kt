package com.immersion.immersionandroid.dataAccess

interface IDataStoreRepository {
    suspend fun get(key: String): String?

    suspend fun save(key: String, value: String)

}