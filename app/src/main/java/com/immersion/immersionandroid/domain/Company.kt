package com.immersion.immersionandroid.domain

import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(val name: String, val description: String?, override val id: String) :
    IEmployerOwnerShip {

    //TODO: This should be added when doing the get
    /*private lateinit var branchList: ArrayList<IEmployeeOwnerShip>

    public fun addBranches(branches: ArrayList<IEmployeeOwnerShip>){
        this.branchList = branches
    }*/

    override fun toString(): String {
        return this.name
    }
}
