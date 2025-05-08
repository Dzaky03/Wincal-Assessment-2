package com.dzaky3022.asesment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzaky3022.asesment1.ui.model.WaterResult

@Dao
interface SavedDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: WaterResult)

    @Query("DELETE FROM water_results WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM water_results where uid = :uid")
    suspend fun getAllData(uid: String): List<WaterResult>

    @Query("SELECT * FROM water_results WHERE id = :id LIMIT 1")
    suspend fun getDetailData(id: String): WaterResult?
}