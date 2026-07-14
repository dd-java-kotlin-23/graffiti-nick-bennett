package edu.cnm.deedpive.graffiti.model.entity

import jakarta.persistence.Embeddable

@Embeddable
class Point(
    val x: Float,
    val y: Float,
)