package edu.cnm.deepdive.graffiti.service

import edu.cnm.deepdive.graffiti.conversion.toBase64
import edu.cnm.deepdive.graffiti.conversion.toUUID
import edu.cnm.deepdive.graffiti.model.dto.PrivateUserProfileDto
import edu.cnm.deepdive.graffiti.model.dto.PublicUserProfileDto
import edu.cnm.deepdive.graffiti.model.dto.UserProfileUpdateDto
import edu.cnm.deepdive.graffiti.model.entity.User
import edu.cnm.deepdive.graffiti.repository.UserRepository
import java.time.OffsetDateTime
import java.time.ZoneId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
internal class UserServiceImpl @Autowired constructor(
    private val repository: UserRepository
) : UserService {

    override val currentUser: User
        get() = SecurityContextHolder.getContext().authentication?.principal as User

    override val userProfile: PrivateUserProfileDto
        get() = buildPrivateUserProfileDto(currentUser)

    override val allUsers: List<PublicUserProfileDto>
        get() = repository
            .findAll()
            .map { buildPublicUserProfileDto(it) }

    override fun getUser(externalId: String): PublicUserProfileDto =
        repository
            .findByExternalKey(externalId.toUUID())
            .map({ buildPublicUserProfileDto(it) })
            .orElseThrow()

    override fun getOrAddUser(user: User): User =
        repository
            .findByOauthKey(user.oauthKey)
            .orElseGet { repository.save(user) }

    override fun updateMe(changes: UserProfileUpdateDto): PrivateUserProfileDto {
        return repository
            .findById(currentUser.id!!)
            .map {
                it.displayName = changes.displayName
                buildPrivateUserProfileDto(repository.save(it))
            }
            .orElseThrow()
    }

    private fun buildPrivateUserProfileDto(user: User): PrivateUserProfileDto =
        PrivateUserProfileDto(
            key = user.externalKey!!.toBase64(),
            displayName = user.displayName,
            created = OffsetDateTime.ofInstant(user.created, ZoneId.systemDefault()),
            ownedCanvases = user.ownedCanvases.size,
        )

    private fun buildPublicUserProfileDto(user: User): PublicUserProfileDto =
        PublicUserProfileDto(
            key = user.externalKey!!.toBase64(),
            displayName = user.displayName,
        )
}