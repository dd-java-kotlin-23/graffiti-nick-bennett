package edu.cnm.deepdive.graffiti.repository

import android.app.Activity
import edu.cnm.deepdive.graffiti.model.auth.AuthCredential

interface AuthRepository {

    suspend fun signInAutomatically(activity: Activity): AuthCredential

    suspend fun signInInteractively(activity: Activity): AuthCredential

    suspend fun refreshToken(activity: Activity, credential: AuthCredential): AuthCredential

    suspend fun signOut()

}
