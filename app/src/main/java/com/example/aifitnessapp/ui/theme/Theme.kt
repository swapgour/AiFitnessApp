package com.example.aifitnessapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = DeepMidnight,
    surface = DarkBlueGray,
    onPrimary = Color.White,
    onSurface = Color.White,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = NeonBlue,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = Color(0xFFF8F8FF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSurface = Color(0xFF1C1C1C),
    onBackground = Color(0xFF1C1C1C)
)

@Composable
fun AiFitnessAppTheme(
    darkTheme: Boolean = true, // FORCE PREMIUM DARK MODE
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (LocalContext.current as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        window.statusBarColor = DarkBlack.value.toInt()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
