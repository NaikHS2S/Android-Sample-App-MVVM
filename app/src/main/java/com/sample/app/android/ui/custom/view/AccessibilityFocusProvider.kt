package com.sample.app.android.ui.custom.view

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object AccessibilityFocusProvider {

    fun setViewAccessibilityFocus(context: Context, view: View?, delayTime :Long = 500) {
        if (Utils.isAccessibilityEnabled(context)) {
            setAccessibilityFocus(context, view)
            val task1 = Runnable { setAccessibilityFocus(context, view) }
            val worker1: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
            worker1.schedule(task1, delayTime, TimeUnit.MILLISECONDS)

        }
    }

    private fun setAccessibilityFocus(context: Context, view: View?) {
        if (Utils.isAccessibilityEnabled(context) && view != null) {
            view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
        }
    }

    fun setCustomAccessibilityNodeInfo(layout: TextInputLayout, editText: TextInputEditText) {
        editText.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                val text: CharSequence? = layout.editText?.text
                info.text = getAccessibilityNodeInfoText(layout, text.toString() )
            }
        }
    }


    private fun getAccessibilityNodeInfoText(layout: TextInputLayout, text:String): String? {
        val inputText: CharSequence = text
        val hintText = layout.hint
        val helperText = layout.helperText
        val errorText = layout.error
        val showingText = !TextUtils.isEmpty(inputText)
        val hasHint = !TextUtils.isEmpty(hintText)
        val hasHelperText = !TextUtils.isEmpty(helperText)
        val showingError = !TextUtils.isEmpty(errorText)
        var hint: String? = if (hasHint) hintText.toString() else ""
        hint += if ((showingError || hasHelperText) && !TextUtils.isEmpty(hint)) ", " else ""
        hint += if (showingError) "Error $errorText" else if (hasHelperText) helperText else ""
        return if (showingText) {
            inputText.toString() + if (!TextUtils.isEmpty(hint)) ", $hint" else ""
        } else if (!TextUtils.isEmpty(hint)) {
            hint
        } else {
            ""
        }
    }
}