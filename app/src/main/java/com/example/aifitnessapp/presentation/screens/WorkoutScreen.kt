package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aifitnessapp.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check


@Composable
fun WorkoutScreen() {

    var level by remember { mutableStateOf("Beginner") }
    var daysPerWeek by remember { mutableStateOf(4) }
    var goal by remember { mutableStateOf("Fat Loss") }

    val scroll = rememberScrollState()

    val plan = remember(level, daysPerWeek, goal) {
        generateWorkoutPlan(level, daysPerWeek, goal)
    }

    PremiumLightBackground {
        ScreenContainer {

            PremiumTitle("AI Workout Plan")
            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
            ) {

                // ----------------- LEVEL -----------------
                Text("Experience Level", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))

                Row {
                    PremiumChip("Beginner", level == "Beginner") { level = "Beginner" }
                    Spacer(Modifier.width(8.dp))
                    PremiumChip("Intermediate", level == "Intermediate") { level = "Intermediate" }
                    Spacer(Modifier.width(8.dp))
                    PremiumChip("Advanced", level == "Advanced") { level = "Advanced" }
                }

                Spacer(Modifier.height(20.dp))

                // ----------------- GOAL -----------------
                Text("Goal", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))

                Row {
                    PremiumChip("Fat Loss", goal == "Fat Loss") { goal = "Fat Loss" }
                    Spacer(Modifier.width(8.dp))
                    PremiumChip("Muscle Gain", goal == "Muscle Gain") { goal = "Muscle Gain" }
                    Spacer(Modifier.width(8.dp))
                    PremiumChip("General Fitness", goal == "General Fitness") { goal = "General Fitness" }
                }

                Spacer(Modifier.height(20.dp))

                // ----------------- DAYS / WEEK -----------------
                Text("Days per Week: $daysPerWeek", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))

                Row {
                    (3..6).forEach { d ->
                        PremiumChip("$d Days", daysPerWeek == d) { daysPerWeek = d }
                        Spacer(Modifier.width(8.dp))
                    }
                }

                Spacer(Modifier.height(30.dp))

                // ----------------- WEEKLY PLAN -----------------
                Text("Suggested Weekly Plan", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))

                plan.forEach { day ->
                    GlassCard {
                        Text(day.day, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        day.exercises.forEach {
                            Text("• $it", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

// ------------------ WORKOUT DATA CLASS ------------------
data class DayWorkout(
    val day: String,
    val exercises: List<String>
)

// ------------------ PLAN GENERATOR ------------------
private fun generateWorkoutPlan(
    level: String,
    days: Int,
    goal: String
): List<DayWorkout> {

    val beginnerStrength = listOf(
        "Bodyweight Squats – 3x12",
        "Push-Ups – 3x10",
        "Dumbbell Rows – 3x12",
        "Plank – 3x20 sec"
    )
    val cardio = listOf(
        "Brisk Walk – 20 min",
        "Cycling – 15 min",
        "Skipping – 3 rounds"
    )
    val advancedStrength = listOf(
        "Barbell Squats – 4x8",
        "Deadlift – 4x6",
        "Bench Press – 4x8",
        "Pull-Ups – 3x8",
        "Plank – 3x40 sec"
    )

    val focusStrength = goal == "Muscle Gain"
    val focusCardio = goal == "Fat Loss"

    val list = mutableListOf<DayWorkout>()

    for (i in 1..days) {
        val isEven = i % 2 == 0

        val exercises = when {
            focusStrength && !isEven -> if (level == "Advanced") advancedStrength else beginnerStrength
            focusCardio && isEven -> cardio
            else -> beginnerStrength + listOf("Light Cardio – 10 min")
        }

        list.add(DayWorkout("Day $i", exercises))
    }

    return list
}

// ------------------ PREMIUM CHIP ------------------
@Composable
fun PremiumChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (selected) {
            { Icon(Icons.Default.Check, contentDescription = null) }
        } else null
    )
}
