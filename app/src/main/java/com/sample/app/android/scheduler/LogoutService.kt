package com.sample.app.android.scheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.sample.app.android.data.store.PreferenceUtil
import com.sample.app.android.ui.login.view.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogoutService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        val logoutService = this
        GlobalScope.launch(Dispatchers.Main) { PreferenceUtil.updatePref(
            PreferenceUtil.USERNAME,
            logoutService,
            ""
        )}
        PreferenceUtil.DataStoreObject.name = ""
        val intent = Intent(logoutService, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}