package com.example.aifitnessapp.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onSave: (Float) -> Unit
) {
    var weightInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Today's Weight") },
        text = {
            Column {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg)") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val weight = weightInput.toFloatOrNull()
                if (weight != null) {
                    onSave(weight)
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
