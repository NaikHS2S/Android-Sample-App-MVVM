package com.sample.app.android.utils

import android.util.Patterns

class LoginUtil{

    companion object{

        fun isUserNameValid(username: String): Boolean {
            return if (username.contains('@')) {
                Patterns.EMAIL_ADDRESS.matcher(username).matches()
            } else {
                username.isNotBlank()
            }
        }

        fun isPasswordValid(passWord: String): Boolean {
            return passWord.length > 5
        }

    }

}