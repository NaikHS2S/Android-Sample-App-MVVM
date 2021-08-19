package com.sample.app.android.ui.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sample.app.android.R
import com.sample.app.android.constants.ConstantFile.Companion.USER
import com.sample.app.android.data.store.PreferenceUtil
import com.sample.app.android.data.store.PreferenceUtil.Companion.USERNAME
import com.sample.app.android.databinding.ActivityLoginBinding
import com.sample.app.android.ui.home.view.HomeActivity
import com.sample.app.android.ui.login.view.model.LoginViewModel
import com.sample.app.android.ui.view.model.ViewModelFactory
import com.sample.app.android.work.WorkerObject
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory()).get(
            LoginViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this


        loginViewModel.userName.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                loginViewModel.loginDataChanged()
            }
        })

        loginViewModel.password.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                loginViewModel.loginDataChanged()
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loginViewModel.loadingVisibility.set(false)

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            } else if (loginResult.success != null) {

                CoroutineScope(Job() +Dispatchers.Default).launch(Dispatchers.Main) {
                    PreferenceUtil.updatePref(
                        USERNAME,
                        this@LoginActivity,
                        loginResult.success.displayName
                    )
                }

                updateUiWithUser(loginResult.success)
            }
        })

    }


    override fun onResume() {
        super.onResume()
        navigateToHome()
    }

    private fun navigateToHome() {
        val value = PreferenceUtil.DataStoreObject.name ?: ""

        if (value.isNotEmpty()) {
            val model = LoggedInUserView(value)
            if (model.displayName.isNotEmpty()) {
                updateUiWithUser(model)
            } else {
                updateUiWithUser(LoggedInUserView(""))
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        //JobSchedulerObject.scheduleJob(ConstantFile.SESSION_JOB_ID, this)
        WorkerObject.scheduleWork(this)
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(USER, model.displayName)
        PreferenceUtil.DataStoreObject.name = model.displayName
        startActivity(intent)
        setResult(Activity.RESULT_OK)
        finish()
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
