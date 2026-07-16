package edu.cnm.deepdive.graffiti.repository

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.cnm.deepdive.graffiti.R
import edu.cnm.deepdive.graffiti.model.auth.AuthCredential
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
internal class GoogleAuthRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val tokenParser: TokenParser,
) : AuthRepository {

    private val clientId: String = context.getString(R.string.client_id)
    private val credentialManager = CredentialManager.create(context)

    override suspend fun signInQuickly(activity: Activity): AuthCredential =
        attemptSignIn(activity, filter = true, autoSelect = false)

    override suspend fun signIn(activity: Activity): AuthCredential {
        return attemptSignIn(activity, filter = false, autoSelect = false)
    }

    override suspend fun refreshToken(
        activity: Activity,
        credential: AuthCredential
    ): AuthCredential {
        return if (!tokenParser.isExpired(credential.idToken)) {
            credential
        } else {
            attemptSignIn(activity, filter = true, autoSelect = true)
        }
    }

    override suspend fun signOut() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    private suspend fun attemptSignIn(
        activity: Activity,
        filter: Boolean,
        autoSelect: Boolean,
    ): AuthCredential {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filter)
            .setAutoSelectEnabled(autoSelect)
            .setServerClientId(clientId)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return try {
            val result = credentialManager.getCredential(activity, request)
            if (result.credential is CustomCredential
                && result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                GoogleIdTokenCredential.createFrom(result.credential.data).toAuthCredential()
            } else {
                throw IllegalStateException("Credential is not a Google ID token credential.")
            }
        } catch (e: Exception) {
            throw AuthRepository.SignInRequiredException("Sign-in required.", e)
        }
    }

    private fun GoogleIdTokenCredential.toAuthCredential(): AuthCredential =
        AuthCredential(
            idToken,
            tokenParser.extractSubject(idToken),
            tokenParser.extractDisplayName(idToken),
        )

}