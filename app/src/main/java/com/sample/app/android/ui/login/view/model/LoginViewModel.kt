package com.sample.app.android.ui.login.view.model

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.app.android.R
import com.sample.app.android.data.LoginRepository
import com.sample.app.android.data.Result
import com.sample.app.android.ui.login.model.LoginFormState
import com.sample.app.android.ui.login.model.LoginResult
import com.sample.app.android.ui.login.view.LoggedInUserView
import com.sample.app.android.ui.view.model.BaseObservableViewModel
import com.sample.app.android.utils.LoginUtil

class LoginViewModel(private val loginRepository: LoginRepository) : BaseObservableViewModel() {

    var userName = ObservableField<String>()
    var password = ObservableField<String>()
    var loadingVisibility = ObservableBoolean(false)

    //To save local validation status

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    fun validateLogin(view: View) {
        if(view.context != null) {
            loadingVisibility.set(true)
            login(getUserNameText(), getPasswordText())
        }
    }

    private fun getPasswordText() = password.get() ?: ""

    private fun getUserNameText() = userName.get() ?: ""

    fun login(username: String, password: String) {
        val result = loginRepository.login(username, password)
        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged() {

        val isUserNameInValid = !LoginUtil.isUserNameValid(getUserNameText())
        val isInvalidPassword = !LoginUtil.isPasswordValid(getPasswordText())

        when {
            isUserNameInValid -> {
                _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            }
            isInvalidPassword -> {
                _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            }
            else -> {
                _loginForm.value = LoginFormState(isDataValid = true)
            }
        }
    }

    fun isUserNameValid() = LoginUtil.isUserNameValid(getUserNameText())

    fun isPasswordValid() = LoginUtil.isPasswordValid(getPasswordText())



}