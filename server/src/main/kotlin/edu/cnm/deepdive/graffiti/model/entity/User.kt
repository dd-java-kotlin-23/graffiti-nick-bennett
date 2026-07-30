package edu.cnm.deepdive.graffiti.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp

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

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("created DESC")
    val ownedCanvases: MutableList<Canvas> = mutableListOf(),
) {

    override fun equals(other: Any?): Boolean =
        when {
            other === null -> false
            other !is User -> false
            other === this -> true
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