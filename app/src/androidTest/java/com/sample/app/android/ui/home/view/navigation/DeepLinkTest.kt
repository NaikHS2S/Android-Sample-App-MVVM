package com.sample.app.android.ui.home.view.navigation

import android.content.Intent
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.sample.app.android.R
import com.sample.app.android.ui.home.view.HomeActivity
import org.junit.Rule
import org.junit.Test

class DeepLinkTest {

    private val employeeToAdd = "Employee"

    private val url = "http://www.xyz.com/employee/$employeeToAdd"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityTestRule = object : ActivityTestRule<HomeActivity>(HomeActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return Intent(Intent.ACTION_VIEW, Uri.parse(url))
        }
    }

    @Test
    fun bottomNavView_DeepLink_HandlesIntent_BackGoesToList() {
        // Opening the app with the proper Intent should start it in the profile screen.
        assertInProfile()

        pressBack()

        pressBack()

        // Home destination should be shown
        assertInHome()
    }

    private fun assertInProfile() {
        onView(withText(employeeToAdd))
            .check(matches(isDisplayed()))
    }

    private fun assertInHome() {
        onView(withText(R.string.welcome))
            .check(matches(isDisplayed()))
    }
}
