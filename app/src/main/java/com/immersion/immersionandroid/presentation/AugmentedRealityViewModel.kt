package com.immersion.immersionandroid.presentation

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.immersion.immersionandroid.dataAccess.IAugmentedImageRepository
import com.immersion.immersionandroid.domain.AugmentedImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AugmentedRealityViewModel @Inject constructor(private val augmentedImageRepository: IAugmentedImageRepository): ViewModel() {

    /*private val _state: MutableStateFlow<AugmentedImagesState> = MutableStateFlow(AugmentedImagesState())
    val state: StateFlow<AugmentedImagesState> = _state.asStateFlow()

    init {
        Log.d("golazo", "supe megagaga")
        viewModelScope.launch {
            val result = apolloAugmentedImageClient.getAugmentedImages()
           // Log.d("metodo", result.modelURL)
            // _state.update { it.copy(augmentedImage = result) }
        }
    }

    data class AugmentedImagesState(val augmentedImage: AugmentedImage? = null)*/

    suspend fun getAugmentedRealitiesInOpenPositionsNearby(coordinates: LatLng): List<AugmentedImage>{
        return this.augmentedImageRepository.getAugmentedImagesNearbyCoordinates(coordinates)
    }

}