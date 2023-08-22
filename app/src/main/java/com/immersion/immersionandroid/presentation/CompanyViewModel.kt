package com.immersion.immersionandroid.presentation

import androidx.lifecycle.ViewModel
import com.immersion.AddCompanyMutation
import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetCompaniesQuery
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(private val companyRepository: ACRUImmersionRepository<Company, AddCompanyMutation.Data, IEmployerOwnerShip?, AddCompanyMutation.Data, Boolean, GetCompaniesQuery.Data, List<IEmployerOwnerShip>, Unit>) :
    ViewModel() {

    suspend fun createCompany(name: String, description: String?): IEmployerOwnerShip? {
        val company = Company(name = name, description = description, id = "")
        return this.companyRepository.create(company)
    }

    suspend fun getUserCompanies(): MutableList<IEmployerOwnerShip> {

        return this.companyRepository.read(Unit).toMutableList()
    }
}