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

    @Query("""
    UPDATE water_results SET 
        dataStatus = :status
    WHERE id = :id
""")
    suspend fun restoreById(id: String, status: Enums.DataStatus = Enums.DataStatus.Available): Int

    @Query("DELETE FROM water_results WHERE id = :waterResultId")
    suspend fun deletePermanent(waterResultId: String): Int

    @Query("SELECT * FROM water_results WHERE id = :id LIMIT 1")
    suspend fun getDataById(id: String): WaterResult?

    @Query("""
    SELECT * FROM water_results
    WHERE uid = :uid AND (:status IS NULL OR dataStatus = :status)
""")
    suspend fun getDataByUser(uid: String, status: Enums.DataStatus? = null): List<WaterResult>?
}