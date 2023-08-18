package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.immersion.LogInMutation
import com.immersion.immersionandroid.domain.User
import com.immersion.type.LoginUserInput

class AuthorizationRepository(private val apolloClient: ApolloClient) : IAuthorizationRepository {

    override suspend fun logIn(user: User): String {


        Log.d("TESTING","voy a hacer log in $user")
        val logInInput = LoginUserInput(password = user.password, email = user.email)
        val mutation = LogInMutation(logInInput)

        val unauthenticatedResponse = ""
        return try {
            val response = this.executeMutation(mutation, this.apolloClient)

            if (response != null)
                return response.data!!.login.accessToken

            unauthenticatedResponse

        } catch (error: SecurityException) {
            unauthenticatedResponse
        }
    }
}