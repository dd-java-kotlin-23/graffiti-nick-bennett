package edu.cnm.deepdive.graffiti.model.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "user_profile")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var id: Long? = null,

    @Column(nullable = false, updatable = false, unique = true)
    var externalKey: UUID? = null,

    @Column(nullable = false, updatable = false, unique = true)
    var oauthKey: String,

    @Column(nullable = false, updatable = true, unique = true)
    var displayName: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var created: Instant? = null,
) {

    override fun equals(other: Any?): Boolean =
        when {
            (other === null) -> false
            (other !is User) -> false
            (other === this) -> true
            else -> (other.id !== null && other.id == this.id)
        }

    override fun hashCode(): Int =
        Hibernate.getClass(this).hashCode()

    override fun toString(): String =
        "User(id=$id, externalKey=$externalKey, oauthKey='$oauthKey', displayName='$displayName', created=$created)"

    @PrePersist
    private fun generateAttributeValues() {
        externalKey = UUID.randomUUID()
    }

}