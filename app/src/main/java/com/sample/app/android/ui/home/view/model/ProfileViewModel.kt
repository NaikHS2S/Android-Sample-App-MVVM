package com.sample.app.android.ui.home.view.model

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sample.app.android.R
import com.sample.app.android.data.store.PreferenceUtil
import com.sample.app.android.data.store.PreferenceUtil.Companion.getValueFlow
import com.sample.app.android.ui.login.view.LoggedInUserView
import com.sample.app.android.ui.view.model.BaseObservableViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel : BaseObservableViewModel() {

    var nameEditValue = ObservableField<String>()
    var navController: NavController? = null
    var name = MutableLiveData("")
    var displayName = MutableLiveData("")
    var errorText = MutableLiveData("")

    init {
        viewModelScope.launch {
            PreferenceUtil.DataStoreObject.dataStore?.getValueFlow(PreferenceUtil.USERNAME, "")?.collect { value ->
                if (value.isNotEmpty()) {
                    val model = LoggedInUserView(value)
                    if (model.displayName.isNotEmpty()) {
                        name = MutableLiveData(" "+model.displayName)
                        displayName =  MutableLiveData("Hi ! "+model.displayName)
                    }
                }
            }
        }
    }

    private fun updateName(nameValue: String){
        displayName.value =  "Hi ! $nameValue"
        name.value = " $nameValue"
    }

    fun updateUser(view: View) {
        if (view.context != null) {
            val nameValue = nameEditValue.get() ?: ""
            if (nameValue.isNotEmpty()) {
                updateName(nameValue)
                nameEditValue.set("")
                errorText.value = ""
                navController?.navigate(R.id.action_profile_to_profileDone)
            }else{
                errorText.value = "Enter a valid name"
            }
        }
    }

}