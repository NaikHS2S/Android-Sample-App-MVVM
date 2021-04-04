package com.sample.app.android.ui.custom.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.accessibility.AccessibilityEvent
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.sample.app.android.R
import com.sample.app.android.databinding.CustomTextInputBinding
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private const val TEXT_INPUT = 0
private const val NUMBER_INPUT = 1

@SuppressLint("ClickableViewAccessibility")
class CustomTextInputLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var textField: CustomTextInputEditText
    private var outlinedTextField: TextInputLayout
    private var minValue = 0f
    private var maxValue = 0f

    constructor(context: Context) : this(context, null)
    var editTextType = TEXT_INPUT

    init {

        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomInputLayout, 0, 0)
        editTextType = a.getInt(R.styleable.CustomInputLayout_editTextType, TEXT_INPUT)
        val hintText = a.getString(R.styleable.CustomInputLayout_hintText)

        a.recycle()
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL

        val binding: CustomTextInputBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_text_input,this, true)
        textField = binding.textField
        outlinedTextField = binding.outlinedTextField

        setTextHint(hintText ?: "")

        AccessibilityFocusProvider.setCustomAccessibilityNodeInfo(outlinedTextField, textField)

        textField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestFocusOnAView(textField)
                return@setOnEditorActionListener false
            }
            false
        }

        textField.listener = object : CustomTextInputEditText.Listener {
            override fun onImeBack(editText: CustomTextInputEditText) {
                requestFocusOnAView(textField)
            }
        }

        setKeyInputType()

    }

    private fun setKeyInputType() {

        when (editTextType) {

            NUMBER_INPUT -> {
                textField.inputType = InputType.TYPE_CLASS_NUMBER
            }
            else -> {
                textField.inputType = InputType.TYPE_CLASS_TEXT
            }

        }
    }

    fun validateEditText() {
        val error = getErrorText()

        if (!TextUtils.isEmpty(error)) {
            textField.setError(null, null)
            outlinedTextField.isErrorEnabled = true
            outlinedTextField.error = error
        }else{
            textField.setError(null, null)
            outlinedTextField.error = null
            outlinedTextField.isErrorEnabled = false
            outlinedTextField.boxStrokeColor = (ContextCompat.getColor(context, R.color.input_layout_box_color))
        }
    }

    private fun getErrorText(): String {

        val isLimitValidation =  maxValue > 0f &&  editTextType == NUMBER_INPUT
        if (isHavingEmptyText()) {
            return context.getString(R.string.is_required, textField.hint.toString())
        } else if(isLimitValidation){
            when (editTextType) {
                NUMBER_INPUT -> {
                    return getNumberError()
                }
            }
        }
        return ""
    }

    private fun getNumberError(): String {
        try{
            val intValue = textField.text.toString().toFloat()
            if (minValue > intValue || maxValue < intValue) {
                return context.getString(R.string.should_be_between, minValue.toInt().toString(), maxValue.toInt().toString(), textField.hint.toString())
            }}catch (_:Exception){
            return context.getString(R.string.should_be_between, minValue.toInt().toString(), maxValue.toInt().toString(), textField.hint.toString())
        }

        return ""
    }

    private fun isHavingEmptyText() = TextUtils.isEmpty(textField.text)

    fun setText(textValue: String) {
        textField.setText(textValue)
    }

    fun resetEdit() {
        textField.text?.clear()
        textField.clearFocus()
        outlinedTextField.isErrorEnabled = false
        outlinedTextField.error = ""
        outlinedTextField.boxStrokeColor = (ContextCompat.getColor(context, R.color.input_layout_box_color))
    }

    private fun setTextHint(textValue: String) {
        getOutlinedTextField().hint = textValue
    }

    fun setOtherInputAsHint() {
        getOutlinedTextField().hint = getDescriptionHint()
    }

    private fun getDescriptionHint() = context.getString(R.string.description)

    fun addTextChangedListener(watcher: TextWatcher?) {
        textField.addTextChangedListener(watcher)
    }

    fun hasEditFocus(): Boolean {
        return textField.hasFocus()
    }

    fun clearEditFocus() {
        textField.clearFocus()
    }

    private fun requestFocusOnAView(@Nullable view: CustomTextInputEditText?) {
        if (view != null && view.hasFocus()) {
            requestFocusOnEdit(view)
        }
    }

    fun requestFocusOnThis() {
        requestFocusOnEdit(textField)
    }

    private fun requestFocusOnEdit(view: CustomTextInputEditText?) {
        if (Utils.isAccessibilityEnabled(context) && view != null ) {
            val task = Runnable {
                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
            }
            val worker: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
            worker.schedule(task, 500, TimeUnit.MILLISECONDS)
        }
    }

    fun getTextValue(): String {
        return getTextField().text.toString()
    }

    fun setError(saveAPIError: String) {
        getTextField().setError(null, null)
        getOutlinedTextField().isErrorEnabled = true
        getOutlinedTextField().error = saveAPIError
    }

    private fun getOutlinedTextField() = outlinedTextField
    private fun getTextField() = textField

}