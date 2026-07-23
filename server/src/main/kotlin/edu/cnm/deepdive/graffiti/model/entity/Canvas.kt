package edu.cnm.deepdive.graffiti.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.PrePersist
import java.time.Instant
import java.util.UUID
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp

@Entity
class Canvas(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "canvas_id")
    var id: Long? = null,

    @Column(nullable = false, updatable = false, unique = true)
    var externalKey: UUID? = null,

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    var owner: User,

    @Column(nullable = false, updatable = false)
    var width: Int,

    @Column(nullable = false, updatable = false)
    var height: Int,

    @Column(nullable = false, updatable = false, columnDefinition = "integer default -1")
    var backgroundColor: Int = -1,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var created: Instant? = null,

    @OneToMany(mappedBy = "canvas", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    var brushStrokes: MutableList<BrushStroke> = mutableListOf(),
) {
    override fun equals(other: Any?): Boolean =
        when {
            other === null -> false
            other !is Canvas -> false
            other === this -> true
            else -> (other.id !== null && other.id == this.id)
        }

    override fun hashCode(): Int =
        Hibernate.getClass(this).hashCode()


    override fun toString(): String =
        "Canvas(id=$id, externalKey=$externalKey, owner=$owner, width=$width, height=$height, backGroundColor=$backgroundColor, created=$created)"

    @PrePersist
    private fun generateAttributeValues() {
        externalKey = UUID.randomUUID()
    }
}