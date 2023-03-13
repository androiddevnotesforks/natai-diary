package com.svbackend.natai.android.service

import android.net.Uri
import com.svbackend.natai.android.entity.AttachmentEntityDto
import com.svbackend.natai.android.entity.LocalNote
import com.svbackend.natai.android.entity.Note
import com.svbackend.natai.android.http.ApiClient
import com.svbackend.natai.android.http.model.CloudNote
import com.svbackend.natai.android.repository.DiaryRepository
import com.svbackend.natai.android.utils.isAfterSecs

class ApiSyncService(
    private val apiClient: ApiClient,
    private val repository: DiaryRepository,
    private val fileManagerService: FileManagerService
) {
    @Volatile
    private var isRunning = false

    suspend fun syncNotes() {
        if (isRunning) {
            return
        }

        isRunning = true

        apiClient.getCurrentUser()

        val cloudNotesResponse = apiClient.getNotesForSync()

        val cloudNotes = cloudNotesResponse
            .notes
            .associateBy { it.id }

        val localNotes = repository
            .getAllNotesForSync()

        localNotes.forEach {
            val cloudNote = if (it.cloudId != null) cloudNotes[it.cloudId] else null

            if (cloudNote == null) {
                insertToCloud(it)
            } else if (it.updatedAt.isAfterSecs(cloudNote.updatedAt)) {
                updateToCloud(it)
            }
        }

        cloudNotes.forEach { kv ->
            val cloudNoteId = kv.key
            val cloudNote = kv.value
            val localNote = localNotes.find { it.cloudId == cloudNoteId }

            if (localNote == null) {
                insertToLocal(cloudNote)
            } else if (cloudNote.updatedAt.isAfterSecs(localNote.updatedAt)) {
                updateToLocal(localNote, cloudNote)
            }
        }

        //cleanOldAttachments()

        isRunning = false
    }

    private suspend fun insertToLocal(cloudNote: CloudNote) {
        val newLocalNote = LocalNote.create(cloudNote)
        try {
            repository.insertNote(newLocalNote)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    private suspend fun updateToLocal(localNote: LocalNote, cloudNote: CloudNote) {
        val note = Note.create(localNote)
        note.sync(cloudNote)

        val response = apiClient.getAttachmentsByNote(cloudNote.id, cloudNote.attachments)

        val attachments = response.attachments.map {
            AttachmentEntityDto(
                uri = null,
                filename = it.key, // todo where's original filename?
                cloudAttachmentId = it.attachmentId,
            )
        }

        try {
            repository.updateNote(note)
            repository.updateAttachments(note.id, attachments)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private suspend fun insertToCloud(localNote: LocalNote) {
        try {
            val response = apiClient.addNote(localNote)
            val syncedNote = Note.create(localNote)
            syncedNote.cloudId = response.noteId

            repository.updateNote(syncedNote)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private suspend fun updateToCloud(localNote: LocalNote) {
        try {
            apiClient.updateNote(localNote)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private suspend fun cleanOldAttachments() {
        // remove all attachments files for 10th+ note
        val oldNotes = repository.getOldNotes()

        oldNotes.forEach {
            val newAttachments = mutableListOf<AttachmentEntityDto>()
            var isChanged = false
            it.attachments.forEach { attachment ->
                if (attachment.uri != null) {
                    fileManagerService.deleteFile(attachment.uri)
                    newAttachments.add(attachment.copy(uri = null))
                    isChanged = true
                } else {
                    newAttachments.add(attachment)
                }
            }

            if (isChanged) {
                repository.updateAttachments(it.id, newAttachments)
            }
        }
    }
}