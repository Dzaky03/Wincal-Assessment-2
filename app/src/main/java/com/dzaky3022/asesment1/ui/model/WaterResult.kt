package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dzaky3022.asesment1.utils.Enums.*
import org.threeten.bp.Instant
import java.util.UUID

@Entity(
    tableName = "water_results",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["uid"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["uid"])]
)
data class WaterResult (
    @PrimaryKey var id: String = UUID.randomUUID().toString().take(8),
    var uid: String? = null,
    val roomTemp: Float,
    val weight: Float,
    val activityLevel: ActivityLevel,
    val amount: Float,
    val resultValue: Float,
    val percentage: Float,
    val gender: Gender,
    val dataStatus: DataStatus = DataStatus.Available,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)