package com.dzaky3022.asesment1.navigation


sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Form : Screen("form")
    data object OnBoard : Screen("onboarding")
    data object Visual : Screen("visual")
}