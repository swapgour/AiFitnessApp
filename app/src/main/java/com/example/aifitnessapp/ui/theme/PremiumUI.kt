package com.example.aifitnessapp.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------
// PREMIUM LIGHT GRADIENT BACKGROUND (SINGLE VERSION)
// ----------------------------------------------------
@Composable
fun PremiumLightBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF5F7FA),
                        Color(0xFFEFF3F8),
                        Color(0xFFE4ECF5)
                    )
                )
            )
    ) {
        content()
    }
}

// ----------------------------------------------------
// SCREEN CONTAINER (UNIFIED FOR ALL SCREENS)
// ----------------------------------------------------
@Composable
fun ScreenContainer(
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = content
    )
}

// ----------------------------------------------------
// PREMIUM TITLE (UNIQUE, CLEAN, NO DUPLICATES)
// ----------------------------------------------------
@Composable
fun PremiumTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            color = Color(0xFF3A86FF),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp,
            shadow = Shadow(
                color = Color(0xFF8338EC).copy(alpha = 0.5f),
                offset = Offset(2f, 3f),
                blurRadius = 10f
            )
        )
    )
}

// ----------------------------------------------------
// GLASS CARD (ONE VERSION ONLY)
// ----------------------------------------------------
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}
