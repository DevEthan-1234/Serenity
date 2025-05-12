package com.example.serenity.ui.theme.screens.Start

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.serenity.navigation.ROUTE_WELCOME

@Composable
fun BreathingScreen(navController: NavController, modifier: Modifier = Modifier) {
    val breatheAnim = rememberInfiniteTransition(label = "BreathingAnimation")
    val scale by breatheAnim.animateFloat(
        initialValue = 0.8f,    // bigger starting scale
        targetValue = 1.5f,     // bigger expansion
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingScale"
    )

    val textAnim = rememberInfiniteTransition(label = "TextAnimation")
    val textAlpha by textAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TextAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB2DFDB),
                        Color(0xFF80CBC4),
                        Color(0xFF4DB6AC)
                    )
                )
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Breathing Circle
        Box(
            modifier = Modifier
                .size(250.dp)  // much bigger
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF004D40), Color(0xFF00796B)),
                        radius = 400f
                    )
                )
        )

        // Animated Welcome Text
        AnimatedVisibility(visible = true) {
            Text(
                text = "Welcome to Serenity –\nYour Space for Peace and Calm.",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(textAlpha)
            )
        }

        // Lower and Longer Start Button
        Button(
            onClick = { navController.navigate(ROUTE_WELCOME) },
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .height(60.dp), // taller button
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
        ) {
            Text("Start Here", fontSize = 20.sp, color = Color.White)
        }
    }
}

@Composable
@Preview
fun PreviewBreathingScreen() {
    BreathingScreen(rememberNavController())
}
