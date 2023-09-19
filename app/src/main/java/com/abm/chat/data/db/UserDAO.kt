package com.abm.chat.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abm.chat.data.repository.user.datasource.local.User


@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User) : Long // return row ID

    @Delete
    suspend fun delete(user: User) : Int

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM user_data_table")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_data_table WHERE email = :email")
    fun getUserByEmail(email: String): LiveData<User?>

    @Query("SELECT * FROM user_data_table WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): LiveData<User?>

    @Query("DELETE FROM user_data_table")
    suspend fun logout()
}

