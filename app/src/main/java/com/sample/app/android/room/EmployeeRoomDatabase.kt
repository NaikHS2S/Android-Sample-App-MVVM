package com.sample.app.android.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [EmployeeEntity::class], version = 1, exportSchema = false)
abstract class EmployeeRoomDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): EmployeeRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmployeeRoomDatabase::class.java, "employee_database")
                    //.fallbackToDestructiveMigration()
                    .addMigrations(migration2To3())
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }


        private fun migration1To2() = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'employee_table' ADD COLUMN 'age' INTEGER NOT NULL DEFAULT 0")
            }
        }


        // Migration from 2 to 3
        private fun migration2To3() = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                        CREATE TABLE new_employee_table (
                            employeeId TEXT PRIMARY KEY NOT NULL,
                            name TEXT NOT NULL,
                            age INTEGER NOT NULL DEFAULT 0
                            )
                        """.trimIndent()
                )
                database.execSQL(
                    """
                        INSERT INTO new_employee_table (employeeId, name)
                        SELECT employeeId, name FROM employee_table
                        """.trimIndent()
                )
                database.execSQL("DROP TABLE employee_table")
                database.execSQL("ALTER TABLE new_employee_table RENAME TO employee_table")
            }
        }



        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.employeeDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(employeeDao: EmployeeDao) {
            employeeDao.deleteAll()
            var employee = EmployeeEntity("E100000", "Raj")
            employeeDao.insert(employee)
            employee = EmployeeEntity("E100001", "John")
            employeeDao.insert(employee)
        }
    }
}
