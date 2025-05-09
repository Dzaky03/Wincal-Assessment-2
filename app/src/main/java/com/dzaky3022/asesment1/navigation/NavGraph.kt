package com.dzaky3022.asesment1.navigation

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dzaky3022.asesment1.database.WaterResultDao
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaveParams
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.ui.screen.dashboard.DashboardScreen
import com.dzaky3022.asesment1.ui.screen.dashboard.DashboardViewModel
import com.dzaky3022.asesment1.ui.screen.deleted_list.DeletedListScreen
import com.dzaky3022.asesment1.ui.screen.deleted_list.DeletedListViewModel
import com.dzaky3022.asesment1.ui.screen.form.FormScreen
import com.dzaky3022.asesment1.ui.screen.list.ListScreen
import com.dzaky3022.asesment1.ui.screen.list.ListViewModel
import com.dzaky3022.asesment1.ui.screen.visual.VisualScreen
import com.dzaky3022.asesment1.ui.theme.Poppins
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.utils.Enums.ActivityLevel
import com.dzaky3022.asesment1.utils.Enums.Gender
import com.dzaky3022.asesment1.utils.ViewModelFactory
import com.dzaky3022.asesment1.waveGap
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NavGraph(
    dashboardViewModel: DashboardViewModel,
    listViewModel: ListViewModel,
    deletedListViewModel: DeletedListViewModel,
    waterResultDao: WaterResultDao,
    localUser: StateFlow<User?>,
) {
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
                dashboardViewModel = dashboardViewModel,
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
                formViewModel = viewModel(
                    factory = ViewModelFactory(
                        localUser = localUser,
                        waterResultDao = waterResultDao,
                    )
                ),
            )
        }
        composable(Screen.UpdateForm.route,
            arguments = listOf(
                navArgument(KEY_DATA_ID) {
                    type = NavType.StringType
                },
            ),
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn()
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }
        ) {
            val dataId = it.arguments?.getString(KEY_DATA_ID) ?: ""

            FormScreen(
                navController = navController,
                formViewModel = viewModel(
                    factory = ViewModelFactory(
                        localUser = localUser,
                        waterResultId = dataId,
                        waterResultDao = waterResultDao,
                    )
                ),
            )
        }
        composable(Screen.Visual.route, arguments = listOf(
            navArgument(KEY_AMOUNT_VALUE) {
                type = NavType.FloatType
            },
            navArgument(KEY_RESULT_VALUE) {
                type = NavType.FloatType
            },
            navArgument(KEY_TEMP_VALUE) {
                type = NavType.FloatType
            },
            navArgument(KEY_ACT_LVL_VALUE) {
                type = NavType.StringType
            },
            navArgument(KEY_GENDER_VALUE) {
                type = NavType.StringType
            },
            navArgument(KEY_WEIGHT_VALUE) {
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
            val temp = it.arguments?.getFloat(KEY_TEMP_VALUE) ?: 0f
            val actLvl = it.arguments?.getString(KEY_ACT_LVL_VALUE) ?: ""
            val gender = it.arguments?.getString(KEY_GENDER_VALUE) ?: ""
            val weight = it.arguments?.getFloat(KEY_WEIGHT_VALUE) ?: 0f
            val percentage = amount / resultValue * 100
            Log.d("NavGraph", "amount: $amount, result: $resultValue, percentage: $percentage")
            val activityLevel = ActivityLevel.valueOf(actLvl)
            val genderEnum = Gender.valueOf(gender)
            val waterResult = WaterResult(
                drinkAmount = amount,
                resultValue = resultValue,
                roomTemp = temp,
                activityLevel = activityLevel,
                gender = genderEnum,
                weight = weight,
                percentage = percentage,
            )
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
        composable(Screen.List.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn()
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }
        ) {
            ListScreen(
                navController = navController,
                listViewModel = listViewModel,
            )
        }
        composable(Screen.DeletedList.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn()
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }
        ) {
            DeletedListScreen(
                navController = navController,
                deletedListViewModel = deletedListViewModel,
            )
        }
    }
}
