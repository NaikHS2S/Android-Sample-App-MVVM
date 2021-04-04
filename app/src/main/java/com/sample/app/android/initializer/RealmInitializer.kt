package com.sample.app.android.initializer

import android.content.Context
import androidx.startup.Initializer
import com.sample.app.android.realm.Employee.Companion.PROPERTY_AGE
import io.realm.Realm
import io.realm.RealmConfiguration


class RealmInitializer : Initializer<Realm> {
    override fun create(context: Context): Realm {

        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .name("employee.db")
            .schemaVersion(2)
            .migration { realm, oldVersion, newVersion ->
                val schema = realm.schema
                if (oldVersion == 0L && newVersion == 2L) {
                    val userSchema = schema["Employee"]
                    userSchema!!.addField(PROPERTY_AGE, String::class.java)
                }
            }
            .deleteRealmIfMigrationNeeded()
            .build()

        return Realm.getInstance(config)
    }
    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return arrayListOf()
    }
}

