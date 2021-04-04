package com.sample.app.android.work

import android.content.Context
import androidx.work.*
import com.sample.app.android.constants.ConstantFile.Companion.SESSION_JOB_ID
import java.util.concurrent.TimeUnit

object WorkerObject {

    var workManager: WorkManager? = null

    fun scheduleWork(myContext: Context) {

        if (workManager == null) {
            workManager = WorkManager.getInstance(myContext)
        }

        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            //.setRequiresDeviceIdle(true)
            .build()

        workManager?.cancelAllWorkByTag(SESSION_JOB_ID.toString())

        val sessionWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<SessionWorker>()
            .setConstraints(constraints)
            .setInitialDelay(15, TimeUnit.MINUTES)
            .addTag(SESSION_JOB_ID.toString())
            .build()
        workManager?.enqueue(sessionWorkRequest)

    }
}