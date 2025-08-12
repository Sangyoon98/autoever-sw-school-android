package com.sangyoon.userapp.repository

import com.sangyoon.userapp.model.User
import com.sangyoon.userapp.network.RetrofitInstance

class UserRepository {
    private val api = RetrofitInstance.api

    suspend fun getUsers(): List<User> {
        return api.getUsers()
    }

    suspend fun getUser(id: Long): User {
        return api.getUser(id)
    }

    suspend fun createUser(user: User): User {
        return api.createUser(user)
    }

    suspend fun updateUser(id: Long, user: User): User {
        return api.updateUser(id, user)
    }

    suspend fun deleteUser(id: Long) {
        api.deleteUser(id)
    }
}