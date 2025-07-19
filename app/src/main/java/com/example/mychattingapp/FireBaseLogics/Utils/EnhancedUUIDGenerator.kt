package com.example.mychattingapp.FireBaseLogics.Utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.UUID

fun generateEnhancedUUID(): UUID {
    val auth = Firebase.auth
    // Generate a random UUID
    val key = auth.currentUser?.uid.toString()
    val randomUUID = UUID.randomUUID()

    // Combine the random UUID with the custom string key
    val combined = randomUUID.toString() + key

    // Hash the combined value using SHA-256
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(combined.toByteArray(StandardCharsets.UTF_8))

    // Create a new UUID from the first 16 bytes of the hash
    return UUID.nameUUIDFromBytes(hash)
}

fun generateProfilePicUUID(): UUID {
    val auth = Firebase.auth
    // Generate a random UUID
    val key = auth.currentUser?.uid.toString()
    val key2 = auth.currentUser?.email.toString()
    val randomUUID = UUID.randomUUID()

    // Combine the random UUID with the custom string key
    val combined = key2 + randomUUID.toString() + key

    // Hash the combined value using SHA-256
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(combined.toByteArray(StandardCharsets.UTF_8))

    // Create a new UUID from the first 16 bytes of the hash
    return UUID.nameUUIDFromBytes(hash)
}