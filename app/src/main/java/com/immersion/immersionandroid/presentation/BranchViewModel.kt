package com.immersion.immersionandroid.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.immersion.AddBranchMutation
// import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetBranchesQuery
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.dataAccess.FileStackDataSource
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.domain.Branch
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(
    private val filestackRepository: FileStackDataSource,
    private val branchRepository: ACRUImmersionRepository<Branch, AddBranchMutation.Data, IEmployerOwnerShip?, AddBranchMutation.Data, Boolean, GetBranchesQuery.Data, List<IEmployerOwnerShip>, String>
) :
    ViewModel() {

    private val mutableNewBranchAddress = MutableLiveData<LatLng>()

    private val mutableImageBitmap = MutableLiveData<Bitmap?>()

    private val mutableAugmentedImageRedirectURL = MutableLiveData<Uri>()

    private val mutableCompanyId = MutableLiveData<String>()

    fun addImageBitmap(bitmap: Bitmap?) {
        mutableImageBitmap.value = bitmap
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

    suspend fun createBranch(): IEmployerOwnerShip? {

        val byteArra = createByteArrayFromBitmap()
        var response = filestackRepository.uploadImage(byteArra)

        val newAugmentedRealityImage =
            AugmentedImage(
                response.url,
                "",
                mutableImageBitmap.value!!,
                mutableAugmentedImageRedirectURL.value
            )

        val newBranch =
            Branch(
                mutableNewBranchAddress.value!!,
                newAugmentedRealityImage,
                mutableCompanyId.value!!,
                id = ""
            )
        return this.branchRepository.create(newBranch)
    }

    fun changeBranchCoordinates(latLong: LatLng) {
        mutableNewBranchAddress.value = latLong
    }

    suspend fun companyBranches(companyId: String): MutableList<IEmployerOwnerShip> {
        return this.branchRepository.read(companyId).toMutableList()
    }

    fun setCompanyId(companyId: String) {
        this.mutableCompanyId.value = companyId
    }
}