package com.dzaky3022.asesment1.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dzaky3022.asesment1.navigation.Screen
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.ui.theme.WhiteCaption
import com.dzaky3022.asesment1.ui.theme.WhiteTitle

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    var isPressed2 by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundDark,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.app_name_complete),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhiteTitle
                )
                Text(
                    text = stringResource(R.string.app_desc),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WhiteCaption
                )
            }
            Image(
                modifier = Modifier
                    .size(250.dp)
                    .offset(y = (-30).dp)
                    .align(Alignment.CenterHorizontally)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed2 = true
                                awaitRelease()
                                isPressed2 = false
                            }
                        )
                    },
                painter = painterResource(id = R.drawable.app_logo_2),
                contentDescription = "App Logo",
                colorFilter = ColorFilter.tint(
                    color = if (isPressed2) Color.White else Water,
                    blendMode = BlendMode.SrcIn
                )
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    navController.navigate(Screen.Form.route)
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isPressed) Color.White else Water,
                ),
                interactionSource = interactionSource,
            ) {
                Text(
                    text = stringResource(R.string.button_start),
                    fontSize = 16.sp,
                    color = BackgroundDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}