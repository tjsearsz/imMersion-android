package com.immersion.immersionandroid.dataAccess

import com.google.android.gms.maps.model.LatLng
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.domain.IEmployerOwnerShip

interface IAugmentedImageRepository: IImmersionRepository {
    // suspend fun getAugmentedImages(): List<AugmentedImage>

    suspend fun getAugmentedImagesNearbyCoordinates(coordinates: LatLng): LinkedHashMap<AugmentedImage, List<IEmployerOwnerShip>> // List<AugmentedImage>
}