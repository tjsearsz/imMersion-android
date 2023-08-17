package com.immersion.immersionandroid.domain

import com.google.android.gms.maps.model.LatLng

data class Branch(val address: LatLng, val companyId: String, val isEnabled: Boolean = false) :
    IEmployeeOwnerShip
