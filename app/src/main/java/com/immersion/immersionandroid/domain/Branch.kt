package com.immersion.immersionandroid.domain

data class Branch(val address: String, val companyId: String, val isEnabled: Boolean = false) :
    IEmployeeOwnerShip
