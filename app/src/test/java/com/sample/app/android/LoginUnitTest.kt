package com.sample.app.android

import com.sample.app.android.ui.login.view.LoginActivity
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginUnitTest {
    @Mock
    var loginActivity: LoginActivity? = null

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        loginActivity = mock(LoginActivity::class.java)
    }

}