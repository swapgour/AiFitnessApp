package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aifitnessapp.data.local.UserPreferences
import com.example.aifitnessapp.presentation.navigation.NavRoutes
import com.example.aifitnessapp.ui.theme.*
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    var name by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf(0f) }
    var weight by remember { mutableStateOf(0f) }
    var targetWeight by remember { mutableStateOf(0f) }

    // Load user details
    LaunchedEffect(Unit) {
        name = prefs.userNameFlow.first() ?: ""
        heightCm = prefs.userHeightFlow.first() ?: 0f
        weight = prefs.userWeightFlow.first() ?: 0f
        targetWeight = prefs.userTargetWeightFlow.first() ?: 0f
    }

    var menuExpanded by remember { mutableStateOf(false) }

    PremiumLightBackground {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Welcome, $name",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Switch Profile") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(NavRoutes.ProfileSelection.route) {
                                        popUpTo(0)
                                    }
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Edit User Details") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(NavRoutes.UserDetails.route)
                                }
                            )
                        }
                    }
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { pad ->

            // FIXED: ScreenContainer can only accept `padding`
            ScreenContainer(padding = pad) {

                Spacer(Modifier.height(20.dp))

                GlassCard {
                    Text("Your Stats", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))

                    Text("Current Weight: $weight kg", style = MaterialTheme.typography.bodyLarge)
                    Text("Target Weight: $targetWeight kg", style = MaterialTheme.typography.bodyLarge)
                    Text("Height: ${heightCm.toInt()} cm", style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = { navController.navigate(NavRoutes.Dashboard.route) },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Text("Start Your Fitness Journey", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
