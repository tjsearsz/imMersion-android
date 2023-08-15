package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.exception.ApolloException
import com.immersion.immersionandroid.domain.IEmployeeOwnerShip

abstract class ACRURepository<T: IEmployeeOwnerShip, M: Mutation.Data>(private val apolloClient: ApolloClient) {

    protected abstract fun prepareMutation(entity: T): Mutation<M>

    suspend fun create(entity: T): Boolean{
        return try{
            val preparedMutation = this.prepareMutation(entity)
            val response = this.apolloClient.mutation(preparedMutation).execute()
            return true
        }
        catch(exception: ApolloException){
            exception.printStackTrace()
            return false
        }
    }
}