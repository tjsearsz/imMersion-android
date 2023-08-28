package com.immersion.immersionandroid.dataAccess

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.immersion.CompanySectorsQuery
import com.immersion.immersionandroid.domain.CompanySector

class CompanySectorImmersionRepository(private val apolloClient: ApolloClient) :
    ICompanySectorImmersionRepository {
    override suspend fun getAllCompanySectors(): List<CompanySector> {

        val query = CompanySectorsQuery()

        return try {
            val response = this.executeQuery(query, apolloClient)

            if (response != null && !response.hasErrors()) {
                return response.data!!.CompanySectors.map { (_id, title) ->
                    CompanySector(_id, title)
                }

            }

            emptyList()
        } catch (error: SecurityException) {
            emptyList()
        }
    }
}