package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dzaky3022.asesment1.utils.Enums.ActivityLevel
import com.dzaky3022.asesment1.utils.Enums.DataStatus
import com.dzaky3022.asesment1.utils.Enums.Gender
import com.dzaky3022.asesment1.utils.Enums.TempUnit
import com.dzaky3022.asesment1.utils.Enums.WaterUnit
import com.dzaky3022.asesment1.utils.Enums.WeightUnit
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
    var roomTemp: Float,
    var tempUnit: TempUnit? = null,
    var weight: Float,
    var weightUnit: WeightUnit? = null,
    var activityLevel: ActivityLevel,
    var drinkAmount: Float,
    var waterUnit: WaterUnit? = null,
    var resultValue: Float,
    var percentage: Float,
    var gender: Gender,
    var dataStatus: DataStatus = DataStatus.Available,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
)