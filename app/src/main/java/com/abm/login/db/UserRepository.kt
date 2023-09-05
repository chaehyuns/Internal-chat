package com.abm.login.db

import androidx.lifecycle.LiveData

class UserRepository(private val dao: UserDAO) {

    // LiveData 형식의 변수는 일반 함수가 아니라 직접 접근이 가능하므로 다음과 같이 선언합니다.
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

    // clear Users
    suspend fun logout() {
        dao.logout()
    }
}
