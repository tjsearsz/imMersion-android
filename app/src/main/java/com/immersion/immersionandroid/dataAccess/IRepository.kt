package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.exception.ApolloException

interface IRepository {
    suspend fun <M : Mutation.Data> executeMutation(
        mutation: Mutation<M>,
        apolloClient: ApolloClient
    ): ApolloResponse<M>? {
        return try {
            // val preparedMutation = this.prepareMutation(entity)
            val response = apolloClient.mutation(mutation).execute()

            unauthenticatedUser(response)

            response
        } catch (exception: ApolloException) {
            null
        }
    }
}