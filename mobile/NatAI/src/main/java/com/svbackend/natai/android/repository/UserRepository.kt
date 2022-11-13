package com.svbackend.natai.android.repository

import com.svbackend.natai.android.DiaryDatabase
import com.svbackend.natai.android.entity.User
import com.svbackend.natai.android.http.ApiClient
import com.svbackend.natai.android.http.request.LoginRequest
import com.svbackend.natai.android.http.request.RegisterRequest
import com.svbackend.natai.android.http.dto.CloudUserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val db: DiaryDatabase,
    private val api: ApiClient
) {
    suspend fun getUserByCloudId(cloudId: String): Flow<User?> = withContext(Dispatchers.IO) {
        db
            .userDAO()
            .getUserByCloudId(cloudId)
    }

    suspend fun getUserByCloudIdSync(cloudId: String): User? = withContext(Dispatchers.IO) {
        db
            .userDAO()
            .getUserByCloudIdSync(cloudId)
    }

    suspend fun login(email: String, password: String): User = withContext(Dispatchers.IO) {
        val response = api.login(
            LoginRequest(
                email,
                password
            )
        )

        val userId = response.user.id
        val newApiToken = response.apiToken

        val existingUser = db.userDAO().getUserByCloudIdSync(userId.toString())

        if (existingUser == null) {
            val cloudUser = response.user
            val newLocalUser = User(
                cloudId = userId.toString(),
                email = email,
                apiToken = newApiToken,
                name = cloudUser.name,
            )

            db.userDAO().insertUser(newLocalUser)
            return@withContext newLocalUser
        } else {
            val updatedUser = existingUser.copy(
                apiToken = newApiToken
            )

            db.userDAO().updateUser(updatedUser)
            return@withContext updatedUser
        }

    }

    suspend fun register(email: String, name: String, password: String): User {
        api.register(
            RegisterRequest(
                name = name,
                email = email,
                password = password
            )
        )

        return login(email, password)
    }

    suspend fun updateUser(user: CloudUserDto) {
        val entity = getUserByCloudIdSync(user.id.toString()) ?: return

        if (entity.isEmailVerified == user.isEmailVerified) {
            return
        }

        val updatedEntity = entity.copy(
            isEmailVerified = user.isEmailVerified
        )

        db.userDAO().updateUser(updatedEntity)
    }
}