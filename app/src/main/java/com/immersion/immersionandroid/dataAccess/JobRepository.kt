package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.http.HttpHeader
import com.apollographql.apollo3.exception.ApolloException
import com.immersion.AddJobMutation
import com.immersion.type.CreateJobInput
import com.immersion.immersionandroid.domain.Job
import com.immersion.type.AugmentedImageInput


//TODO: Remove redirectURL from the job Entity
class JobRepository(private val apolloClient: ApolloClient) : IJobRepository {
    override suspend fun createJob(job: Job): Boolean {
        return try {

            val arRedirectUrl =
                if (job.augmentedImage.redirectURL !== null) Optional.present(job.augmentedImage.redirectURL.toString()) else Optional.absent()

            val imageInput = AugmentedImageInput(
                job.augmentedImage.imageURL,
                job.augmentedImage.modelURL,
                arRedirectUrl
            )

            val createJobInput =
                CreateJobInput(imageInput, job.branchId, job.description, job.name)
            val jobInput = AddJobMutation(createJobInput)

            val response = this.apolloClient.mutation(jobInput).execute()

            //TODO: IMPROVE HOW TO HANDLE THE ERROR'S OBJECT
            /*for(x in response.errors!!){
                Log.d("TESTING",x.message)
                Log.d("TESTING", x.toString())
            }*/
            true
        } catch (error: ApolloException) {
            error.printStackTrace()
            false
        }
    }
}