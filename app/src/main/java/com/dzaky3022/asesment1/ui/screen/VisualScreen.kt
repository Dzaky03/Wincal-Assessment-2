package com.dzaky3022.asesment1.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.drawToBitmap
import androidx.navigation.NavHostController
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.utils.navigate
import com.dzaky3022.asesment1.navigation.Screen
import com.dzaky3022.asesment1.utils.shareData
import com.dzaky3022.asesment1.ui.component.animating.WaterLevelState
import com.dzaky3022.asesment1.ui.component.animating.waveProgressAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.canvas.drawTextWithBlendMode
import com.dzaky3022.asesment1.ui.component.waterdrops.canvas.drawWaves
import com.dzaky3022.asesment1.ui.component.waterdrops.createLevelAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.createPathsAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.rememberDropWaterDuration
import com.dzaky3022.asesment1.ui.component.waterdrops.text.createTextParamsAsState
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaterDropText
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.WaveParams
import com.dzaky3022.asesment1.ui.component.waterdrops.wave.createAnimationsAsState
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.ui.theme.BackgroundLight
import com.dzaky3022.asesment1.ui.theme.BackgroundDark
import com.dzaky3022.asesment1.ui.theme.Danger
import com.dzaky3022.asesment1.ui.theme.Success
import com.dzaky3022.asesment1.ui.theme.Warning
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.ui.theme.WhiteTitle
import com.dzaky3022.asesment1.utils.Enums.*
import kotlinx.coroutines.launch

@Composable
fun VisualScreen(
    modifier: Modifier = Modifier,
    waveDurationInMills: Long = 6000L,
    waterResult: WaterResult? = null,
    navController: NavHostController,
    content: () -> WaterDropText,
) {
    val waveParams = remember { content().waveParams }
    val animations = createAnimationsAsState(pointsQuantity = waveParams.pointsQuantity)
    WaterLevelDrawing(
        modifier = modifier,
        waveDurationInMills = waveDurationInMills,
        waveParams = waveParams,
        animations = animations,
        waterResult = waterResult,
        navController = navController,
        content = content,
    )
}

