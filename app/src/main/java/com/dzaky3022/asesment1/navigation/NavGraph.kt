package com.dzaky3022.asesment1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dzaky3022.asesment1.navigate
import com.dzaky3022.asesment1.ui.component.animating.WaterLevelState
import com.dzaky3022.asesment1.ui.screen.DashboardScreen
import com.dzaky3022.asesment1.ui.screen.FormScreen
import com.dzaky3022.asesment1.ui.screen.VisualScreen
import com.dzaky3022.asesment1.ui.theme.Poppins
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.waveGap

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val points = remember { screenWidth / waveGap }
    var waterLevelState by remember { mutableStateOf(WaterLevelState.StartReady) }

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                navController = navController,
            )
        }
        composable(Screen.Form.route) {
            FormScreen()
        }
        composable(Screen.Visual.route) {
            VisualScreen(
                modifier = Modifier,
                waveDurationInMills = 3000L,
                waterLevelState = waterLevelState,
                onWavesClick = {
                    waterLevelState =
                        if (waterLevelState == WaterLevelState.Animating) {
                            WaterLevelState.StartReady
                        } else {
                            WaterLevelState.Animating
                        }
                },
                onBackPressed = {
                    navigate(navController, Screen.Dashboard.route)
                }
            ) {
                com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText(
                    modifier = Modifier,
                    align = Alignment.Center,
                    textStyle = TextStyle(
                        color = Water,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                    ),
                    waveParams = com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaveParams(
                        pointsQuantity = points,
                        maxWaveHeight = 30f
                    )
                )
            }
        }
    }
}
