package com.svbackend.natai.android.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.svbackend.natai.android.DiaryApplication
import com.svbackend.natai.android.entity.ExistingAttachmentDto
import com.svbackend.natai.android.entity.ExistingLocalAttachmentDto
import com.svbackend.natai.android.entity.LocalNote
import com.svbackend.natai.android.entity.User
import com.svbackend.natai.android.query.UserQueryException
import com.svbackend.natai.android.repository.DiaryRepository
import com.svbackend.natai.android.repository.UserRepository
import com.svbackend.natai.android.service.ApiSyncService
import com.svbackend.natai.android.ui.UserTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class NoteViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "NoteViewModel"

    val diaryRepository: DiaryRepository =
        (application as DiaryApplication).appContainer.diaryRepository
    val userRepository: UserRepository =
        (application as DiaryApplication).appContainer.userRepository

    val apiSyncService: ApiSyncService =
        (application as DiaryApplication).appContainer.apiSyncService

    val attachmentsLoader = (application as DiaryApplication).appContainer.attachmentsLoader

    val prefs = (application as DiaryApplication).appContainer.sharedPrefs

    val userCloudId = MutableSharedFlow<String?>(replay = 1)
    val userCloudIdState = mutableStateOf<String?>(null)
    val users = userRepository.users
    val user = MutableSharedFlow<User?>(replay = 1)
    var userState by mutableStateOf<User?>(null)

    val currentTheme = MutableSharedFlow<UserTheme>(replay = 1)

    val notes = diaryRepository.notes
    var notesState by mutableStateOf(emptyList<LocalNote>()) // current user's notes
    var allNotesState by mutableStateOf(emptyList<LocalNote>())

    val selectedNote = MutableSharedFlow<LocalNote?>(replay = 1)
    val selectedNoteAttachments = mutableStateOf(emptyList<ExistingLocalAttachmentDto>())
    val selectedAttachment = mutableStateOf<ExistingLocalAttachmentDto?>(null)

    fun selectNote(id: String) = viewModelScope.launch {
        selectedNote.emit(null)
        selectedNoteAttachments.value = emptyList()

        diaryRepository.getNote(id)
            .collect {
                val localNote = LocalNote.create(it)
                selectedNote.emit(localNote)
                loadAttachments(localNote)
            }
    }

    fun selectAttachment(attachment: ExistingLocalAttachmentDto) {
        selectedAttachment.value = attachment
    }

    fun clearSelectedAttachment() {
        selectedAttachment.value = null
    }

    fun loadAttachments(note: LocalNote) {
        try {
            val loadedAttachments = attachmentsLoader.loadLocalAttachments(note)
            selectedNoteAttachments.value = loadedAttachments
        } catch (e: Exception) {
            selectedNoteAttachments.value = emptyList()
            Log.v(TAG, "Failed to load attachments", e)
        }
    }

    val isSyncing = MutableSharedFlow<Boolean>()

    suspend fun startSync() {
        isSyncing.emit(true)
    }

    suspend fun finishSync() {
        isSyncing.emit(false)
    }

    suspend fun sync() {
        startSync()
        try {
            apiSyncService.syncNotes()
        } catch (userError: UserQueryException) {
            logout { }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            finishSync()
        }
    }

    fun deleteNote(note: LocalNote) {
        viewModelScope.launch {
            diaryRepository.deleteNoteAndSync(note)
        }
    }

    fun changeTheme(theme: UserTheme) {
        viewModelScope.launch {
            currentTheme.emit(theme)
        }
    }

    suspend fun setUserCloudId(cloudId: String?) {
        this.userCloudId.emit(cloudId)
    }

    suspend fun setUser(user: User) {
        prefs.edit()
            .putString("api_token", user.apiToken)
            .putString("cloud_id", user.cloudId)
            .apply()

        setUserCloudId(user.cloudId)

        diaryRepository.assignNotesToUser(user.cloudId)

        sync()
    }

    private val jobs = mutableListOf<Job>()

    init {
        viewModelScope.launch {
            userCloudId.collect {
                clearSubscribers()
                userCloudIdState.value = it
                setupSubscribers()
            }
        }

        setupSubscribers()
    }

    private fun setupSubscribers() {
        val usersSubscriberJob = viewModelScope.launch {
            users.collect {
                if (userCloudIdState.value != null) {
                    userState = it.find { user -> user.cloudId == userCloudIdState.value }
                } else {
                    userState = null
                }
            }
        }

        val notesSubscriberJob = viewModelScope.launch {
            notes.collect {
                allNotesState = it
                notesState =
                    it.filter { note -> note.cloudUserId == null || note.cloudUserId == userCloudIdState.value }
            }
        }

        jobs.add(usersSubscriberJob)
        jobs.add(notesSubscriberJob)
    }

    private fun clearSubscribers() {
        jobs.forEach { job -> job.cancel() }
        jobs.clear()
    }

    suspend fun logout(onLogout: () -> Unit) {
        prefs
            .edit()
            .remove("api_token")
            .remove("cloud_id")
            .apply()

        setUserCloudId(null)

        onLogout()
    }

    fun selectNextAttachment() {
        val attachments = selectedNoteAttachments.value
        val currentAttachment = selectedAttachment.value

        if (attachments.isEmpty()) {
            return
        }

        if (currentAttachment == null) {
            selectAttachment(attachments.first())
            return
        }

        val currentIndex = attachments.indexOf(currentAttachment)
        val nextIndex = if (currentIndex == attachments.lastIndex) 0 else currentIndex + 1

        selectAttachment(attachments[nextIndex])
    }

    fun selectPrevAttachment() {
        val attachments = selectedNoteAttachments.value
        val currentAttachment = selectedAttachment.value

        if (attachments.isEmpty()) {
            return
        }

        if (currentAttachment == null) {
            selectAttachment(attachments.first())
            return
        }

        val currentIndex = attachments.indexOf(currentAttachment)
        val nextIndex = if (currentIndex == 0) attachments.lastIndex else currentIndex - 1

        selectAttachment(attachments[nextIndex])
    }
}