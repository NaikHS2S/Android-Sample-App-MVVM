package com.sample.app.android.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.app.android.R

class EmployeeListAdapter : ListAdapter<EmployeeEntity, EmployeeListAdapter.EmployeeViewHolder>(EMP_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.age, current.employeeId)
    }

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val age: TextView = itemView.findViewById(R.id.age)
        private val id: TextView = itemView.findViewById(R.id.id)

        fun bind(nameValue: String?, ageValue: Int, empId: String?) {
            name.text = "Name: $nameValue"
            age.text = "Age: $ageValue"
            id.text = "Id: $empId"
        }

        companion object {
            fun create(parent: ViewGroup): EmployeeViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
                return EmployeeViewHolder(view)
            }
        }
    }

    companion object {
        private val EMP_COMPARATOR = object : DiffUtil.ItemCallback<EmployeeEntity>() {
            override fun areItemsTheSame(oldItem: EmployeeEntity, newItem: EmployeeEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: EmployeeEntity, newItem: EmployeeEntity): Boolean {
                return oldItem.employeeId == newItem.employeeId
            }
        }
    }
}