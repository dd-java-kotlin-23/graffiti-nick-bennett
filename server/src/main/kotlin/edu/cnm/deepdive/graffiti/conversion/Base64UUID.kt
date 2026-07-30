package edu.cnm.deepdive.graffiti.conversion

import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

fun UUID.toBase64(): String {
    val buffer: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    buffer.putLong(this.mostSignificantBits)
    buffer.putLong(this.leastSignificantBits)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array())
}

fun String.toUUID(): UUID {
    val bytes = Base64.getUrlDecoder().decode(this)
    val buffer = ByteBuffer.wrap(bytes)
    return UUID(buffer.long, buffer.long)
}
