package com.example.aifitnessapp.presentation.navigation

sealed class NavRoutes(val route: String) {
    object ProfileSelection : NavRoutes("profile_selection")
    object UserDetails : NavRoutes("user_details")
    object Home : NavRoutes("home")
    object Diet : NavRoutes("diet")
    object Workout : NavRoutes("workout")
    object AiCoach : NavRoutes("ai_coach")
    object Dashboard : NavRoutes("dashboard")

}
