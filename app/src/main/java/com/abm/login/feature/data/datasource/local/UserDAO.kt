package com.abm.login.feature.data.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*


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

