package com.dzaky3022.asesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzaky3022.asesment1.ui.model.DeletedResult

@Dao
interface DeletedDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deletedResult: DeletedResult)

    @Query("DELETE FROM deleted_results WHERE waterResultId = :waterResultId")
    suspend fun deletePermanent(waterResultId: String)

    @Query("SELECT * FROM deleted_results WHERE uid = :uid")
    suspend fun getAllDeletedData(uid: String): List<DeletedResult>
}