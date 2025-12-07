package com.example.aifitnessapp

import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aifitnessapp.presentation.components.BottomNavBar
import com.example.aifitnessapp.presentation.navigation.AppNavigation
import com.example.aifitnessapp.presentation.navigation.NavRoutes

class MainActivity : ComponentActivity() {

    override fun onUserLeaveHint() {
        try {
            moveTaskToFront()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun moveTaskToFront() {
        try {
            val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            activityManager.moveTaskToFront(taskId, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ Request Notification Permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute !in listOf(
        NavRoutes.ProfileSelection.route,
        NavRoutes.UserDetails.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { paddingValues ->

        AppNavigation(
            navController = navController,
            paddingValues = paddingValues
        )
    }
}
