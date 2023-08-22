package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.exception.ApolloException

interface IImmersionRepository {
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

    suspend fun <Q : Query.Data> executeQuery(
        query: Query<Q>,
        apolloClient: ApolloClient
    ): ApolloResponse<Q>? {
        return try {
            // val preparedMutation = this.prepareMutation(entity)
            val response = apolloClient.query(query).execute()

            unauthenticatedUser(response)

            response
        } catch (exception: ApolloException) {
            null
        }
    }
}