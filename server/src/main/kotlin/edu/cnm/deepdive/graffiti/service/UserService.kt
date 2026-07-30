package edu.cnm.deepdive.graffiti.service

import edu.cnm.deepdive.graffiti.model.dto.PrivateUserProfileDto
import edu.cnm.deepdive.graffiti.model.dto.PublicUserProfileDto
import edu.cnm.deepdive.graffiti.model.dto.UserProfileUpdateDto
import edu.cnm.deepdive.graffiti.model.entity.User

interface UserService {

    val currentUser: User

    val userProfile: PrivateUserProfileDto

    val allUsers: List<PublicUserProfileDto>

    fun getUser(externalId: String): PublicUserProfileDto

    fun getOrAddUser(user: User): User

    fun updateMe(changes: UserProfileUpdateDto): PrivateUserProfileDto

}