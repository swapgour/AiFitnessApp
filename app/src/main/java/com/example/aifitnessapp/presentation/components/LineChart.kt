package com.example.aifitnessapp.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    weights: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    if (weights.isEmpty()) return

    Canvas(modifier = modifier) {
        val maxWeight = weights.maxOrNull() ?: 0f
        val minWeight = weights.minOrNull() ?: 0f
        val range = (maxWeight - minWeight).takeIf { it > 0 } ?: 1f

        val stepX = size.width / (weights.size - 1).coerceAtLeast(1)

        var prevPoint: Offset? = null

        weights.forEachIndexed { idx, w ->
            val x = idx * stepX
            val y = size.height - ((w - minWeight) / range) * size.height

            val point = Offset(x, y)

            prevPoint?.let { old ->
                drawLine(
                    start = old,
                    end = point,
                    color = lineColor,
                    strokeWidth = 5f
                )
            }

            prevPoint = point
        }
    }
}
