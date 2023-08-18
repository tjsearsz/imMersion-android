package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.immersion.AddBranchMutation
import com.immersion.immersionandroid.domain.Branch
import com.immersion.type.CreateBranchInput

class BranchRepository(apolloClient: ApolloClient) :
    ACRURepository<Branch, AddBranchMutation.Data, Boolean, Boolean, AddBranchMutation.Data>(
        apolloClient
    ) {
    override fun prepareCreate(entity: Branch): Mutation<AddBranchMutation.Data> {

        val data = CreateBranchInput(
            address = listOf(
                entity.address.longitude,
                entity.address.latitude
            ), companyId = entity.companyId
        )
        return AddBranchMutation(data)

    }

    override fun handleCreateResponse(response: ApolloResponse<AddBranchMutation.Data>?): Boolean {
         if (response !== null && !response.hasErrors()) {
             return true
         }

         return false
     }

    override fun prepareUpdate(entity: Branch): Mutation<AddBranchMutation.Data> {
        TODO("Not yet implemented")
    }

    override fun handleUpdateResponse(response: ApolloResponse<AddBranchMutation.Data>?): Boolean {
        TODO("Not yet implemented")
    }
}