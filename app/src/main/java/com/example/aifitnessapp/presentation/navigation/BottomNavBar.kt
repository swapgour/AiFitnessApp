package com.example.aifitnessapp.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController
import com.example.aifitnessapp.presentation.navigation.NavRoutes

@Composable
fun BottomNavBar(navController: NavHostController) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(NavRoutes.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(NavRoutes.Diet.route) },
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Diet") },
            label = { Text("Diet") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(NavRoutes.Workout.route) },
            icon = { Icon(Icons.Default.FitnessCenter, "Workout") },
            label = { Text("Workout") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(NavRoutes.AiCoach.route) },
            icon = { Icon(Icons.Default.SmartToy, "AI Coach") },
            label = { Text("AI Coach") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(NavRoutes.Dashboard.route) },
            icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
            label = { Text("Dashboard") }
        )
    }
}
