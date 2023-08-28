package com.immersion.immersionandroid.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.immersion.AddCompanyMutation
// import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetCompaniesQuery
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.dataAccess.ICompanySectorImmersionRepository
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.CompanySector
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val companyRepository: ACRUImmersionRepository<Company, AddCompanyMutation.Data, IEmployerOwnerShip?, AddCompanyMutation.Data, Boolean, GetCompaniesQuery.Data, List<IEmployerOwnerShip>, Unit>,
    private val companySectorRepository: ICompanySectorImmersionRepository
) :
    ViewModel() {

    private val mutableCompanyName = MutableLiveData<String>()
    private val mutableCompanyDescription = MutableLiveData<String?>()
    private val mutableCompanySectorList = MutableLiveData<List<CompanySector>>()

    suspend fun createCompany(companySector: String): IEmployerOwnerShip? {
        val company = Company(
            name = mutableCompanyName.value!!,
            description = mutableCompanyDescription.value,
            id = "",
            companySector = companySector
        )
        return this.companyRepository.create(company)
    }

    fun setCompanyNameAndDescription(name: String, description: String?) {
        this.mutableCompanyName.value = name
        this.mutableCompanyDescription.value = description
    }

    suspend fun getUserCompanies(): MutableList<IEmployerOwnerShip> {

        return this.companyRepository.read(Unit).toMutableList()
    }

    suspend fun getAllCompanySectors(): Boolean {
        Log.d("debugging", "aca voy")
        val companySectorList = this.companySectorRepository.getAllCompanySectors()
        Log.d("debugging", "${companySectorList.size}")
        this.mutableCompanySectorList.postValue(companySectorList)
        Log.d("debugging", "aca me fui")
        if (companySectorList.isEmpty())
            return false
        return true
    }

    public fun retrieveCompanySectors(): List<CompanySector> {
        return this.mutableCompanySectorList.value!!
    }
}