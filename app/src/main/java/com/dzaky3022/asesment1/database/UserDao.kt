package com.dzaky3022.asesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.model.UserWithResults

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: String)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserWithResults(id: String): UserWithResults
}