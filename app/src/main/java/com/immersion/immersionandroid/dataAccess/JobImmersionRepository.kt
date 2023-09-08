package com.immersion.immersionandroid.dataAccess

import android.net.Uri
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.Query
import com.immersion.AddJobMutation
// import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetJobsQuery
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.type.CreateJobInput
import com.immersion.immersionandroid.domain.Job
import com.immersion.type.AugmentedImageInput


//TODO: Remove redirectURL from the job Entity
class JobImmersionRepository(apolloClient: ApolloClient) :
    ACRUImmersionRepository<Job, AddJobMutation.Data, IEmployerOwnerShip?, AddJobMutation.Data, Boolean, GetJobsQuery.Data, List<IEmployerOwnerShip>, String>(
        apolloClient
    ) {

    override fun prepareCreate(entity: Job): Mutation<AddJobMutation.Data> {

        val arRedirectUrl =
            if (entity.redirectURL !== null) Optional.present(entity.redirectURL.toString()) else Optional.absent()

        val createJobInput =
            CreateJobInput(
                entity.branchId,
                entity.description,
                entity.name,
                entity.positions,
                arRedirectUrl
            )
        return AddJobMutation(createJobInput)
    }

    override fun handleCreateResponse(response: ApolloResponse<AddJobMutation.Data>?): IEmployerOwnerShip? {
        if (response !== null && !response.hasErrors()) {
            val (name, _id, description, immediateAncestor, redirectURL, positions) = response.data!!.createJob

            return Job(
                name,
                description,
                immediateAncestor,
                _id,
                if (redirectURL !== null) Uri.parse(redirectURL) else null,
                positions
            )
        }

        return null
    }

    override fun prepareUpdate(entity: Job): Mutation<AddJobMutation.Data> {
        TODO("Not yet implemented")
    }

    override fun handleUpdateResponse(response: ApolloResponse<AddJobMutation.Data>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun prepareRead(entity: String): Query<GetJobsQuery.Data> {
        return GetJobsQuery(entity)
    }

    override fun handleReadResponse(response: ApolloResponse<GetJobsQuery.Data>?): List<IEmployerOwnerShip> {
        if (response != null && !response.hasErrors()) {
            return response.data!!.branchJobs.map { job ->
                Job(
                    name = job.name,
                    id = job._id,
                    description = job.description,
                    branchId = job.immediateAncestor,
                    positions = job.positions,
                    redirectURL = if (job.redirectURL !== null) Uri.parse(job.redirectURL) else null
                )
            }
        }

        return mutableListOf()
    }
}