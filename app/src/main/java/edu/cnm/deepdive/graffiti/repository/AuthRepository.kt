package edu.cnm.deepdive.graffiti.repository

import android.app.Activity
import edu.cnm.deepdive.graffiti.model.auth.AuthCredential

interface AuthRepository {

    suspend fun signInQuickly(activity: Activity): AuthCredential

    suspend fun signIn(activity: Activity): AuthCredential

    suspend fun refreshToken(activity: Activity, credential: AuthCredential): AuthCredential

    suspend fun signOut()

    class SignInRequiredException(message: String, cause: Throwable) :
        RuntimeException(message, cause)

}