package com.sample.app.android.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee_table ORDER BY name ASC")
    fun getEmployeeList(): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Query("DELETE FROM employee_table")
    suspend fun deleteAll()
}