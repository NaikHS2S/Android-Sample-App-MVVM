package com.sample.app.android.data

import com.sample.app.android.data.model.LoggedInUser

class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), username))
            } else {
                Result.Error(Exception("Wrong Password"))
            }
        } catch (e: Throwable) {
            Result.Error(Exception("Error logging in", e))
        }
    }

}