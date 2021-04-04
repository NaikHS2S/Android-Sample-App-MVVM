package com.sample.app.android.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val employeeDao: EmployeeDao) {

    val allEmployee: Flow<List<EmployeeEntity>> = employeeDao.getEmployeeList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(employee: EmployeeEntity) {
        employeeDao.insert(employee)
    }
}