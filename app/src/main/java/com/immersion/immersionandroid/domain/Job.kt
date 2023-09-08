package com.immersion.immersionandroid.domain

import android.net.Uri
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Job(
    val name: String,
    val description: String,
    val branchId: String,
    override val id: String,
    val redirectURL: Uri?,
    val positions: Int
) : IEmployerOwnerShip {
    override fun toString(): String {
        return this.name
    }
}
