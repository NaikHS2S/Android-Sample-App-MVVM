package com.sample.app.android.room.view.model

import android.content.Context
import androidx.lifecycle.*
import com.sample.app.android.room.EmployeeEntity
import com.sample.app.android.room.EmployeeRepository
import com.sample.app.android.room.EmployeeRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {

    val allEmployeeData: LiveData<List<EmployeeEntity>> = repository.allEmployee.asLiveData()

    fun insert(employeeEntity: EmployeeEntity) = viewModelScope.launch {
        repository.insert(employeeEntity)
    }
}

class EmployeeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeeViewModel::class.java)) {

            if (CustomDBObject.repository == null) {
                val database by lazy {
                    context.let {
                        EmployeeRoomDatabase.getDatabase(
                            it,
                            CoroutineScope(SupervisorJob())
                        )
                    }
                }

                CustomDBObject.repository = EmployeeRepository(database.employeeDao())
            }

            @Suppress("UNCHECKED_CAST")
            return EmployeeViewModel(CustomDBObject.repository!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    object CustomDBObject {
        var repository: EmployeeRepository? = null
    }


}
