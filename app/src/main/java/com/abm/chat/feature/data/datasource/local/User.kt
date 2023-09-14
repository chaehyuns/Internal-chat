package com.abm.chat.feature.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_data_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val email: String,
    val password: String?,
    val loginType: LoginType
)

enum class LoginType {
    LOCAL, KAKAO
}