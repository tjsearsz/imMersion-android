package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.User

interface IAuthorizationImmersionRepository: IImmersionRepository {
    suspend fun logIn(user: User): Boolean?
}