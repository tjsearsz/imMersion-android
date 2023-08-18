package com.immersion.immersionandroid.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.immersion.AddBranchMutation
import com.immersion.immersionandroid.dataAccess.ACRURepository
import com.immersion.immersionandroid.domain.Branch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(private val branchRepository: ACRURepository<Branch, AddBranchMutation.Data, Boolean, Boolean, AddBranchMutation.Data>) :
    ViewModel() {

    private val mutableNewBranchAddress = MutableLiveData<LatLng>()

    suspend fun createBranch() {

        val newBranch = Branch(mutableNewBranchAddress.value!!, "64dad7b221c653f112edbfd2")
        this.branchRepository.create(newBranch)
    }

    fun changeBranchCoordinates(latLong: LatLng) {
        mutableNewBranchAddress.value = latLong
    }
}