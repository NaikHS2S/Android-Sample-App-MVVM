package com.sample.app.android.ui.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.app.android.data.LoginDataSource
import com.sample.app.android.data.LoginRepository
import com.sample.app.android.ui.home.view.model.HomeFragmentModel
import com.sample.app.android.ui.home.view.model.ProfileViewModel
import com.sample.app.android.ui.login.view.model.LoginViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(
                    loginRepository = LoginRepository(
                        dataSource = LoginDataSource()
                    )
                ) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel() as T
            }
            modelClass.isAssignableFrom(HomeFragmentModel::class.java) -> {
                return HomeFragmentModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}