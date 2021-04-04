package com.sample.app.android.ui.custom.view

import android.content.Context
import android.view.accessibility.AccessibilityManager


object Utils {

    fun isAccessibilityEnabled(mContext: Context?): Boolean {
        if(mContext != null) {
            val accessibilityManager: AccessibilityManager? =
                mContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?
            return accessibilityManager != null && accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled
        }
        return false
    }

}
