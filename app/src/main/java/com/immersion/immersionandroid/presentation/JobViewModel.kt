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
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetJobsQuery
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val filestackRepository: FileStackDataSource,
    private val jobRepository: ACRUImmersionRepository<Job, AddJobMutation.Data, IEmployerOwnerShip?, AddJobMutation.Data, Boolean, GetJobsQuery.Data, List<IEmployerOwnerShip>, String>,
    @ApplicationContext val context: Context //TODO: CHECK THIS MAYBE IT IS NOT NEEDED ANYMORE
) :
    ViewModel() {


    private val mutableNewJobName = MutableLiveData<String>()

    private val mutableNewJobDescription = MutableLiveData<String>()

    private val mutableImageBitmap = MutableLiveData<Bitmap?>()

    private val mutableAugmentedImageRedirectURL = MutableLiveData<Uri>()


    fun addJobNameAndDescription(jobName: String, jobDescription: String) {
        mutableNewJobName.value = jobName
        mutableNewJobDescription.value = jobDescription
    }

    fun addImageBitmap(bitmap: Bitmap?) {
        mutableImageBitmap.value = bitmap
    }

    suspend fun createNewJob(branchId: String): IEmployerOwnerShip? {

        val byteArra = createByteArrayFromBitmap()
        var response = filestackRepository.uploadImage(byteArra)

        val newAugmentedRealityImage =
            AugmentedImage(
                response.url,
                "http://google.com",
                mutableImageBitmap.value!!,
                mutableAugmentedImageRedirectURL.value
            )
        val newJob = Job(
            mutableNewJobName.value!!,
            mutableNewJobDescription.value!!,
            newAugmentedRealityImage,
            branchId,
            ""
        )
        return this.jobRepository.create(newJob)
    }

    fun validateUrl(intendedUrl: String): Boolean {
        if (Patterns.WEB_URL.matcher(intendedUrl).matches()) {
            mutableAugmentedImageRedirectURL.value = Uri.parse(intendedUrl)
            return true
        }
        return false
    }

    private fun createByteArrayFromBitmap(): ByteArray {

        val bitmap = mutableImageBitmap.value
        val bos = ByteArrayOutputStream();
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray();

        return bitmapdata
    }

    suspend fun branchJobs(branchId: String): MutableList<IEmployerOwnerShip> {
        return this.jobRepository.read(branchId).toMutableList()
    }

}