package com.immersion.immersionandroid.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.immersion.immersionandroid.dataAccess.FileStackDataSource
import com.immersion.immersionandroid.dataAccess.IJobRepository
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.domain.Job
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import android.net.Uri
import android.util.Patterns
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val filestackRepository: FileStackDataSource,
    private val jobRepository: IJobRepository,
    @ApplicationContext val context: Context
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

    suspend fun createNewJob() {

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
            null,
            newAugmentedRealityImage
        )
        this.jobRepository.createJob(newJob)
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

}