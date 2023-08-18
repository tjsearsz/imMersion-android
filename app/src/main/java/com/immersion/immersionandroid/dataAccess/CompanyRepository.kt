package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Optional
import com.immersion.AddBranchMutation
import com.immersion.AddCompanyMutation
import com.immersion.immersionandroid.domain.Branch
import com.immersion.immersionandroid.domain.Company
import com.immersion.type.CreateCompanyInput

class CompanyRepository(apolloClient: ApolloClient) :
    ACRURepository<Company, AddCompanyMutation.Data, Boolean, Boolean, AddCompanyMutation.Data>(apolloClient) {
    override fun prepareCreate(entity: Company): Mutation<AddCompanyMutation.Data> {

        val description =
            if (entity.description !== null) Optional.present(entity.description) else Optional.absent()

        val companyInput = CreateCompanyInput(name = entity.name, description = description)
        return AddCompanyMutation(companyInput)
    }

    override fun handleCreateResponse(response: ApolloResponse<AddCompanyMutation.Data>?): Boolean {
        if (response !== null && !response.hasErrors()) {
            return true
        }

        return false
    }

    override fun prepareUpdate(entity: Company): Mutation<AddCompanyMutation.Data> {
        TODO("Not yet implemented")
    }

    override fun handleUpdateResponse(response: ApolloResponse<AddCompanyMutation.Data>?): Boolean {
        TODO("Not yet implemented")
    }
}