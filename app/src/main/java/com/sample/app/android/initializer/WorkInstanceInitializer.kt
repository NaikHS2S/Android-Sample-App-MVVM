package com.sample.app.android.initializer

import android.annotation.SuppressLint
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerInitializer
import androidx.work.impl.utils.SynchronousExecutor

@SuppressLint("RestrictedApi")
class WorkInstanceInitializer : WorkManagerInitializer() {

    override fun onCreate(): Boolean {

       val configuration = Configuration.Builder()
            // Set log level to Log.DEBUG to make it easier to debug
            .setMinimumLoggingLevel(Log.DEBUG)
            // Use a SynchronousExecutor here to make it easier to write tests
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManager.initialize(context!!, configuration)
        return true
    }
}