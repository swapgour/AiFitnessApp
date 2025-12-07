package com.example.aifitnessapp.domain.model

data class DietPlanJson(
    val breakfast: Meal,
    val mid_morning_snack: Meal,
    val lunch: Meal,
    val evening_snack: Meal,
    val dinner: Meal,
    val summary: Summary
)

data class Meal(
    val time: String,
    val items: List<String>,
    val calories: Int,
    val details: List<String>
)

data class Summary(
    val total_calories: Int,
    val nutrients: List<String>,
    val tips: List<String>
)
