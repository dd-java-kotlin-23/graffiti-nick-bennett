package edu.cnm.deepdive.graffiti.repository

import android.app.Activity
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.cnm.deepdive.graffiti.R
import edu.cnm.deepdive.graffiti.model.auth.AuthCredential
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
internal class GoogleAuthRepository @Inject constructor(
    @ApplicationContext context: Context,
    // TODO: Add a TokenParser 
) : AuthRepository {
    
    private val clientId: String = context.getString(R.string.client_id)
    // TODO: Create a CredentialManager and store a reference to it in a property. 
    
    override suspend fun signInQuickly(activity: Activity): AuthCredential {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(activity: Activity): AuthCredential {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(
        activity: Activity,
        credential: AuthCredential
    ): AuthCredential {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}