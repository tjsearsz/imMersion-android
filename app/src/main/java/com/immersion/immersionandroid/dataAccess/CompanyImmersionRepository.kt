package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.Query
import com.immersion.AddCompanyMutation
import com.immersion.GetCompaniesQuery
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.type.CreateCompanyInput

class CompanyImmersionRepository(apolloClient: ApolloClient) :
    ACRUImmersionRepository<Company, AddCompanyMutation.Data, IEmployerOwnerShip?, AddCompanyMutation.Data, Boolean, GetCompaniesQuery.Data, List<IEmployerOwnerShip>, Unit>(
        apolloClient
    ) {
    override fun prepareCreate(entity: Company): Mutation<AddCompanyMutation.Data> {

        val description =
            if (entity.description !== null) Optional.present(entity.description) else Optional.absent()

        val companyInput = CreateCompanyInput(name = entity.name, description = description)
        return AddCompanyMutation(companyInput)
    }

    override fun handleCreateResponse(response: ApolloResponse<AddCompanyMutation.Data>?): IEmployerOwnerShip? {
        if (response !== null && !response.hasErrors()) {
            val (name, description, _id) = response.data!!.createCompany
            return Company(name,description, _id)
        }

        return null
    }

    override fun prepareUpdate(entity: Company): Mutation<AddCompanyMutation.Data> {
        TODO("Not yet implemented")
    }

    override fun handleUpdateResponse(response: ApolloResponse<AddCompanyMutation.Data>?): Boolean {
        TODO("Not yet implemented")
    }



    override fun handleReadResponse(response: ApolloResponse<GetCompaniesQuery.Data>?): List<IEmployerOwnerShip> {
        if (response != null && !response.hasErrors()) {
            return response.data!!.userCompanies.map { company ->
                Company(
                    name = company.name,
                    id = company._id,
                    description = company.description
                )
            }
        }

        return mutableListOf()
    }

    override fun prepareRead(entity: Unit): Query<GetCompaniesQuery.Data> {
        return GetCompaniesQuery()
    }
}