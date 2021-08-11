package com.sample.app.android

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.sample.app.android.ui.custom.view.CustomTextInputEditText

@BindingAdapter("userNameError")
fun displayUserNameError(view: EditText, userError: Int?) {
    if(userError != null) {
        view.error = view.context.getString(R.string.invalid_username)
    }
}

@BindingAdapter("passError")
fun displayPasswordError(view: EditText, passWordError: Int?) {
    if(passWordError != null) {
        view.error = view.context.getString(R.string.invalid_password)
    }
}

@BindingAdapter("textError")
fun displayPasswordError(view: CustomTextInputEditText, error: String?) {
    if(error != null && error.isNotEmpty()) {
        view.error = error
    }
}

@BindingAdapter("loginEnable")
fun loginEnable(view: Button, isLoginEnable: Boolean) {
    view.isEnabled = isLoginEnable
}

@BindingAdapter(value = ["accessibilityReadText", "isClickable"], requireAll = false)
fun updateValue(view: Button, accessibilityReadText: String, isClickable: Boolean) {
    view.importantForAccessibility = if(accessibilityReadText.isNotEmpty())  View.IMPORTANT_FOR_ACCESSIBILITY_YES else View.IMPORTANT_FOR_ACCESSIBILITY_NO
    view.contentDescription = if(accessibilityReadText.isNotEmpty()) accessibilityReadText else view.text

}
