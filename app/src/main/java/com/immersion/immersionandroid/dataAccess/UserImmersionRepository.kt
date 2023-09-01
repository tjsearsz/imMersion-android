package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.Query
import com.immersion.ChangeUserBusinessOwnerStatusMutation
import com.immersion.GetBranchesQuery
// import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.SignInMutation
import com.immersion.UpdateUserMutation
import com.immersion.immersionandroid.domain.User
import com.immersion.type.CreateUserInput
import com.immersion.type.UpdateUserInput

class UserImmersionRepository(apolloClient: ApolloClient) :
    ACRUImmersionRepository<User, SignInMutation.Data, Boolean, UpdateUserMutation.Data, Boolean, GetBranchesQuery.Data, Boolean, Unit>(
        apolloClient
    ), IUserRepository {

    override fun prepareCreate(entity: User): Mutation<SignInMutation.Data> {
        Log.d("TESTING", "VOY A INSERTAR $entity.email $entity.password")
        val createUserInput = CreateUserInput(entity.email, Optional.absent(), entity.password)

        return SignInMutation(createUserInput)
    }

    override fun handleCreateResponse(response: ApolloResponse<SignInMutation.Data>?): Boolean {
        if (response !== null && !response.hasErrors()) {
            return true
        }

        return false
    }

    override fun prepareUpdate(entity: User): Mutation<UpdateUserMutation.Data> {
        val updateUserInput = UpdateUserInput(entity.email, entity.isBusinessOwner)
        return UpdateUserMutation(updateUserInput)
    }

    override fun handleUpdateResponse(response: ApolloResponse<UpdateUserMutation.Data>?): Boolean {
        if (response !== null && !response.hasErrors()) {
            return true
        }

        return false
    }

    override fun prepareRead(parameters: Unit): Query<GetBranchesQuery.Data> {
        TODO("Not yet implemented")
    }

    override fun handleReadResponse(response: ApolloResponse<GetBranchesQuery.Data>?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun changeBusinessOwnerStatus(isBusinessOwner: Boolean): Boolean {

        val mutation = ChangeUserBusinessOwnerStatusMutation(isBusinessOwner)

        return try {
            this.executeMutation(mutation, apolloClient)
            true
        } catch (error: Exception) {
            false
        }

    }


}