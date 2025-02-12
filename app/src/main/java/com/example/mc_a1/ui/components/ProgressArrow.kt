package com.example.mc_a1.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun ProgressArrow(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color
) {
    Canvas(
        modifier = modifier
    ) {
        // Draw vertical line
        drawLine(
            color = color,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height * progress),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )

        // Draw circle at the end
        drawCircle(
            color = color,
            radius = 15f,
            center = Offset(size.width / 2, size.height * progress)
        )
    }
}