package edu.cnm.deepdive.graffiti.session

import android.app.Activity
import edu.cnm.deepdive.graffiti.model.domain.User
import kotlinx.coroutines.flow.StateFlow

interface SessionManager {

    val state: StateFlow<SessionState>

    suspend fun signInAutomatically(activity: Activity): User

    suspend fun signInInteractively(activity: Activity): User

    fun getCurrentUser(): User?

    suspend fun getAuthorizationHeader(activity: Activity): String

    suspend fun signOut()

}