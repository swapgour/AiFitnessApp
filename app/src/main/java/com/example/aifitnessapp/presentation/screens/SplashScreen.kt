package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aifitnessapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    onFinish: () -> Unit
) {

    // Auto navigate after delay
    LaunchedEffect(Unit) {
        delay(1500)   // 1.5 seconds splash
        onFinish()
    }

    // UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // App logo (change to your own resource)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(160.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "AI Fitness Pro",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Transforming Your Fitness Journey",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
