package com.immersion.immersionandroid.presentation

import androidx.lifecycle.ViewModel
import com.immersion.AddBranchMutation
import com.immersion.immersionandroid.dataAccess.ACRURepository
import com.immersion.immersionandroid.domain.Branch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(private val branchRepository: ACRURepository<Branch, AddBranchMutation.Data>) :
    ViewModel() {

    suspend fun createBranch() {

        val newBranch = Branch("adsfad","asdfa")
        this.branchRepository.create(newBranch)
    }
}