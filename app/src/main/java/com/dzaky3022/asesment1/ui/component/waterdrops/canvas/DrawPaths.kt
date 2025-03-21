package com.dzaky3022.asesment1.ui.component.waterdrops.canvas

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import com.dzaky3022.asesment1.ui.theme.Blue
import com.dzaky3022.asesment1.ui.theme.Water
import com.dzaky3022.asesment1.ui.screen.Paths
import com.dzaky3022.asesment1.ui.component.waterdrops.text.TextParams
import com.dzaky3022.asesment1.ui.theme.BackgroundDark

fun DrawScope.drawWaves(
    paths: Paths,
) {
    drawIntoCanvas {
        it.drawPath(paths.pathList[1], paint.apply {
            color = Blue
        })
        it.drawPath(paths.pathList[0], paint.apply {
            color = Water
        })
    }
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawTextWithBlendMode(
    mask: Path,
    textParams: TextParams,
) {
    drawText(
        textMeasurer = textParams.textMeasurer,
        topLeft = textParams.textOffset,
        text = textParams.text,
        style = textParams.textStyle,
    )
    drawText(
        textMeasurer = textParams.textMeasurer,
        topLeft = textParams.unitTextOffset,
        text = "FT",
        style = textParams.unitTextStyle,
    )

    drawPath(
        path = mask,
        color = BackgroundDark,
        blendMode = BlendMode.SrcIn
    )
}

val paint = Paint().apply {
    this.color = Blue
    pathEffect = PathEffect.cornerPathEffect(100f)
}