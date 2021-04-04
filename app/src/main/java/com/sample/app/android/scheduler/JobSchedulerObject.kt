package com.sample.app.android.scheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import com.sample.app.android.constants.ConstantFile
import com.sample.app.android.constants.ConstantFile.Companion.JOB_ID


object JobSchedulerObject {

    fun reScheduleJob(jobId: Int = JOB_ID, context: Context) {
        cancelJob(jobId, context)
        scheduleJob(jobId, context)
    }

    private fun cancelJob(jobId: Int = JOB_ID, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val jobScheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            jobScheduler.cancel(jobId)
        }
    }

    fun cancelAllJobs(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val jobScheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            jobScheduler.cancelAll()
        }
    }

    fun scheduleJob(jobId: Int = JOB_ID, context: Context) {

        val serviceComponent = ComponentName(context, LogoutService::class.java)
        val builder = JobInfo.Builder(jobId, serviceComponent)
        builder.setMinimumLatency(1 * 60000) // wait at least
        builder.setOverrideDeadline(1 * 60000)
        //builder.setPeriodic(1 * 1000.toLong()) // wait at least
        //builder.setPersisted(true) //Needs   <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not

        val bundle = PersistableBundle()
        //val bundle2 = Bundle()
        bundle.putInt(ConstantFile.USER, JOB_ID)
        builder.setExtras(bundle)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setTransientExtras(bundle2)
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val jobScheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            jobScheduler.schedule(builder.build())
        }
    }

}