package com.immersion.immersionandroid.dataAccess

import com.immersion.immersionandroid.domain.CompanySector

interface ICompanySectorImmersionRepository: IImmersionRepository {
    suspend fun getAllCompanySectors(): List<CompanySector>
}