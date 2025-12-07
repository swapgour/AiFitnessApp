package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aifitnessapp.presentation.components.WeightBarChart
import com.example.aifitnessapp.presentation.viewmodels.WeightViewModel
import com.example.aifitnessapp.presentation.viewmodels.WeightViewModelFactory
import com.example.aifitnessapp.ui.theme.*
import kotlin.math.pow

// ⭐ Added imports
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.notification.WaterReminderScheduler
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreen(profileId: Int) {

    val context = LocalContext.current

    // 🔥 Load profile so we can schedule water reminders
    val db = DatabaseModule.getDatabase(context)
    val profileFlow = remember { db.profileDao().observeProfile(profileId) }
    val profile by profileFlow.collectAsState(initial = null)

    // 🔔 Schedule water reminders once profile is loaded
    LaunchedEffect(profile) {
        profile?.let {
            WaterReminderScheduler.scheduleWaterReminders(
                context = context,
                weightKg = it.weight
            )
        }
    }

    // --------------------- Load Weight History ---------------------
    val vm: WeightViewModel = viewModel(factory = WeightViewModelFactory(context))

    LaunchedEffect(profileId) {
        vm.loadHistory(profileId)
    }

    val history by vm.history.collectAsState()

    val currentWeight = history.lastOrNull()?.weight ?: 0f
    val previousWeight = history.getOrNull(history.size - 2)?.weight ?: currentWeight
    val weeklyDiff = currentWeight - previousWeight

    // This should ideally come from profile, but using fixed 175 cm because original code had it
    val heightCm = 175f
    val heightM = heightCm / 100f
    val bmi = if (heightM > 0) currentWeight / heightM.pow(2) else 0f

    var showAddWeight by remember { mutableStateOf(false) }

    // --------------------- UI Layout ---------------------
    PremiumLightBackground {
        ScreenContainer {

            PremiumTitle("Dashboard")
            Spacer(Modifier.height(20.dp))

            // ------------------ BMI CARD ------------------
            GlassCard {
                Text("BMI", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))

                Text(
                    text = String.format("%.1f", bmi),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(bmiCategory(bmi))
            }

            Spacer(Modifier.height(16.dp))

            // ------------------ WEEKLY CHANGE ------------------
            GlassCard {
                Text("Weekly Change", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))

                val diffTxt = String.format("%.1f", weeklyDiff)
                Text(
                    text = if (weeklyDiff >= 0) "+$diffTxt kg" else "$diffTxt kg",
                    color = if (weeklyDiff >= 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(Modifier.height(20.dp))

            // ------------------ WEIGHT GRAPH ------------------
            GlassCard {
                Text("Weight Progress", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(10.dp))

                WeightBarChart(history = history)
            }

            Spacer(Modifier.height(20.dp))

            // ------------------ ADD WEIGHT BUTTON ------------------
            Button(
                onClick = { showAddWeight = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Today's Weight")
            }
        }
    }

    if (showAddWeight) {
        AddWeightDialog(
            onDismiss = { showAddWeight = false },
            onSave = { weight ->
                vm.addWeight(profileId, weight)
                showAddWeight = false
            }
        )
    }
}

@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onSave: (Float) -> Unit
) {
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val w = weight.toFloatOrNull()
                if (w != null) onSave(w)
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Weight") },
        text = {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

fun bmiCategory(bmi: Float): String = when {
    bmi < 18.5f -> "Underweight"
    bmi < 24.9f -> "Normal"
    bmi < 29.9f -> "Overweight"
    else -> "Obese"
}
