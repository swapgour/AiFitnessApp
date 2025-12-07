package com.example.aifitnessapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    // Stylish Large Title
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    ),

    // Section Titles
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    ),

    // Body Text for Cards
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    ),

    // Smaller body text
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
)
