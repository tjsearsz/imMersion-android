package com.immersion.immersionandroid.dataAccess

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.google.android.gms.maps.model.LatLng
import com.immersion.AddBranchMutation
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetBranchesQuery
import com.immersion.immersionandroid.domain.Branch
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.type.CreateBranchInput

class BranchImmersionRepository(apolloClient: ApolloClient) :
    ACRUImmersionRepository<Branch, AddBranchMutation.Data, IEmployerOwnerShip?, AddBranchMutation.Data, Boolean, GetBranchesQuery.Data, List<IEmployerOwnerShip>, String>(
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

    override fun handleCreateResponse(response: ApolloResponse<AddBranchMutation.Data>?): IEmployerOwnerShip? {
        if (response !== null && !response.hasErrors()) {
            val (address, isEnabled, _id, immediateAncestor) = response.data!!.createBranch
            val finalAddress = LatLng(address[1], address[0])
            return Branch(finalAddress, immediateAncestor, isEnabled, _id)
        }

        return null
    }

    override fun prepareUpdate(entity: Branch): Mutation<AddBranchMutation.Data> {
        TODO("Not yet implemented")
    }

    override fun handleUpdateResponse(response: ApolloResponse<AddBranchMutation.Data>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun prepareRead(entity: String): Query<GetBranchesQuery.Data> {
        return GetBranchesQuery(entity)
    }

    override fun handleReadResponse(response: ApolloResponse<GetBranchesQuery.Data>?): List<IEmployerOwnerShip> {
        if (response != null && !response.hasErrors()) {
            return response.data!!.companyBranches.map { branch ->
                Branch(
                    companyId = branch.immediateAncestor,
                    isEnabled = branch.isEnabled,
                    id = branch._id,
                    address = LatLng(branch.address[1], branch.address[0])
                )
            }
        }

        return mutableListOf()
    }
}