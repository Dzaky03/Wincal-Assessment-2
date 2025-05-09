package com.dzaky3022.asesment1.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.BackgroundLight
import com.dzaky3022.asesment1.ui.theme.Danger
import com.dzaky3022.asesment1.utils.roundUpTwoDecimals
import com.dzaky3022.asesment1.utils.toFormattedDate

@Composable
fun WaterResultItem(
    label: String,
    item: WaterResult,
    isRestore: Boolean = false,
    onEditOrRestore: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = cardColors(containerColor = BackgroundLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, fontWeight = FontWeight.SemiBold)
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    onDelete(item.id)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = Danger
                                )
                            }
                            IconButton(
                                onClick = {
                                    onEditOrRestore(item.id)
                                },
                            ) {
                                Icon(
                                    imageVector = if (isRestore) Icons.Default.History else Icons.Default.Edit,
                                    contentDescription = if (isRestore) stringResource(R.string.restore) else stringResource(
                                        R.string.edit
                                    ),
                                    tint = BackgroundDark
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Room Temp: ${item.roomTemp} ${item.tempUnit ?: "°C"}")
                    Text("Weight: ${item.weight} ${item.weightUnit ?: "kg"}")
                    Text("Activity Level: ${item.activityLevel}")
                    Text("Drink Amount: ${item.drinkAmount} ${item.waterUnit ?: "ml"}")
                    Text("Result Value: ${item.resultValue} ml")
                    Text("Percentage: ${item.percentage.roundUpTwoDecimals()}%")
                    Text("Gender: ${item.gender}")

                    item.createdAt?.let {
                        Text("Created At: ${it.toFormattedDate(java.util.Locale.getDefault())}")
                    }
                    item.updatedAt?.let {
                        Text("Updated At: ${it.toFormattedDate(java.util.Locale.getDefault())}")
                    }
                }
            }
        }
    }
}