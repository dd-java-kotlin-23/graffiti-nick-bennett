package edu.cnm.deepdive.graffiti.service

import android.app.Activity
import edu.cnm.deepdive.graffiti.model.domain.User
import java.util.concurrent.CompletableFuture

interface UserService {

    fun signIn(activity: Activity): CompletableFuture<User>

    fun signOut(): CompletableFuture<Void?>

}