package com.svbackend.natai.dto

data class NoteDto(
    val id: Int = 1,
    val title: String = "Hello World",
    val content: String = "Note content",
)

data class NewNote(
    val title: String,
    val content: String
)