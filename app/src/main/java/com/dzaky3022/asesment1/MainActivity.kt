package com.dzaky3022.asesment1

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dzaky3022.asesment1.database.AppDatabase
import com.dzaky3022.asesment1.navigation.NavGraph
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.ui.theme.WinCalTheme
import com.dzaky3022.asesment1.utils.DataStore
import com.dzaky3022.asesment1.utils.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

const val waveGap = 30

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val dataStore = DataStore(this.applicationContext)
        val roomDb = AppDatabase.getAppDb(this.applicationContext)
        setContent {
            WinCalTheme {
                val coroutineScope = rememberCoroutineScope()
                val emailStateFlow = remember {
                    dataStore.emailFlow
                        .stateIn(
                            scope = coroutineScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = ""
                        )
                }

                val localUserFlow = remember {
                    emailStateFlow
                        .flatMapLatest { email ->
                            roomDb.userDao().getAndListenUserByEmail(email)
                        }
                        .stateIn(
                            scope = coroutineScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = null
                        )
                }
                var isSplashOver by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(500)
                    isSplashOver = true
                }

                if (!isSplashOver) {
                    // Simple splash screen
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier
                                .size(250.dp)
                                .offset(y = (-30).dp)
                                .align(Alignment.CenterHorizontally),
                            painter = painterResource(id = R.drawable.app_logo_2),
                            contentDescription = stringResource(R.string.app_logo),
                            colorFilter = ColorFilter.tint(
                                color = Water,
                                blendMode = BlendMode.SrcIn
                            )
                        )
                        Spacer(
                            Modifier.height(
                                24.dp
                            )
                        )
                        Text(
                            text = stringResource(R.string.initializing),
                            color = BackgroundDark,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(
                            Modifier.height(
                                24.dp
                            )
                        )
                        CircularProgressIndicator(
                            color = Water
                        )
                    }
                } else {
                    val factory = ViewModelFactory(
                        localUser = localUserFlow,
                        dataStore = dataStore,
                        userDao = roomDb.userDao(),
                        waterResultDao = roomDb.waterResultDao(),
                    )
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = BackgroundDark
                    ) {
                        NavGraph(
                            dashboardViewModel = viewModel(factory = factory),
                            listViewModel = viewModel(factory = factory),
                            deletedListViewModel = viewModel(factory = factory),
                            waterResultDao = roomDb.waterResultDao(),
                            localUser = localUserFlow,
                        )
                    }
                }
            }
        }
    }
}