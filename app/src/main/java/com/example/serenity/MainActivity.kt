package com.example.serenity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.serenity.navigation.AppNavHost
import com.example.serenity.ui.theme.SerenityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SerenityTheme {
                val navController = rememberNavController()
                var isDarkTheme by remember { mutableStateOf(false) }
                var isGuestMode by remember { mutableStateOf(true) }

                AppNavHost(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    isGuestMode = isGuestMode,
                    onLoginNavigate = {
                        navController.navigate("login") // Navigate to LoginScreen
                    }
                )
            }
        }
    }
}
