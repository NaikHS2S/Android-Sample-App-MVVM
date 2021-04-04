package com.sample.app.android.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_table")
class EmployeeEntity(@PrimaryKey @ColumnInfo(name = "employeeId") val employeeId: String, @ColumnInfo(name = "name") val name: String, @ColumnInfo(name = "age") val age: Int = 0)