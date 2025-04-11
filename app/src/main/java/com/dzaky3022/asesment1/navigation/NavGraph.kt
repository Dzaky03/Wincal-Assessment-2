package com.dzaky3022.asesment1.navigation

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dzaky3022.asesment1.ui.component.animating.WaterLevelState
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaveParams
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.ui.screen.DashboardScreen
import com.dzaky3022.asesment1.ui.screen.FormScreen
import com.dzaky3022.asesment1.ui.screen.VisualScreen
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.Poppins
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.waveGap

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val points = remember { screenWidth / waveGap }

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
    ) {
        composable(Screen.Dashboard.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn()
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }
        )
        {
            DashboardScreen(
                navController = navController,
            )
        }
        composable(Screen.Form.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn()
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }
            ) {
            FormScreen(
                navController = navController,
            )
        }
        composable(Screen.Visual.route, arguments = listOf(
            navArgument(KEY_AMOUNT_VALUE) {
                type = NavType.FloatType
            },
            navArgument(KEY_RESULT_VALUE) {
                type = NavType.FloatType
            },
        ),
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn()
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }
        ) {
            val amount = it.arguments?.getFloat(KEY_AMOUNT_VALUE) ?: 0f
            val resultValue = it.arguments?.getFloat(KEY_RESULT_VALUE) ?: 0f
            val percentage = amount/resultValue*100
            Log.d("NavGraph", "amount: $amount, result: $resultValue, percentage: $percentage")
            val waterResult = WaterResult(amount, resultValue, percentage)
            VisualScreen(
                modifier = Modifier,
                waveDurationInMills = 3000L,
                waterResult = waterResult,
                navController = navController,
            ) {
                WaterDropText(
                    modifier = Modifier,
                    align = Alignment.Center,
                    textStyle = TextStyle(
                        color = Water,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                    ),
                    waveParams = WaveParams(
                        pointsQuantity = points,
                        maxWaveHeight = 30f
                    )
                )
            }
        }
    }
}
