package com.dzaky3022.asesment1.ui.component.animating

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember

enum class WaterLevelState {
    StartReady,
    Animating,
    Done,
}

@Composable
fun waveProgressAsState(
    timerState: WaterLevelState,
    percentage: Float? = 0f,
    timerDurationInMillis: Long
): State<Float> {
    val animatable = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(timerState) {
        when (timerState) {
            WaterLevelState.StartReady -> {
                animatable.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(stiffness = 100f)
                )
            }

            WaterLevelState.Animating -> {
                animatable.animateTo(
                    targetValue = percentage!! / 100,
                    animationSpec = tween(
                        durationMillis = timerDurationInMillis.toInt(),
                        easing = LinearEasing
                    )
                )
            }

            else -> return@LaunchedEffect
        }
    }

    return produceState(initialValue = animatable.value, key1 = animatable.value) {
        this.value = animatable.value
    }
}