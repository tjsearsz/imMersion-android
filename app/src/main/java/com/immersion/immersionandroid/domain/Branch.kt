package com.immersion.immersionandroid.domain

import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branch(
    val address: LatLng, val companyId: String, val isEnabled: Boolean = false,
    override val id: String
) :
    IEmployerOwnerShip {
    override fun toString(): String {
        return this.id
    }
}
