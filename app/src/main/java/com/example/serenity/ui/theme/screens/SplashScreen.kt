package com.example.serenity.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serenity.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToNext: () -> Unit) {
    // Delay for 2.0 seconds before navigating
    LaunchedEffect(Unit) {
        delay(2000L)
        onNavigateToNext()
    }

    // Splash layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0F0C0)), // Light green color
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // App Logo (circular clipped)
            Image(
                painter = painterResource(id = R.drawable.logo), // Ensure logo exists in drawable
                contentDescription = "Serenity Logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // App Tagline
            Text(
                text = "Serenity. A Mental Health App",
                color = Color(0xFF004D40),
                fontSize = 20.sp
            )
        }
    }
}
