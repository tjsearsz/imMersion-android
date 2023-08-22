package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query

abstract class ACRUImmersionRepository<T, CD : Mutation.Data, CR, UD : Mutation.Data, UR, RD : Query.Data, RR, RP>(
    protected val apolloClient: ApolloClient
) :
    IImmersionRepository {

    protected abstract fun prepareCreate(entity: T): Mutation<CD>
    protected abstract fun handleCreateResponse(response: ApolloResponse<CD>?): CR

    suspend fun create(entity: T): CR {
        val mutation = this.prepareCreate(entity)
        val response = this.executeMutation(mutation, this.apolloClient)
        return this.handleCreateResponse(response)
    }

    protected abstract fun prepareUpdate(entity: T): Mutation<UD>
    protected abstract fun handleUpdateResponse(response: ApolloResponse<UD>?): UR

    suspend fun update(entity: T): UR {
        val mutation = this.prepareUpdate(entity)
        val response = this.executeMutation(mutation, this.apolloClient)
        return this.handleUpdateResponse(response)
    }

    protected abstract fun prepareRead(entity: RP): Query<RD>
    protected abstract fun handleReadResponse(response: ApolloResponse<RD>?): RR

    suspend fun read(parameters: RP): RR {
        val query = this.prepareRead(parameters)
        val response = this.executeQuery(query, this.apolloClient)
        return this.handleReadResponse(response)
    }
}