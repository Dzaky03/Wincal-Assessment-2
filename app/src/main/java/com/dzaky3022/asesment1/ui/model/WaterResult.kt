package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity
import com.dzaky3022.asesment1.utils.Enums.*

@Entity(tableName = "water_results")
data class WaterResult (
    var id: String? = null,
    var uid: String? = null,
    val roomTemp: Float,
    val weight: Float,
    val activityLevel: ActivityLevel,
    val amount: Float,
    val resultValue: Float,
    val percentage: Float,
    val gender: Gender,
)