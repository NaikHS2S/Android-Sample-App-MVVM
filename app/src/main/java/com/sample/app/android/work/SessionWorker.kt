package com.sample.app.android.work

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sample.app.android.data.store.PreferenceUtil
import com.sample.app.android.ui.login.view.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SessionWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    private val context = appContext

    override fun doWork(): Result {

        CoroutineScope(Job() +Dispatchers.Default).launch(Dispatchers.Main) { PreferenceUtil.updatePref(
            PreferenceUtil.USERNAME,
            context,
            ""
        )}
        PreferenceUtil.DataStoreObject.name = ""
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        return Result.success()
    }
}