package com.immersion.immersionandroid.dataAccess

interface IUserRepository:IImmersionRepository {

    suspend fun changeBusinessOwnerStatus(isBusinessOwner: Boolean): Boolean
}