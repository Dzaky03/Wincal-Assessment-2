package com.dzaky3022.asesment1.ui.screen.form

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.navigation.Screen
import com.dzaky3022.asesment1.ui.component.CustomInput
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.ui.theme.BackgroundLight
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.Danger
import com.dzaky3022.asesment1.ui.theme.Gray
import com.dzaky3022.asesment1.ui.theme.IconBackgroundGray
import com.dzaky3022.asesment1.utils.Enums.*
import java.math.RoundingMode


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    var weight by rememberSaveable { mutableStateOf("") }
    var weightUnit by rememberSaveable { mutableStateOf(WeightUnit.Kilogram) }
    var temp by rememberSaveable { mutableStateOf("") }
    var tempUnit by rememberSaveable { mutableStateOf(TempUnit.Celsius) }
    var activityLevel by rememberSaveable { mutableStateOf(ActivityLevel.Low) }
    var gender by rememberSaveable { mutableStateOf(Gender.Male) }
    var isEnabled by rememberSaveable { mutableStateOf(false) }
    var drinkAmount by rememberSaveable { mutableStateOf("") }
    var waterUnit by rememberSaveable { mutableStateOf(WaterUnit.ml) }
    var isNext by rememberSaveable { mutableStateOf(false) }


    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    LaunchedEffect(isNext, weight, temp, drinkAmount) {
        isEnabled = if (isNext) {
            weight.isNotEmpty() && temp.isNotEmpty()
        } else {
            drinkAmount.isNotEmpty()
        }
    }

        Scaffold(
        modifier = modifier,
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(IconBackgroundGray),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                            onClick = {
                                if (isNext)
                                    isNext = false
                                else
                                    navController.popBackStack()
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.back_button),
                                tint = Color.Unspecified
                            )
                        }
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.water_intake_calculator_text),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { paddingValues ->
        AnimatedContent(
            targetState = isNext,
            transitionSpec = {
                slideInVertically(
                    initialOffsetY = { fullWidth -> if (targetState) fullWidth else -fullWidth },
                    animationSpec = tween(500)
                ) togetherWith slideOutVertically(
                    targetOffsetY = { fullWidth -> if (targetState) -fullWidth else fullWidth },
                    animationSpec = tween(850)
                )
            },
            label = "AnimatedContent"
        ) { targetState ->
            if (targetState)
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        CustomInput(
                            isRequired = true,
                            isDigitOnly = true,
                            label = stringResource(R.string.room_temp),
                            hint = stringResource(R.string.enter_your, stringResource(R.string.room_temp)),
                            initialValue = temp,
                            onChange = { temp = it },
                            isSuffixDropdown = true,
                            options = TempUnit.entries,
                            selectedOption = tempUnit,
                            optionLabel = { it?.symbol ?: "-" },
                            onOptionSelected = { unit -> if (unit != null) tempUnit = unit }
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomInput(
                            imeAction = ImeAction.Done,
                            isRequired = true,
                            isDigitOnly = true,
                            label = stringResource(R.string.weight),
                            hint = stringResource(R.string.enter_your, stringResource(R.string.weight)),
                            initialValue = weight,
                            onChange = { weight = it },
                            isSuffixDropdown = true,
                            options = WeightUnit.entries,
                            selectedOption = weightUnit,
                            optionLabel = { it?.symbol ?: "-" },
                            onOptionSelected = { unit -> if (unit != null) weightUnit = unit }
                        )
                        Spacer(Modifier.height(10.dp))
                        RadioButtonGroup(
                            context = context,
                            label = stringResource(R.string.activity_exercise_level),
                            options = ActivityLevel.entries,
                            selectedOption = activityLevel,
                            onOptionSelected = { activityLevel = it }
                        )
                        RadioButtonGroup(
                            context = context,
                            label =stringResource(R.string.gender),
                            direction = Direction.Horizontal,
                            options = Gender.entries,
                            selectedOption = gender,
                            onOptionSelected = { gender = it }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            enabled = isEnabled,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val value = calculateWaterIntake(
                                    weight.toDouble(),
                                    temp.toDouble(),
                                    gender,
                                    tempUnit,
                                    weightUnit,
                                    activityLevel
                                ).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                                    .toFloat()
                                navController.navigate(
                                    Screen.Visual.withValue(
                                        WaterResult(
                                            amount = convertToML(drinkAmount.toDouble(), waterUnit).toBigDecimal()
                                                .setScale(2, RoundingMode.HALF_UP).toFloat(),
                                            resultValue = value,
                                            roomTemp = temp.toFloat(),
                                            gender = gender,
                                            weight = weight.toFloat(),
                                            activityLevel = activityLevel,
                                            percentage = 0f,
                                        )
                                    )
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isPressed) Color.White else BackgroundDark,
                                disabledContainerColor = IconBackgroundGray
                            ),
                            border = BorderStroke(1.dp, if (isEnabled) BackgroundDark else Gray),
                            interactionSource = interactionSource,
                        ) {
                            Text(
                                text = stringResource(R.string.calculate),
                                fontSize = 16.sp,
                                color = if (isEnabled) if (isPressed) BackgroundDark else Color.White else BackgroundDark,
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            else
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.glass_water_illustration_jpg),
                            contentDescription = null
                        )
                        CustomInput(
                            imeAction = ImeAction.Done,
                            isRequired = true,
                            isDigitOnly = true,
                            label = stringResource(R.string.how_much_water_did_you_drink_today),
                            hint = stringResource(R.string.enter_your_amount),
                            initialValue = drinkAmount,
                            onChange = { drinkAmount = it },
                            isSuffixDropdown = true,
                            options = WaterUnit.entries,
                            selectedOption = waterUnit,
                            optionLabel = { it?.name ?: "-" },
                            onOptionSelected = { unit -> if (unit != null) waterUnit = unit }
                        )
                    }
                    Button(
                        enabled = isEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (drinkAmount.toDoubleOrNull() != null)
                                isNext = true
                            else Toast.makeText(
                                context,
                                context.getString(R.string.please_make_sure_you_have_entered_the_correct_number_format),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isPressed) Color.White else BackgroundDark,
                            disabledContainerColor = IconBackgroundGray
                        ),
                        border = BorderStroke(1.dp, if (isEnabled) BackgroundDark else Gray),
                        interactionSource = interactionSource,
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            fontSize = 16.sp,
                            color = if (isEnabled) if (isPressed) BackgroundDark else Color.White else BackgroundDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
        }
    }
}


