package edu.cnm.deepdive.graffiti.session

import edu.cnm.deepdive.graffiti.model.auth.AuthCredential
import edu.cnm.deepdive.graffiti.model.domain.User

sealed interface SessionState {

    data object SignedOut : SessionState

    data class SignedIn(
        val user: User,
        val credential: AuthCredential,
    ): SessionState

}