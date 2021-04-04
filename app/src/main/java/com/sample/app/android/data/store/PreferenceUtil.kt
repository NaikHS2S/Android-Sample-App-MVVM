package com.sample.app.android.data.store

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.IOException

class PreferenceUtil {

    object DataStoreObject {
        var dataStore: DataStore<Preferences>? = null
        var name: String? = null
    }

    companion object {

        val USERNAME = stringPreferencesKey("username")
        const val DATE_STORE_NAME = "data-store-prefs"
        const val PREF_NAME = "prefs"

        suspend fun updatePref(
            keyName: Preferences.Key<String>,
            context: Context,
            keyValue: String
        ) {
            val dataStore: DataStore<Preferences> =
                context.createDataStore(name = DATE_STORE_NAME)
            dataStore.edit { preferences ->
                preferences[keyName] = keyValue
            }
        }

        fun <T> DataStore<Preferences>.getValueFlow(
            key: Preferences.Key<T>,
            defaultValue: T
        ): Flow<T> {
            return this.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    preferences[key] ?: defaultValue
                }
        }

        suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
            this.edit { preferences ->
                preferences[key] = value
            }
        }

        fun Context.createDataStore(
            name: String,
            corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
            migrations: List<DataMigration<Preferences>> = listOf(),
            scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ): DataStore<Preferences> {

            if (DataStoreObject.dataStore == null) {

                DataStoreObject.dataStore = PreferenceDataStoreFactory.create(
                    corruptionHandler = corruptionHandler,
                    migrations = migrations,
                    scope = scope
                )
                {
                    File(this.filesDir, "datastore/$name.preferences_pb")
                }
            }

            return DataStoreObject.dataStore!!
        }
    }
}