@Composable
fun WaterLevelDrawing(
    modifier: Modifier = Modifier,
    waveDurationInMills: Long,
    waveParams: WaveParams,
    animations: MutableList<State<Float>>,
    waterResult: WaterResult? = null,
    navController: NavHostController,
    content: () -> WaterDropText,
) {
    val waveDuration by rememberSaveable { mutableLongStateOf(waveDurationInMills) }
    var waterLevelState by remember { mutableStateOf(WaterLevelState.StartReady) }
    val waveProgress by waveProgressAsState(
        timerState = waterLevelState,
        percentage = waterResult?.percentage,
        timerDurationInMillis = waveDuration
    )
    WavesDrawing(
        modifier = modifier,
        waveDuration = waveDuration,
        animations = animations,
        waveProgress = waveProgress,
        waveParams = waveParams,
        waterResult = waterResult,
        navController = navController,
        onWaterAnimated = { waterLevelState = it },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WavesDrawing(
    modifier: Modifier = Modifier,
    waveDuration: Long,
    waveParams: WaveParams,
    animations: MutableList<State<Float>>,
    waveProgress: Float,
    waterResult: WaterResult? = null,
    navController: NavHostController,
    onWaterAnimated: (WaterLevelState) -> Unit,
    content: () -> WaterDropText,
) {
    val elementParams by remember { mutableStateOf(ElementParams()) }
    var containerSize by remember { mutableStateOf(IntSize(0, 0)) }
    var expanded by remember { mutableStateOf(false) }
    var isSimpleView by remember { mutableStateOf(false) }
    var currentScreenState by remember { mutableStateOf(ScreenState.FirstScreen) }
    val context = LocalContext.current
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val dropWaterDuration = rememberDropWaterDuration(
        elementSize = elementParams.size,
        containerSize = containerSize,
        duration = waveDuration
    )
    val waterLevel by remember(waveProgress, containerSize.height) {
        derivedStateOf {
            ((1 - waveProgress) * containerSize.height).toInt()
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
    LaunchedEffect(waterResult, currentScreenState) {
        if (waterResult != null && currentScreenState == ScreenState.SecondScreen)
            onWaterAnimated(WaterLevelState.Animating)
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                            "Water Intake Visualizer",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        )
                    }
                },
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
                        onChangeView = {
                            navigate(navController, Screen.Form.route)
                        },
                        onShare = {
                            coroutineScope.launch {
                                val bitmap = view.drawToBitmap()
                                imageBitmap.value = bitmap
                                shareData(context, bitmap, "this is a caption")
                            }
                        },
                    );
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Water),
            )
        },
        content = { paddingValues ->
            if (!isSimpleView)
            when (currentScreenState) {
                ScreenState.FirstScreen -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(
                                BackgroundDark
                            )
                            .clickable {
                                currentScreenState = ScreenState.SecondScreen
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Column {
                                Spacer(
                                    Modifier.height(50.dp)
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = buildAnnotatedString {
                                        append("You've consumed ")
                                        withStyle(style = SpanStyle(color = Water)) {
                                            append("${waterResult?.amount} ml")
                                        }
                                        append(" of water")
                                    },
                                    color = WhiteTitle,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Start,
                                    fontSize = 45.sp,
                                )
                                Spacer(Modifier.height(32.dp))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = buildAnnotatedString {
                                        append("The amount you need today is\n")
                                        withStyle(style = SpanStyle(color = Water)) {
                                            append("${waterResult?.resultValue} ml")
                                        }
                                    },
                                    color = WhiteTitle,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.End,
                                    fontSize = 45.sp,
                                )
                            }
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Tap to continue...",
                                color = WhiteTitle,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
                ScreenState.SecondScreen -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                currentScreenState = ScreenState.ThirdScreen
                            }
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .background(
                                    BackgroundDark
                                )
                        ) {
                            drawWaves(paths, waveProgress)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .onGloballyPositioned {
                                    containerSize = IntSize(it.size.width, it.size.height)
                                }
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
                                    .offset(x = 50.dp)
                                    .onGloballyPositioned {
                                        elementParams.position = it.positionInParent()
                                        elementParams.size = it.size
                                    },
                                text = "77.00%",
                                style = content().textStyle
                            )
                        }
                    }
                }
                ScreenState.ThirdScreen -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(
                                BackgroundDark
                            )
                            .clickable {
                                currentScreenState = ScreenState.SecondScreen
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            val image = when {
                                waveProgress >= 1f -> R.drawable.very_high
                                waveProgress > 0.75f -> R.drawable.high
                                waveProgress > 0.5f -> R.drawable.medium
                                else -> R.drawable.low
                            }
                            val text = when {
                                waveProgress >= 1f -> "Goal Achieved! 🥳🎉"
                                waveProgress > 0.75f -> "Almost there! Just a bit more 💧"
                                waveProgress > 0.5f -> "You're halfway there! Keep it up 💪"
                                else -> "You're falling behind! Drink some water ⚠️"
                            }
                            Image(
                                modifier = Modifier.size(146.dp, 242.dp),
                                painter = painterResource(image),
                                contentDescription = null,
                            )
                            Spacer(Modifier.height(24.dp))
                            Text(
                                text = text,
                                color = WhiteTitle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(Modifier.height(24.dp))
                            Text(
                                text = "Share your result to your friend by pressing share button on top right corner menu!",
                                color = WhiteTitle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            else
                Column {
                    Text(
                        text = "Water Consumed"
                    )
                    Text(
                        text = "${waterResult?.amount}"
                    )
                    Text(
                        text = "Water Needed"
                    )
                    Text(
                        text = "${waterResult?.resultValue}"
                    )
                    Text(
                        text = "Percentage"
                    )
                    Text(
                        text = "${waterResult?.percentage}"
                    )
                }
        }
    )
}

@Composable
fun MoreMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onChangeView: () -> Unit = {},
    onShare: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    DropdownMenu(
        modifier = modifier.background(BackgroundLight),
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        DropdownMenuItem(
            text = { Text("Simple View", fontSize = 15.sp) },
            onClick = {
                onChangeView()
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = { Text("Share", fontSize = 15.sp) },
            onClick = {
                onShare()
                onDismiss()
            }
        )
    }
}

@Stable
data class ElementParams(
    var size: IntSize = IntSize.Zero,
    var position: Offset = Offset(0f, 0f),
)

data class Paths(
    val pathList: MutableList<Path> = mutableListOf(Path(), Path())
)