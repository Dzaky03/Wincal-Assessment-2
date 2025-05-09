package com.dzaky3022.asesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzaky3022.asesment1.ui.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long?

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: String): Int

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    fun getAndListenUserByEmail(email: String): Flow<User?>
}