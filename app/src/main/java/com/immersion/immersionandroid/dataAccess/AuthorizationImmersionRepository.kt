package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.immersion.LogInMutation
import com.immersion.immersionandroid.domain.User
import com.immersion.type.LoginUserInput

class AuthorizationImmersionRepository(
    private val apolloClient: ApolloClient,
    private val dataStoreRepository: IDataStoreRepository
) : IAuthorizationImmersionRepository {

    override suspend fun logIn(user: User): Boolean? {


        Log.d("TESTING", "voy a hacer log in $user")
        val logInInput = LoginUserInput(password = user.password, email = user.email)
        val mutation = LogInMutation(logInInput)

        return try {
            val response = this.executeMutation(mutation, this.apolloClient)

            if (response != null && !response.hasErrors()) {
                val token = response.data!!.login.accessToken
                Log.d("TESTINGS", "el super token llego $token")
                this.dataStoreRepository.save("token", token)
                return response.data!!.login.user.isBusinessOwner
            } 

            null

        } catch (error: SecurityException) {
            null
        }
    }
}