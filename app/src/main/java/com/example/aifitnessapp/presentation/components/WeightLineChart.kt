package com.example.aifitnessapp.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aifitnessapp.domain.model.WeightHistory
import kotlin.math.roundToInt

@Composable
fun WeightLineChart(history: List<WeightHistory>) {

    if (history.isEmpty()) {
        Text("No weight history yet.", style = MaterialTheme.typography.bodyMedium)
        return
    }

    val minWeight = history.minOf { it.weight }
    val maxWeight = history.maxOf { it.weight }
    val range = (maxWeight - minWeight).takeIf { it > 0f } ?: 1f

    val points = remember(history) {
        history.mapIndexed { index: Int, entry: WeightHistory ->
            index to ((entry.weight - minWeight) / range)
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (points.size < 2) return@Canvas

        val gap = size.width / (points.size - 1)

        points.zipWithNext().forEach { pair ->
            val (first, second) = pair

            val x1 = first.first * gap
            val y1 = size.height - (first.second * size.height)

            val x2 = second.first * gap
            val y2 = size.height - (second.second * size.height)

            drawLine(
                color = Color(0xFF4CAF50),
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = 6f
            )
        }
    }
}
