package com.sample.app.android.ui.login.model

import com.sample.app.android.ui.login.view.LoggedInUserView

data class LoginResult(val success: LoggedInUserView? = null, val error: Int? = null)