@Composable
private fun <T> RadioButtonGroup(
    context: Context,
    label: String,
    direction: Direction = Direction.Horizontal,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp),
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "*", color = Danger, fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (direction == Direction.Horizontal)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option == selectedOption),
                                onClick = { onOptionSelected(option) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = BackgroundDark)
                        )
                        Text(
                            text = when (option) {
                                is ActivityLevel -> option.getLabel(context)
                                is Gender -> option.getLabel(context)
                                else -> option.toString()
                            },
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        else
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) },
                        colors = RadioButtonDefaults.colors(selectedColor = BackgroundDark)
                    )
                    Text(
                        text = when (option) {
                            is ActivityLevel -> option.getLabel(context)
                            is Gender -> option.getLabel(context)
                            else -> option.toString()
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
    }
}


private fun calculateWaterIntake(
    weight: Double,
    temp: Double,
    gender: Gender,
    tempUnit: TempUnit,
    weightUnit: WeightUnit,
    activityLevel: ActivityLevel,
): Double {
    return calculateWeight(weight, weightUnit) * activityLevel.value * (1 + calculateClimate(
        temp,
        tempUnit
    )) * (1 + calculateGender(gender))
}

private fun calculateWeight(weight: Double, unit: WeightUnit): Double {
    return when (unit) {
        WeightUnit.Pound -> weight / 2.205
        else -> weight
    }
}

private fun calculateClimate(temp: Double, unit: TempUnit): Double {
    val celsiusTemp = when (unit) {
        TempUnit.Fahrenheit -> (temp - 32) / 1.8
        TempUnit.Kelvin -> temp - 273.15
        else -> temp
    }
    return when {
        celsiusTemp < 15.0 -> -(5 / 100.0)
        celsiusTemp in 15.0..30.0 -> 0.0
        else -> 10 / 100.0
    }
}

private fun calculateGender(gender: Gender): Double {
    return when (gender) {
        Gender.Male -> 10 / 100.0
        Gender.Female -> 0.0
    }
}

fun convertToML(amount: Double, unit: WaterUnit): Double {
    return when (unit) {
        WaterUnit.oz -> amount * 29.5735
        WaterUnit.glasses -> amount * 250
        WaterUnit.ml -> amount
    }
}