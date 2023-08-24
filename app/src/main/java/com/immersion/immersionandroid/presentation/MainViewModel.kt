package com.immersion.immersionandroid.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.SignInMutation
import com.immersion.UpdateUserMutation
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.dataAccess.IDataStoreRepository
import com.immersion.immersionandroid.dataAccess.IAuthorizationImmersionRepository
import com.immersion.immersionandroid.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: ACRUImmersionRepository<User, SignInMutation.Data, Boolean, UpdateUserMutation.Data, Boolean, GetAllAugmentedImagesQuery.Data, Boolean, Unit>,
    private val authorizationRepository: IAuthorizationImmersionRepository
) :
    ViewModel() {

    private val mutableUserEmail = MutableLiveData<String>()

    private val mutableUserPassword = MutableLiveData<String>()

    private val mutableUser = MutableLiveData<User>()

    suspend fun logIn(email: String, password: String): Boolean? {
        Log.d("TESTING", "EL EMAIL $email y la clave $password")


        return this.authorizationRepository.logIn(
            User(
                email,
                password
            )
        )

    }

    suspend fun registerUser(email: String, password: String): Boolean {

        return try {
            val isCreateUserSuccessful = this.userRepository.create(User(email, password))
            Log.d("TESTING", "EL RESULTADO DE CREAR $isCreateUserSuccessful")
            if (isCreateUserSuccessful) {
                logIn(email, password)
                mutableUserEmail.postValue(email)
                mutableUserPassword.postValue(password)
                mutableUser.postValue(User(email, password))
            }

            isCreateUserSuccessful
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("TESTING", "NOUUUU THAT WASN'T MEAN TOT HAPPEN!")
            false
        }

    }

    suspend fun setUserAsBusinessOwner(): Boolean {
        return try {
            Log.d("TESTING", "el email es: $mutableUserEmail.value!!")
            val user = mutableUser.value!!
            user.isBusinessOwner = true
            this.userRepository.update(user)
        } catch (error: Exception) {
            false
        }
    }
}