package com.immersion.immersionandroid.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.immersion.AddBranchMutation
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetBranchesQuery
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.domain.Branch
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(private val branchRepository: ACRUImmersionRepository<Branch, AddBranchMutation.Data, IEmployerOwnerShip?, AddBranchMutation.Data, Boolean, GetBranchesQuery.Data, List<IEmployerOwnerShip>, String>) :
    ViewModel() {

    private val mutableNewBranchAddress = MutableLiveData<LatLng>()

    suspend fun createBranch(companyId: String): IEmployerOwnerShip? {

        val newBranch =
            Branch(mutableNewBranchAddress.value!!, companyId, id = "")
        return this.branchRepository.create(newBranch)
    }

    fun changeBranchCoordinates(latLong: LatLng) {
        mutableNewBranchAddress.value = latLong
    }

    suspend fun companyBranches(companyId: String): MutableList<IEmployerOwnerShip> {
        return this.branchRepository.read(companyId).toMutableList()
    }
}