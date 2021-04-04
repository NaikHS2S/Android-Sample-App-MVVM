package com.sample.app.android.ui.home.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.app.android.R
import com.sample.app.android.constants.ConstantFile
import com.sample.app.android.secure.KeyStoreRSAUtil
import com.sample.app.android.secure.KeystoreWithAESUtil
import com.sample.app.android.ui.home.view.navigation.setupWithNavController
import com.sample.app.android.work.WorkerObject

class HomeActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KeyStoreRSAUtil.createNewKeys(ConstantFile.USER_KEY)
        KeystoreWithAESUtil.initialize(ConstantFile.USER_KEY)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        //JobSchedulerObject.reScheduleJob(ConstantFile.SESSION_JOB_ID, this)
        WorkerObject.scheduleWork(this)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(R.navigation.home_graph, R.navigation.employee_fragments_graph, R.navigation.profile_update)

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        controller.observe(this, { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
