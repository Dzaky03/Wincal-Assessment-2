package com.dzaky3022.asesment1.navigation

import com.dzaky3022.asesment1.ui.model.WaterResult

const val KEY_RESULT_VALUE = "value"
const val KEY_AMOUNT_VALUE = "amount"
const val KEY_TEMP_VALUE = "temp"
const val KEY_ACT_LVL_VALUE = "actlvl"
const val KEY_GENDER_VALUE = "gender"
const val KEY_WEIGHT_VALUE = "weight"

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Form : Screen("form")
    data object Visual :
        Screen("visual/amount/{$KEY_AMOUNT_VALUE}/result/{$KEY_RESULT_VALUE}/temp/{$KEY_TEMP_VALUE}/actlvl/{$KEY_ACT_LVL_VALUE}/gender/{$KEY_GENDER_VALUE}/weight/{$KEY_WEIGHT_VALUE}") {
        fun withValue(waterResult: WaterResult) =
            "visual/amount/${waterResult.amount}/result/${waterResult.resultValue}/temp/${waterResult.roomTemp}/actlvl/${waterResult.activityLevel.name}/gender/${waterResult.gender.name}/weight/${waterResult.weight}"
    }
}