package com.sample.app.android.realm

import io.realm.RealmObject


open class Skill : RealmObject() {

    var skillName: String? = null

    companion object {
        const val PROPERTY_SKILL = "skillName"
    }
}