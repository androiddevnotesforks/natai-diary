package com.svbackend.natai.android.http.dto

import java.time.Instant

data class UpdateNoteRequest(
    val title: String,
    val content: String,
    val updatedAt: Instant,
    val deletedAt: Instant? = null
)
