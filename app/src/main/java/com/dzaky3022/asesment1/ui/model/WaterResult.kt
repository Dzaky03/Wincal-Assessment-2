package com.dzaky3022.asesment1.ui.model

import com.dzaky3022.asesment1.utils.Enums.*

data class WaterResult (
    val roomTemp: Float,
    val weight: Float,
    val activityLevel: ActivityLevel,
    val amount: Float,
    val resultValue: Float,
    val percentage: Float,
    val gender: Gender,
)