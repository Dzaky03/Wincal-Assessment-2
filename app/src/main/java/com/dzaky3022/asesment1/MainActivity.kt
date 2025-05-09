package com.dzaky3022.asesment1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dzaky3022.asesment1.database.AppDatabase
import com.dzaky3022.asesment1.navigation.NavGraph
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.WinCalTheme
import com.dzaky3022.asesment1.utils.DataStore
import com.dzaky3022.asesment1.utils.ViewModelFactory

const val waveGap = 30

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val dataStore = DataStore(this.applicationContext)
        val roomDb = AppDatabase.getAppDb(this.applicationContext)
        setContent {
            val savedEmail by dataStore.emailFlow.collectAsState("")
            var localUser: User? = null
            LaunchedEffect(savedEmail) {
                localUser = savedEmail.let {
                    roomDb.userDao().getUserByEmail(it)
                }
                if (localUser == null) {
                    dataStore.clearEmail()
                }
            }
            val factory = ViewModelFactory(
                localUser = localUser,
                dataStore = dataStore,
                userDao = roomDb.userDao(),
                waterResultDao = roomDb.waterResultDao(),
            )
            Log.d("MainActivity", "savedEmail: $savedEmail\nfactory: $factory")
            WinCalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    NavGraph(
                        dashboardViewModel = viewModel(factory = factory),
                        listViewModel = viewModel(factory = factory),
                        deletedListViewModel = viewModel(factory = factory),
                        formViewModel = viewModel(factory = factory),
                    )
                }
            }
        }
    }
}