package com.sample.app.android.ui.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.google.android.material.textfield.TextInputEditText

class CustomTextInputEditText : TextInputEditText {

    var listener: Listener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            listener?.onImeBack(this)
        }
        return super.dispatchKeyEvent(event)
    }

    interface Listener {
        fun onImeBack(editText: CustomTextInputEditText)
    }

}