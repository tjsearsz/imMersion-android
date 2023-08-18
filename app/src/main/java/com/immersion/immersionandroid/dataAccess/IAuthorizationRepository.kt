package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.User

interface IAuthorizationRepository: IRepository {
    suspend fun logIn(user: User): String
}