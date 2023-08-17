package com.immersion.immersionandroid.domain

import java.net.URI

data class Job(
    val name: String,
    val description: String,
    val redirectURL: URI?,
    val augmentedImage: AugmentedImage,
    val branchId: String
)
