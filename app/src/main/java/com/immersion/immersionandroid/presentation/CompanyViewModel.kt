package com.immersion.immersionandroid.presentation

import androidx.lifecycle.ViewModel
import com.immersion.AddCompanyMutation
import com.immersion.immersionandroid.dataAccess.ACRURepository
import com.immersion.immersionandroid.domain.Company
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(private val companyRepository: ACRURepository<Company, AddCompanyMutation.Data, Boolean, Boolean, AddCompanyMutation.Data>) :
    ViewModel() {

    suspend fun createCompany(name: String, description: String?) {
        val company = Company(name = name, description = description)
        this.companyRepository.create(company)
    }
}