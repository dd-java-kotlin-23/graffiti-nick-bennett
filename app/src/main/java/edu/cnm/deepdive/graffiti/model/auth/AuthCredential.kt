package edu.cnm.deepdive.graffiti.model.auth

data class AuthCredential(
    val idToken: String,
    val subject: String,
    val displayName: String,
)
