package com.sample.app.android.data.model

import javax.inject.Inject

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser @Inject constructor(val userId: String, val displayName: String)