package edu.cnm.deepdive.graffiti.repository

interface TokenParser {

    fun extractSubject(token: String): String

    fun isExpired(token: String): Boolean

    fun extractDisplayName(token: String): String

}