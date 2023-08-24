package com.immersion.immersionandroid.dataAccess

import com.google.android.gms.maps.model.LatLng
import com.immersion.immersionandroid.domain.AugmentedImage

interface IAugmentedImageRepository: IImmersionRepository {
    suspend fun getAugmentedImages(): List<AugmentedImage>

    suspend fun getAugmentedImagesNearbyCoordinates(coordinates: LatLng): List<AugmentedImage>
}