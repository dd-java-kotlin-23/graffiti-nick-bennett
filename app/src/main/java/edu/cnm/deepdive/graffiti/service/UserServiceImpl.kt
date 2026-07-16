package edu.cnm.deepdive.graffiti.service

import android.app.Activity
import edu.cnm.deepdive.graffiti.model.domain.User
import edu.cnm.deepdive.graffiti.session.SessionManager
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.future

@Singleton
internal class UserServiceImpl @Inject constructor(
    private val sessionManager: SessionManager,
) : UserService {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun signIn(activity: Activity): CompletableFuture<User> =
        scope.future {
            sessionManager.signIn(activity)
        }

    override fun signOut(): CompletableFuture<Void?> =
        scope.future {
            sessionManager.signOut()
            null
        }
}