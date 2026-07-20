package edu.cnm.deepdive.graffiti.repository

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest.Builder
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
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

    override suspend fun signInAutomatically(activity: Activity): AuthCredential =
        getCredential(activity, buildGoogleIdOption(filter = true, autoSelect = true))

    override suspend fun signInInteractively(activity: Activity): AuthCredential =
        getCredential(activity, GetSignInWithGoogleOption.Builder(clientId).build())

    override suspend fun refreshToken(
        activity: Activity,
        credential: AuthCredential
    ): AuthCredential =
        if (!tokenParser.isExpired(credential.idToken)) {
            credential
        } else {
            getCredential(activity, buildGoogleIdOption(filter = true, autoSelect = true))
        }

    override suspend fun signOut() =
        credentialManager.clearCredentialState(ClearCredentialStateRequest())

    private suspend fun getCredential(
        activity: Activity,
        option: CredentialOption
    ): AuthCredential {
        val request = Builder()
            .addCredentialOption(option)
            .build()
        return parseCredential(credentialManager.getCredential(activity, request))
    }

    private fun buildGoogleIdOption(filter: Boolean, autoSelect: Boolean): GetGoogleIdOption =
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filter)
            .setAutoSelectEnabled(autoSelect)
            .setServerClientId(clientId)
            .build()

    private fun parseCredential(result: GetCredentialResponse): AuthCredential {
        val credential = result.credential
        if (credential is CustomCredential
            && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.createFrom(credential.data).toAuthCredential()
        }
        throw IllegalStateException("Credential is not a Google ID token credential.")
    }

    private fun GoogleIdTokenCredential.toAuthCredential(): AuthCredential =
        AuthCredential(
            idToken,
            tokenParser.extractSubject(idToken),
            tokenParser.extractDisplayName(idToken),
        )

}