package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Optional
import com.immersion.AddCompanyMutation
import com.immersion.immersionandroid.domain.Company
import com.immersion.type.CreateCompanyInput

class CompanyRepository(apolloClient: ApolloClient) :
    ACRURepository<Company, AddCompanyMutation.Data>(apolloClient) {
    override fun prepareMutation(entity: Company): Mutation<AddCompanyMutation.Data> {

        val description =
            if (entity.description !== null) Optional.present(entity.description) else Optional.absent()

        val companyInput = CreateCompanyInput(name = entity.name, description = description)
        return AddCompanyMutation(companyInput)
    }

}