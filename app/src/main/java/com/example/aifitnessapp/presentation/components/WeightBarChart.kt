package com.example.aifitnessapp.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aifitnessapp.domain.model.WeightHistory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeightBarChart(history: List<WeightHistory>) {

    if (history.isEmpty()) {
        Text("No weight history yet.", style = MaterialTheme.typography.bodyMedium)
        return
    }

    val maxWeight = remember(history) { history.maxOf { it.weight } }
    val minWeight = remember(history) { history.minOf { it.weight } }
    val range = (maxWeight - minWeight).takeIf { it > 0 } ?: 1f

    var startAnim by remember(history) { mutableStateOf(false) }
    LaunchedEffect(history) { startAnim = true }

    val animatedHeights = history.map { entry ->
        val target = if (startAnim) (entry.weight - minWeight) / range else 0f

        animateFloatAsState(
            targetValue = target,
            animationSpec = tween(900, easing = FastOutSlowInEasing),
            label = "barHeight"
        ).value
    }

    val barColor by animateColorAsState(
        targetValue = Color(0xFF4CAF50),
        animationSpec = tween(600),
        label = "barColor"
    )

    val dateFormat = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxWidth()) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            val barWidth = size.width / (history.size * 1.5f)
            val space = barWidth / 2f

            history.forEachIndexed { index, entry ->
                val progress = animatedHeights[index]
                val barHeight = progress * size.height

                val x = index * (barWidth + space)
                val y = size.height - barHeight

                drawRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            history.forEach { entry ->
                Text(
                    text = dateFormat.format(Date(entry.date)),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
