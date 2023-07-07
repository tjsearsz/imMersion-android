package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.AugmentedImage

interface IAugmentedImageClient {
    suspend fun getAugmentedImages(): List<AugmentedImage>
}