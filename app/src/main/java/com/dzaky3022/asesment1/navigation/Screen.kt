package com.dzaky3022.asesment1.navigation

import com.dzaky3022.asesment1.ui.model.WaterResult

const val KEY_RESULT_VALUE = "value"
const val KEY_AMOUNT_VALUE = "amount"
const val KEY_TEMP_VALUE = "temp"
const val KEY_ACT_LVL_VALUE = "actlvl"
const val KEY_GENDER_VALUE = "gender"
const val KEY_WEIGHT_VALUE = "weight"
const val KEY_DATA_ID = "dataId"
const val KEY_USE_FAB = "fab"

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Form : Screen("form/{$KEY_DATA_ID}?useFab={${KEY_USE_FAB}}") {
        fun withParams(id: String? = null, useFab: Boolean = false) =
            "form/$id?useFab=$useFab"
    }

    data object Visual :
        Screen("visual?amount={$KEY_AMOUNT_VALUE}&result={$KEY_RESULT_VALUE}&temp={$KEY_TEMP_VALUE}&actlvl={$KEY_ACT_LVL_VALUE}&gender={$KEY_GENDER_VALUE}&weight={$KEY_WEIGHT_VALUE}") {
        fun withValue(waterResult: WaterResult) =
            "visual?amount=${waterResult.drinkAmount}&result=${waterResult.resultValue}&temp=${waterResult.roomTemp}&actlvl=${waterResult.activityLevel.name}&gender=${waterResult.gender.name}&weight=${waterResult.weight}"
    }

    data object List : Screen("list")
    data object DeletedList : Screen("deleted-list")
}