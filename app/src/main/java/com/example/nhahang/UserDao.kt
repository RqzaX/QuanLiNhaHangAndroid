package com.example.nhahang

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun login(username: String, password: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    fun kiemTraUsername(username: String): Int

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE gmail = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): User?

    @Query("UPDATE users SET password = :newPassword WHERE gmail = :email")
    suspend fun updatePassword(email: String, newPassword: String)

    @Query("SELECT * FROM users WHERE password = :password LIMIT 1")
    suspend fun kiemTraMatKhau(password: String): User?
}
