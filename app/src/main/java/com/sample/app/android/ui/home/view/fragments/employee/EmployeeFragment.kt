package com.sample.app.android.ui.home.view.fragments.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sample.app.android.R
import com.sample.app.android.room.EmployeeListAdapter
import com.sample.app.android.room.view.model.EmployeeViewModel
import com.sample.app.android.room.view.model.EmployeeViewModelFactory

class EmployeeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = EmployeeListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        employeeViewModel?.allEmployeeData?.observe(viewLifecycleOwner, { list -> adapter.submitList(list) })

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { findNavController().navigate(R.id.action_EmployeeFragment_to_newHiresFragment) }
    }
}