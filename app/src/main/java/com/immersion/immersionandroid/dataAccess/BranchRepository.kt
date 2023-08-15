package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.immersion.AddBranchMutation
import com.immersion.immersionandroid.domain.Branch
import com.immersion.type.CreateBranchInput

class BranchRepository(apolloClient: ApolloClient) : ACRURepository<Branch, AddBranchMutation.Data>(
    apolloClient
) {
    override fun prepareMutation(entity: Branch): Mutation<AddBranchMutation.Data> {

        val data = CreateBranchInput(address = entity.address, companyId = entity.companyId)
        return AddBranchMutation(data)

    }
}