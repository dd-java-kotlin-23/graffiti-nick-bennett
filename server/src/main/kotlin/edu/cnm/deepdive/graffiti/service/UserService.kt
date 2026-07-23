package edu.cnm.deepdive.graffiti.service

import edu.cnm.deepdive.graffiti.model.entity.User

interface UserService {

    val currentUser: User

    val allUsers: List<User>

    fun getUser(externalId: String)

    fun getOrAddUser(oauthKey: String, user: User)

    fun updateMe(changes: User)

}