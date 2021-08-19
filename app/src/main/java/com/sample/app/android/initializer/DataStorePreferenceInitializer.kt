package com.sample.app.android.initializer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.startup.Initializer
import com.sample.app.android.data.store.PreferenceUtil
import com.sample.app.android.data.store.PreferenceUtil.Companion.DATE_STORE_NAME
import com.sample.app.android.data.store.PreferenceUtil.Companion.PREF_NAME
import com.sample.app.android.data.store.PreferenceUtil.Companion.createDataStore
import com.sample.app.android.data.store.PreferenceUtil.Companion.getValueFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataStorePreferenceInitializer : Initializer<DataStore<Preferences>> {
    override fun create(context: Context): DataStore<Preferences> {

        val createDataStore = context.createDataStore(
            name = DATE_STORE_NAME, migrations = listOf(
                SharedPreferencesMigration(context, PREF_NAME)
            )
        )

        CoroutineScope(Job() + Dispatchers.Default).launch {
            createDataStore.getValueFlow(PreferenceUtil.USERNAME, "").collect { value ->
                if (value.isNotEmpty()) {
                 PreferenceUtil.DataStoreObject.name = value
                }
            }
        }

        return createDataStore
    }
    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return arrayListOf(RealmInitializer::class.java)
       // return arrayListOf()
    }
}