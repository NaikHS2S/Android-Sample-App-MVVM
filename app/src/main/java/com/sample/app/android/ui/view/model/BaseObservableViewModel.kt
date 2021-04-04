package com.sample.app.android.ui.view.model

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel

open class BaseObservableViewModel : ViewModel(), Observable {
    private var callbacks: PropertyChangeRegistry? = null

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks?.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        if (callbacks == null) {
            callbacks = PropertyChangeRegistry()
        }
        callbacks?.add(callback)
    }

    fun notifyChange() {
        callbacks?.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks?.notifyCallbacks(this, fieldId, null)
    }
}