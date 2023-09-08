package com.immersion.immersionandroid.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.immersion.immersionandroid.dataAccess.FileStackDataSource
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.domain.Job
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import android.net.Uri
import android.util.Patterns
import androidx.fragment.app.activityViewModels
import com.immersion.AddJobMutation
// import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetJobsQuery
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: ACRUImmersionRepository<Job, AddJobMutation.Data, IEmployerOwnerShip?, AddJobMutation.Data, Boolean, GetJobsQuery.Data, List<IEmployerOwnerShip>, String>,
    @ApplicationContext val context: Context //TODO: CHECK THIS MAYBE IT IS NOT NEEDED ANYMORE
) :
    ViewModel() {


    private val mutableNewJobName = MutableLiveData<String>()

    private val mutableNewJobDescription = MutableLiveData<String>()

    private val mutableNewPositions = MutableLiveData<Int>()

    private val mutableAugmentedImageRedirectURL = MutableLiveData<Uri?>()


    fun addJobInformation(
        jobName: String,
        jobDescription: String,
        positions: Int
    ) {
        mutableNewJobName.value = jobName
        mutableNewJobDescription.value = jobDescription
        mutableNewPositions.value = positions
    }

    fun validateUrl(intendedUrl: String): Boolean {
        if (Patterns.WEB_URL.matcher(intendedUrl).matches()) {
            mutableAugmentedImageRedirectURL.value = Uri.parse(intendedUrl)
            return true
        }
        return false
    }


    suspend fun createNewJob(branchId: String): IEmployerOwnerShip? {


        val newJob = Job(
            mutableNewJobName.value!!,
            mutableNewJobDescription.value!!,
            branchId,
            "",
            mutableAugmentedImageRedirectURL.value!!,
            mutableNewPositions.value!!
        )
        return this.jobRepository.create(newJob)
    }

    suspend fun branchJobs(branchId: String): MutableList<IEmployerOwnerShip> {
        return this.jobRepository.read(branchId).toMutableList()
    }

}