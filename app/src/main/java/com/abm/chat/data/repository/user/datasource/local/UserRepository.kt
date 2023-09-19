package com.abm.chat.data.repository.user.datasource.local

import androidx.lifecycle.LiveData
import com.abm.chat.data.db.UserDAO

class UserRepository(private val dao: UserDAO) {

    val users: LiveData<List<User>> = dao.getAllUsers()

    suspend fun insert(user: User): Long {
        return dao.insert(user)
    }

    suspend fun delete(user: User): Int {
        return dao.delete(user)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }

    fun getUserByEmail(email: String): LiveData<User?> {
        return dao.getUserByEmail(email)
    }

    fun getUserByEmailAndPassword(email: String, password: String): LiveData<User?> {
        return dao.getUserByEmailAndPassword(email, password)
    }

    // clear Users
    suspend fun logout() {
        dao.logout()
    }
}