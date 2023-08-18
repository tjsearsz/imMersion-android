package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.exception.ApolloException

abstract class ACRURepository<T, M : Mutation.Data, C, U, UD : Mutation.Data>(protected val apolloClient: ApolloClient) :
    IRepository {

    protected abstract fun prepareCreate(entity: T): Mutation<M>
    protected abstract fun handleCreateResponse(response: ApolloResponse<M>?): C

    suspend fun create(entity: T): C {
        val mutation = this.prepareCreate(entity)
        val response = this.executeMutation(mutation, this.apolloClient)
        return this.handleCreateResponse(response)
    }

    protected abstract fun prepareUpdate(entity: T): Mutation<UD>
    protected abstract fun handleUpdateResponse(response: ApolloResponse<UD>?): U

    suspend fun update(entity: T): U {
        val mutation = this.prepareUpdate(entity)
        val response = this.executeMutation(mutation, this.apolloClient)
        return this.handleUpdateResponse(response)
    }

    /*suspend fun update(entity: T): Boolean {
        return try {

            val
            unauthenticatedUser()
            true
        }
        catch(exception: ApolloException){
            false
        }
    }*/
}