package com.example.serenity.ui.theme.screens.Welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.elevatedButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.serenity.R
import com.example.serenity.navigation.ROUTE_LOGIN
import com.example.serenity.navigation.ROUTE_REGISTER
import com.example.serenity.ui.theme.SerenityTheme
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController) {
    val context = LocalContext.current
    var showTexts by remember { mutableStateOf(false) }

    // Animate text visibility
    LaunchedEffect(Unit) {
        delay(500)
        showTexts = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Lowers the entire content
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Serenity Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated welcome text
            AnimatedVisibility(
                visible = showTexts,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Text(
                    text = "Welcome to Serenity",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = showTexts,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
            ) {
                Text(
                    text = "Your Space for Peace and Calm",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(64.dp)) // Balanced spacing before buttons

            // Sign Up button
            Button(
                onClick = { navController.navigate(ROUTE_REGISTER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                elevation = ButtonDefaults.buttonElevation(8.dp),
                colors = elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Sign Up", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Log In button
            OutlinedButton(
                onClick = { navController.navigate(ROUTE_LOGIN) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("Log In", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SerenityTheme {
        WelcomeScreen(rememberNavController())
    }
}
