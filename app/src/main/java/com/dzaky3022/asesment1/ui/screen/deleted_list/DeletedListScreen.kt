package com.dzaky3022.asesment1.ui.screen.deleted_list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.navigation.Screen
import com.dzaky3022.asesment1.ui.component.EmptyState
import com.dzaky3022.asesment1.ui.component.ProfilDialog
import com.dzaky3022.asesment1.ui.component.PullToRefreshContainer
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.BackgroundLight
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.utils.Enums
import com.dzaky3022.asesment1.utils.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletedListScreen(
    navController: NavHostController,
    deletedListViewModel: DeletedListViewModel
) {
    val context = LocalContext.current
    val logoutStatus by deletedListViewModel.logOutStatus.collectAsState()
    val listData by deletedListViewModel.listData.collectAsState()
    val loadStatus by deletedListViewModel.loadStatus.collectAsState()
    val userData by deletedListViewModel.userData.collectAsState()
    val updateStatus by deletedListViewModel.updateStatus.collectAsState()
    var showProfileDialog by rememberSaveable { mutableStateOf(false) }
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var orientationView by rememberSaveable { mutableStateOf(Enums.OrientationView.List) }

    if (showProfileDialog)
        userData?.let {
            ProfilDialog(
                user = it,
                onDismissRequest = { showProfileDialog = false }
            ) {
                showProfileDialog = false
                deletedListViewModel.logout(context)
            }
        }

    LaunchedEffect(logoutStatus) {
        if (logoutStatus != Enums.ResponseStatus.Idle)
            Toast.makeText(context, logoutStatus.message, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(updateStatus) {
        if (updateStatus != Enums.ResponseStatus.Idle)
            Toast.makeText(context, updateStatus.message, Toast.LENGTH_SHORT).show()
    }

    PullToRefreshContainer(
        onRefreshParams = {
            deletedListViewModel.getList()
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back Button",
                                tint = Color.Unspecified
                            )
                        }
                    },
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.list_calculation_results),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showProfileDialog = !showProfileDialog }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonOutline,
                                contentDescription = stringResource(R.string.profile_icon),
                                tint = Color.White,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Water),
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(Screen.DeletedList.route)
                            }
                    ) {
                        Text("View deleted data...", fontSize = 14.sp, color = Color.Gray)
                    }
                    IconButton(onClick = {
                        orientationView =
                            if (orientationView == Enums.OrientationView.List) Enums.OrientationView.Grid else Enums.OrientationView.List
                    }) {
                        Icon(
                            imageVector = if (orientationView == Enums.OrientationView.List) Icons.Default.GridView else Icons.AutoMirrored.Filled.List,
                            contentDescription = "Toggle View"
                        )
                    }
                }
                if (listData.isNullOrEmpty()) {
                    EmptyState(
                        isLoading = loadStatus == Enums.ResponseStatus.Loading
                    )
                } else {
                    when (orientationView) {
                        Enums.OrientationView.List -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(listData!!) { item ->
                                    WaterResultItem(item) { id ->
                                        deletedListViewModel.deleteDataPermanent(id)
                                    }
                                }
                            }
                        }

                        Enums.OrientationView.Grid -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(listData!!) { item ->
                                    WaterResultItem(item) { id ->
                                        deletedListViewModel.deleteDataPermanent(id)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WaterResultItem(item: WaterResult, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = cardColors(containerColor = BackgroundLight)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Text("Room Temp: ${item.roomTemp}°C")
                Text("Weight: ${item.weight} kg")
                Text("Activity Level: ${item.activityLevel}")
                Text("Amount: ${item.amount} ml")
                Text("Result Value: ${item.resultValue}")
                Text("Percentage: ${item.percentage}%")
                Text("Gender: ${item.gender}")
                item.createdAt?.let {
                    Text("Created At: ${it.toFormattedDate(java.util.Locale.getDefault())}")
                }
            }
            IconButton(
                onClick = {
                    onDelete(item.id)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}