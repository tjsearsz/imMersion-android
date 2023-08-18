package com.immersion.immersionandroid.dataAccess

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class DataStoreRepository(private val context: Context): IDataStoreRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("immersion_pref")

    override suspend fun get(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override suspend fun save(key: String, value: String) {
        Log.d("TESTING","VOY A SALVAR $key $value" )
        val dataStoreKey = stringPreferencesKey(key)
        Log.d("TESTING", "agarre el key $dataStoreKey")


        context.dataStore.edit{
            settings ->

            settings[dataStoreKey] = value
        }
    }
}