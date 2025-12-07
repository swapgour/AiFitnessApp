package com.example.aifitnessapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aifitnessapp.presentation.screens.*
import com.example.aifitnessapp.data.local.ActiveProfileManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues = PaddingValues()) {

    val activeProfileManager = remember { ActiveProfileManager(navController.context) }
    var activeProfileId by remember { mutableStateOf<Int?>(null) }

    // Load active profile ONCE
    LaunchedEffect(Unit) {
        activeProfileManager.activeProfileId.collectLatest { id ->
            activeProfileId = id
        }
    }

    // ⭐ Always show Splash first
    val startDestination = "splash"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues)
    ) {

        // ------------------------------------------------------
        // ⭐ SPLASH SCREEN
        // ------------------------------------------------------
        composable("splash") {

            SplashScreen(
                navController = navController,
                onFinish = {

                    // If activeProfileId still null → profile not selected
                    if (activeProfileId == null) {
                        navController.navigate(NavRoutes.ProfileSelection.route) {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate(NavRoutes.Home.route) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }

        // ------------------------------------------------------
        // PROFILE SELECTION SCREEN
        // ------------------------------------------------------
        composable(NavRoutes.ProfileSelection.route) {
            val scope = rememberCoroutineScope()

            ProfileSelectionScreen(
                onAddNewUser = { navController.navigate(NavRoutes.UserDetails.route) },
                onSelectProfile = { profile ->

                    // Save selected profile
                    scope.launch {
                        activeProfileManager.setActiveProfile(profile.id)
                    }

                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ------------------------------------------------------
        // USER DETAILS SCREEN
        // ------------------------------------------------------
        composable(NavRoutes.UserDetails.route) {
            UserDetailsScreen(
                onContinue = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ------------------------------------------------------
        // HOME
        // ------------------------------------------------------
        composable(NavRoutes.Home.route) {
            HomeScreen(navController)
        }

        // ------------------------------------------------------
        // DIET SCREEN
        // ------------------------------------------------------
        composable(NavRoutes.Diet.route) {
            DietScreen()
        }

        // ------------------------------------------------------
        // WORKOUT SCREEN
        // ------------------------------------------------------
        composable(NavRoutes.Workout.route) {
            WorkoutScreen()
        }

        // ------------------------------------------------------
        // AI COACH
        // ------------------------------------------------------
        composable(NavRoutes.AiCoach.route) {
            AiCoachScreen()
        }

        // ------------------------------------------------------
        // DASHBOARD
        // ------------------------------------------------------
        composable(NavRoutes.Dashboard.route) {
            val pid = activeProfileId ?: 1
            DashboardScreen(profileId = pid)
        }
    }
}
