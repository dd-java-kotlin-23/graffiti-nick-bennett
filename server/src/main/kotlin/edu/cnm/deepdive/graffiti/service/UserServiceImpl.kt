package edu.cnm.deepdive.graffiti.service

import edu.cnm.deepdive.graffiti.model.entity.User
import edu.cnm.deepdive.graffiti.repository.UserRepository
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
internal class UserServiceImpl @Autowired constructor(
    private val repository: UserRepository
) : UserService {

    override val currentUser: User
        get() = SecurityContextHolder.getContext().authentication?.principal as User

    override val allUsers: List<User>
        get() = repository.findAll()

    override fun getUser(externalId: UUID): User =
        repository
            .findByExternalKey(externalId)
            .orElseThrow()

    override fun getOrAddUser(user: User): User =
        repository
            .findByOauthKey(user.oauthKey)
            .orElseGet { repository.save(user) }

    override fun updateMe(changes: User): User {
        TODO("Not yet implemented")
    }
}