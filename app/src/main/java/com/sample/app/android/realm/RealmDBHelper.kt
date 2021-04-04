package com.sample.app.android.realm

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.exceptions.RealmPrimaryKeyConstraintException

object RealmDBHelper {

    private fun addEmployee(name: String, age: Int, skillType: String) {
        val realmInstance = Realm.getDefaultInstance()
        realmInstance.use {
            it.executeTransaction { realm ->
                try {
                    if (name.isNotEmpty()) {
                        val employee = Employee()
                        employee.name = name.trim()
                        employee.age = age
                        val skillSet: String = skillType.trim()
                        if (skillSet.isNotEmpty()) {
                            var skill = realm.where(Skill::class.java).equalTo(
                                Skill.PROPERTY_SKILL,
                                skillSet
                            ).findFirst()

                            if (skill == null) {
                                skill = realm.createObject(Skill::class.java, skillSet)
                                realm.copyToRealm(skill!!)
                            }

                            employee.skills = RealmList()
                            employee.skills?.add(skill)
                        }
                        realm.copyToRealm(employee)
                    }
                } catch (e: RealmPrimaryKeyConstraintException) {

                }
            }
        }
    }


    private fun getEmployeeRecords(): List<Employee> {
        val employeeList = arrayListOf<Employee>()
        val realmInstance = Realm.getDefaultInstance()
        realmInstance.executeTransaction { realm ->
            val results = realm.where(Employee::class.java).findAll()
            results.forEach { employeeList.add(it) }
        }

        return employeeList
    }

    fun updateEmployeeRecords(employee: Employee) {
        val realmInstance = Realm.getDefaultInstance()
        realmInstance.beginTransaction()
        realmInstance.copyToRealmOrUpdate(employee)
        realmInstance.commitTransaction()
    }


    private fun updateEmployeeRecords(name: String, age: Int, skillType: String) {
        val realmInstance = Realm.getDefaultInstance()

        realmInstance.executeTransaction { realm ->
            if (name.isNotEmpty()) {
                var employee: Employee? = realm.where(Employee::class.java)
                    .equalTo(Employee.PROPERTY_NAME, name).findFirst()
                if (employee == null) {
                    employee = realm.createObject(Employee::class.java, name.trim())
                }
                 employee?.age = age
                val languageKnown: String = skillType.trim()
                var skill = realm.where(Skill::class.java).equalTo(
                    Skill.PROPERTY_SKILL,
                    languageKnown
                ).findFirst()
                if (skill == null) {
                    skill = realm.createObject(Skill::class.java, languageKnown)
                    realm.copyToRealm(skill!!)
                }
                if (!employee?.skills?.contains(skill)!!) employee.skills?.add(skill)
            }
        }
    }

    private fun deleteEmployeeRecord(name: String) {
            Realm.getDefaultInstance().executeTransaction { realm ->
            val employee: Employee? = realm.where(Employee::class.java)
                .equalTo(Employee.PROPERTY_NAME, name).findFirst()
            employee?.deleteFromRealm()
        }
    }

    private fun deleteEmployeeWithSkill(skillType: String) {
        Realm.getDefaultInstance().executeTransaction { realm ->
            val employees: RealmResults<Employee> = realm.where(Employee::class.java).equalTo(
                "skills.skillName",
                skillType.trim()
            ).findAll()
            employees.deleteAllFromRealm()
        }
    }


    private fun deleteAllEmployeesData() {
        Realm.getDefaultInstance().executeTransaction { realm: Realm ->
            realm.delete(Employee::class.java)
        }
    }


    private fun filterByAge(age: Int): List<Employee> {
        val employeeList = arrayListOf<Employee>()
        val realmInstance = Realm.getDefaultInstance()
        realmInstance.executeTransaction { realm ->
            val results: RealmResults<Employee> = realm.where(Employee::class.java).greaterThanOrEqualTo(
                Employee.PROPERTY_AGE,
                age
            ).findAllAsync()
            results.forEach{  employeeList.add(it) }
        }
        return employeeList
    }
}