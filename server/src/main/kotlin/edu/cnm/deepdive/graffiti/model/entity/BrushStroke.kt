package edu.cnm.deepdive.graffiti.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
class BrushStroke(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "canvas_id")
    val id: Long? = null,

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "canvas_id", nullable = false, updatable = false)
    var canvas: Canvas,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "contributor_id", nullable = false, updatable = false)
    var contributor: User,

    @Column(nullable = false, updatable = false)
    var color: Int,

    @Column(nullable = false, updatable = false)
    var width: Int,

    @ElementCollection
    @CollectionTable(
        name = "brush_stroke_point",
        joinColumns = [JoinColumn(name = "brush_stroke_id")],
    )
    @OrderColumn(name = "point_order")
    var points: MutableList<Point> = mutableListOf(),

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var added: Instant? = null,
) {

    override fun equals(other: Any?): Boolean =
        when {
            other === null -> false
            other !is BrushStroke -> false
            other === this -> true
            else -> (other.id !== null && other.id == this.id)
        }

    override fun hashCode(): Int =
        javaClass.hashCode()

    override fun toString(): String =
        "BrushStroke(id=$id, canvas=$canvas, contributor=$contributor, color=$color, width=$width, added=$added)"

}