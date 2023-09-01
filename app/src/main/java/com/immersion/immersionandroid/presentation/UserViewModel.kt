package com.immersion.immersionandroid.presentation

import androidx.lifecycle.ViewModel
import com.immersion.immersionandroid.dataAccess.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class UserViewModel @Inject constructor(private val userRepository: IUserRepository) :
    ViewModel() {

    public suspend fun makeUserBusinessOwner(): Boolean {
        return this.userRepository.changeBusinessOwnerStatus(true)
    }
}