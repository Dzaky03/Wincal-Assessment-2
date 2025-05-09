package com.dzaky3022.asesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dzaky3022.asesment1.ui.model.DeletedResult
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.utils.Enums

@Dao
interface WaterResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(waterResult: WaterResult): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedData(deletedResult: DeletedResult): Long?

    @Update
    suspend fun update(waterResult: WaterResult): Int

    @Query("""
    UPDATE water_results SET 
        dataStatus = :status
    WHERE id = :id
""")
    suspend fun deleteById(id: String, status: Enums.DataStatus = Enums.DataStatus.Deleted): Int

    @Query("DELETE FROM water_results WHERE id = :waterResultId")
    suspend fun deletePermanent(waterResultId: String): Int
}