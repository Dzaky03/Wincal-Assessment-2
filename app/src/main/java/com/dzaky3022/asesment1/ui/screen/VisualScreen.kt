package com.dzaky3022.asesment1.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import com.dzaky3022.asesment1.ui.component.animating.WaterLevelState
import com.dzaky3022.asesment1.ui.component.animating.waveProgressAsState
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.ui.component.waterdrops.canvas.drawTextWithBlendMode
import com.dzaky3022.asesment1.ui.component.waterdrops.canvas.drawWaves
import com.dzaky3022.asesment1.ui.component.waterdrops.createLevelAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.createPathsAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.rememberDropWaterDuration
import com.dzaky3022.asesment1.ui.component.waterdrops.text.createTextParamsAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.createAnimationsAsState
import com.dzaky3022.asesment1.ui.theme.BackgroundDark

@Composable
fun VisualScreen(
    modifier: Modifier = Modifier,
    waveDurationInMills: Long = 6000L,
    waterLevelState: WaterLevelState,
    onWavesClick: () -> Unit,
    onBackPressed: () -> Unit,
    content: () -> com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText,
) {
    val waveParams = remember { content().waveParams }
    val animations = createAnimationsAsState(pointsQuantity = waveParams.pointsQuantity)
    WaterLevelDrawing(
        modifier = modifier,
        waveDurationInMills = waveDurationInMills,
        waveParams = waveParams,
        animations = animations,
        waterLevelState = waterLevelState,
        onWavesClick = onWavesClick,
        onBackPressed = onBackPressed,
        content = content,
    )
}

@Composable
fun WaterLevelDrawing(
    modifier: Modifier = Modifier,
    waveDurationInMills: Long,
    waveParams: com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaveParams,
    animations: MutableList<State<Float>>,
    waterLevelState: WaterLevelState,
    onWavesClick: () -> Unit,
    onBackPressed: () -> Unit,
    content: () -> com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText,
) {
    val waveDuration by rememberSaveable { mutableLongStateOf(waveDurationInMills) }
    val waveProgress by waveProgressAsState(
        timerState = waterLevelState,
        timerDurationInMillis = waveDuration
    )
    WavesDrawing(
        modifier = modifier,
        waveDuration = waveDuration,
        animations = animations,
        waveProgress = waveProgress,
        waveParams = waveParams,
        onWavesClick = onWavesClick,
        onBackPressed = onBackPressed,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WavesDrawing(
    modifier: Modifier = Modifier,
    waveDuration: Long,
    waveParams: com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaveParams,
    animations: MutableList<State<Float>>,
    waveProgress: Float,
    onWavesClick: () -> Unit,
    onBackPressed: () -> Unit,
    content: () -> com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText,
) {
    val elementParams by remember { mutableStateOf(ElementParams()) }
    var containerSize by remember { mutableStateOf(IntSize(0, 0)) }

    val dropWaterDuration = rememberDropWaterDuration(
        elementSize = elementParams.size,
        containerSize = containerSize,
        duration = waveDuration
    )

    val waterLevel by remember(waveProgress, containerSize.height) {
        derivedStateOf {
            (waveProgress * containerSize.height).toInt()
        }
    }

    val levelState = createLevelAsState(
        waterLevelProvider = { waterLevel },
        bufferY = waveParams.bufferY,
        elementParams = elementParams
    )

    val paths = createPathsAsState(
        containerSize = containerSize,
        elementParams = elementParams,
        levelState = levelState.value,
        waterLevelProvider = { waterLevel.toFloat() },
        dropWaterDuration = dropWaterDuration,
        animations = animations,
        waveParams = waveParams
    )

    val textParams = createTextParamsAsState(
        textStyle = content().textStyle,
        waveProgress = waveProgress,
        elementParams = elementParams
    )

    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
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
                            "Water Intake Visualizer",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Water),
                actions = {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = Color.Unspecified
                        )
                    }
                    MoreMenu(
                        expanded = expanded,
                        onDismiss = {
                            expanded = false
                        },
                    );
                }
            )
        },
        content = { paddingValues ->
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(BackgroundDark)
            ) {
                drawWaves(paths)
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .clickable(onClick = onWavesClick)
                    .onGloballyPositioned { containerSize = IntSize(it.size.width, it.size.height) }
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawTextWithBlendMode(
                            mask = paths.pathList[0],
                            textParams = textParams.value
                        )
                    }
            ) {
                Text(
                    modifier = content().modifier
                        .align(content().align)
                        .onGloballyPositioned {
                            elementParams.position = it.positionInParent()
                            elementParams.size = it.size
                        },
                    text = "46FT",
                    style = content().textStyle
                )
            }
        }
    )
}

@Stable
data class ElementParams(
    var size: IntSize = IntSize.Zero,
    var position: Offset = Offset(0f, 0f),
)

data class Paths(
    val pathList: MutableList<Path> = mutableListOf(Path(), Path())
)

@Composable
fun MoreMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onAdd: () -> Unit = {},
    onShare: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        DropdownMenuItem(
            text = { Text("Add") },
            onClick = {
                onAdd()
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = { Text("Share") },
            onClick = {
                onShare()
                onDismiss()
            }
        )
    }
}