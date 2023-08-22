package com.immersion.immersionandroid.domain

import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Job(
    val name: String,
    val description: String,
    val augmentedImage: AugmentedImage,
    val branchId: String,
    override val id: String
) : IEmployerOwnerShip {
    override fun toString(): String {
        return this.name
    }
}
