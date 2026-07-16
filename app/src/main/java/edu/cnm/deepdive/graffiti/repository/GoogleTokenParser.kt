package edu.cnm.deepdive.graffiti.repository

import android.util.Base64
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.json.JSONObject

@Singleton
internal class GoogleTokenParser @Inject constructor() : TokenParser {

    override fun extractSubject(token: String): String {
        val payload = token.split(".")[1]
        val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP))
        return JSONObject(decodedPayload).getString("sub")
    }

    override fun isExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) {
                true
            } else {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
                val expiration = JSONObject(payload).getLong("exp")
                expiration < System.currentTimeMillis() / 1000 + 300
            }
        } catch (e: Exception) {
            true
        }
    }

    override fun extractDisplayName(token: String): String {
        val payload = token.split(".")[1]
        val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP))
        return JSONObject(decodedPayload).getString("name")
    }

}