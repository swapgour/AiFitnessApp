package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aifitnessapp.data.local.ActiveProfileManager
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.data.local.UserPreferences
import com.example.aifitnessapp.domain.model.UserProfile
import com.example.aifitnessapp.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(onContinue: () -> Unit) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var heightUnit by remember { mutableStateOf("cm") }
    var heightCm by remember { mutableStateOf("") }
    var heightFt by remember { mutableStateOf("") }
    var heightIn by remember { mutableStateOf("") }

    var weight by remember { mutableStateOf("") }
    var targetWeight by remember { mutableStateOf("") }

    PremiumLightBackground {
        ScreenContainer {

            PremiumTitle("Your Details")
            Spacer(Modifier.height(20.dp))

            // NAME
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))

            // AGE
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))

            // HEIGHT SELECTION
            Text("Height", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = heightUnit == "cm",
                    onClick = { heightUnit = "cm" }
                )
                Text("Centimeters")

                Spacer(Modifier.width(24.dp))

                RadioButton(
                    selected = heightUnit == "ft",
                    onClick = { heightUnit = "ft" }
                )
                Text("Feet & Inches")
            }

            Spacer(Modifier.height(16.dp))

            // HEIGHT INPUT
            if (heightUnit == "cm") {
                OutlinedTextField(
                    value = heightCm,
                    onValueChange = { heightCm = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = heightFt,
                        onValueChange = { heightFt = it },
                        label = { Text("Feet") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(12.dp))
                    OutlinedTextField(
                        value = heightIn,
                        onValueChange = { heightIn = it },
                        label = { Text("Inches") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // WEIGHT
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Current Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // TARGET WEIGHT
            OutlinedTextField(
                value = targetWeight,
                onValueChange = { targetWeight = it },
                label = { Text("Target Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(30.dp))


            // 🔥 FINAL FIXED BUTTON — SAVES PROFILE WITH CORRECT GENERATED ID
            Button(
                onClick = {

                    val heightCmFinal = if (heightUnit == "cm") {
                        heightCm.toFloatOrNull() ?: 0f
                    } else {
                        val ft = heightFt.toIntOrNull() ?: 0
                        val inch = heightIn.toIntOrNull() ?: 0
                        (ft * 30.48 + inch * 2.54).toFloat()
                    }

                    val prefs = UserPreferences(context)
                    val dao = DatabaseModule.getDatabase(context).profileDao()
                    val activeProfileManager = ActiveProfileManager(context)

                    CoroutineScope(Dispatchers.IO).launch {

                        // 1️⃣ Save details locally (optional)
                        prefs.saveUserDetails(
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            heightCm = heightCmFinal,
                            weight = weight.toFloatOrNull() ?: 0f,
                            targetWeight = targetWeight.toFloatOrNull() ?: 0f
                        )

                        // 2️⃣ Insert into Room and GET THE GENERATED ID (CRITICAL FIX)
                        val generatedId = dao.insertProfile(
                            UserProfile(
                                name = name,
                                age = age.toIntOrNull() ?: 0,
                                heightCm = heightCmFinal,
                                weight = weight.toFloatOrNull() ?: 0f,
                                targetWeight = targetWeight.toFloatOrNull() ?: 0f
                            )
                        ).toInt()

                        // 3️⃣ Set this ID as the ACTIVE PROFILE
                        activeProfileManager.setActiveProfile(generatedId)

                        println("DEBUG: Active profile set to ID = $generatedId")
                    }

                    onContinue()
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text("Continue", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
