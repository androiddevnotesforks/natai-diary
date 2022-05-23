package com.svbackend.natai.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.svbackend.natai.android.http.model.CloudNote
import java.util.*

@Entity
data class Note(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var cloudId: String? = null,
    var title: String,
    var content: String,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    var deletedAt: Date? = null,
) {
    fun sync(cloudNote: CloudNote) {
        if (cloudId == null) {
            cloudId = cloudNote.id
        } else if (cloudId != cloudNote.id) {
            throw Exception("Cant sync notes with different IDs (local cloud id = $cloudId vs cloud id = ${cloudNote.id}")
        }

        title = cloudNote.title
        content = cloudNote.content
    }

    fun sync(cloudNote: Note) {
        title = cloudNote.title
        content = cloudNote.content
        deletedAt = cloudNote.deletedAt
    }

    companion object {
        fun createByCloudNote(cloudNote: Note) = Note(
            cloudId = cloudNote.id,
            title = cloudNote.title,
            content = cloudNote.content,
            createdAt = cloudNote.createdAt,
            updatedAt = cloudNote.updatedAt
        )
    }
}
