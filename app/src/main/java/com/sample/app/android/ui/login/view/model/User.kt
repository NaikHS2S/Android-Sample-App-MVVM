package com.sample.app.android.ui.login.view.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class User : BaseObservable() {

    @get:Bindable
    var userName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }

    @get:Bindable
    var passWord: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.passWord)
        }
}