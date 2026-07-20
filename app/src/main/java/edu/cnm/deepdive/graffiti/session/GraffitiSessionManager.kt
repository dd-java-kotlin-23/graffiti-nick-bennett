package edu.cnm.deepdive.graffiti.session

import android.app.Activity
import edu.cnm.deepdive.graffiti.model.domain.User
import edu.cnm.deepdive.graffiti.repository.AuthRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
internal class GraffitiSessionManager @Inject constructor(
    private val authRepository: AuthRepository,
    // TODO: Add additional dependencies for web service proxy, etc.
) : SessionManager {

    private val mutex = Mutex()
    private val mutableState = MutableStateFlow<SessionState>(SessionState.SignedOut)

    override val state: StateFlow<SessionState> = mutableState.asStateFlow()

    override suspend fun signIn(activity: Activity): User =
        mutex.withLock {
            val credential = try {
                authRepository.signInQuickly(activity)
            } catch (_: AuthRepository.SignInRequiredException) {
                authRepository.signIn(activity)
            }
            // TODO: Get user info from web service.
            val user = User(credential.displayName, credential.subject)
            mutableState.value = SessionState.SignedIn(user, credential)
            user
        }

    override fun getCurrentUser(): User? =
        (mutableState.value as? SessionState.SignedIn)?.user

    override suspend fun getAuthorizationHeader(activity: Activity): String =
        mutex.withLock {
            val signedIn = (mutableState.value as? SessionState.SignedIn)
                ?: throw IllegalStateException("No signed-in user session")
            val credential = authRepository.refreshToken(activity, signedIn.credential)
            mutableState.value = signedIn.copy(credential = credential)
            "Bearer ${credential.idToken}"
        }

    override suspend fun signOut() =
        mutex.withLock {
            authRepository.signOut()
        }
}