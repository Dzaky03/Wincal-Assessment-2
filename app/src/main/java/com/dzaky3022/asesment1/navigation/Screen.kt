package com.dzaky3022.asesment1.navigation

const val KEY_RESULT_VALUE = "value"
const val KEY_AMOUNT_VALUE = "amount"

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Form : Screen("form")
    data object OnBoard : Screen("onboarding")
    data object Visual : Screen("visual/{$KEY_AMOUNT_VALUE}/{$KEY_RESULT_VALUE}") {
        fun withValue(amount: Float, value: Float) = "visual/$amount/$value"
    }
}