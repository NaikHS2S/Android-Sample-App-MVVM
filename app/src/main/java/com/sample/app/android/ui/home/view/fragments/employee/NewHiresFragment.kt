package com.sample.app.android.ui.home.view.fragments.employee

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sample.app.android.R
import com.sample.app.android.room.EmployeeEntity
import com.sample.app.android.room.view.model.EmployeeViewModel
import com.sample.app.android.room.view.model.EmployeeViewModelFactory
import com.sample.app.android.ui.custom.view.CustomTextInputEditText


class NewHiresFragment : Fragment() {

    private var employeeViewModel: EmployeeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {  employeeViewModel= ViewModelProvider(this, EmployeeViewModelFactory(requireActivity()) ).get(EmployeeViewModel::class.java)}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editEmpId = view.findViewById<CustomTextInputEditText>(R.id.editEmpId)
        val editEmpName = view.findViewById<CustomTextInputEditText>(R.id.editName)
        val editEmpAge = view.findViewById<CustomTextInputEditText>(R.id.editAge)

        val button = view.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            if (TextUtils.isEmpty(editEmpId.text) || TextUtils.isEmpty(editEmpName.text)) {
                  Toast.makeText(activity, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
            } else {
                val employeeEntity = EmployeeEntity(editEmpId.text.toString(), editEmpName.text.toString(),
                    getEmployeeAge(editEmpAge)
                )
                employeeViewModel?.insert(employeeEntity)
                editEmpId.text?.clear()
                editEmpName.text?.clear()
                editEmpAge.text?.clear()
                editEmpId.clearFocus()
                editEmpName.clearFocus()
                editEmpAge.clearFocus()
                val imm: InputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                Toast.makeText(activity, getString(R.string.data_saved), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getEmployeeAge(editEmpAge: CustomTextInputEditText) =
        if(TextUtils.isEmpty(editEmpAge.text))  editEmpAge.text.toString().toInt() else 0
}