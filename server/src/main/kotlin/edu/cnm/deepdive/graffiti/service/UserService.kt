package edu.cnm.deepdive.graffiti.service

import edu.cnm.deepdive.graffiti.model.entity.User
import java.util.UUID

interface UserService {

    val currentUser: User

    val allUsers: List<User>

    fun getUser(externalId: UUID): User

    fun getOrAddUser(user: User): User

    fun updateMe(changes: User): User

}