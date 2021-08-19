package com.sample.app.android.ui.home.view.model

import android.content.Intent
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sample.app.android.R
import com.sample.app.android.data.store.PreferenceUtil
import com.sample.app.android.ui.login.view.LoginActivity
import com.sample.app.android.ui.view.model.BaseObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentModel : BaseObservableViewModel() {

    var navController: NavController? = null

    fun logout(view: View){
        viewModelScope.launch(Dispatchers.Main) { PreferenceUtil.updatePref(PreferenceUtil.USERNAME, view.context, "")}
        PreferenceUtil.DataStoreObject.name = ""
        val intent = Intent(view.context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        view.context.startActivity(intent)
    }

    fun navigateToAbout(view: View){
        if(view.context != null && navController != null) {
            navController?.navigate(R.id.action_title_to_about)
        }
    }

}