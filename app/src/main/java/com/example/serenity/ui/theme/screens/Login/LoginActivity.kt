package com.example.serenity.ui.theme.screens.Login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.serenity.R
import com.example.serenity.data.AuthViewModel
import com.example.serenity.navigation.ROUTE_REGISTER

// Lottie imports
import com.airbnb.lottie.compose.*

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    val authViewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("en") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val translations = mapOf(
        "en" to mapOf(
            "language" to "🌐 Language",
            "login_prompt" to "Log in to your account",
            "email" to "Email",
            "password" to "Password",
            "login" to "Log In",
            "register_prompt" to "Don't have an account? Register here"
        ),
        "es" to mapOf(
            "language" to "🌐 Idioma",
            "login_prompt" to "Inicia sesión en tu cuenta",
            "email" to "Correo electrónico",
            "password" to "Contraseña",
            "login" to "Iniciar sesión",
            "register_prompt" to "¿No tienes cuenta? Regístrate aquí"
        ),
        "fr" to mapOf(
            "language" to "🌐 Langue",
            "login_prompt" to "Connectez-vous à votre compte",
            "email" to "Email",
            "password" to "Mot de passe",
            "login" to "Se connecter",
            "register_prompt" to "Pas de compte ? Inscrivez-vous ici"
        ),
        "de" to mapOf(
            "language" to "🌐 Sprache",
            "login_prompt" to "Melden Sie sich bei Ihrem Konto an",
            "email" to "E-Mail",
            "password" to "Passwort",
            "login" to "Anmelden",
            "register_prompt" to "Kein Konto? Hier registrieren"
        ),
        "it" to mapOf(
            "language" to "🌐 Lingua",
            "login_prompt" to "Accedi al tuo account",
            "email" to "Email",
            "password" to "Password",
            "login" to "Accedi",
            "register_prompt" to "Non hai un account? Registrati qui"
        ),
        "sw" to mapOf(
            "language" to "🌐 Lugha",
            "login_prompt" to "Ingia kwenye akaunti yako",
            "email" to "Barua pepe",
            "password" to "Nenosiri",
            "login" to "Ingia",
            "register_prompt" to "Huna akaunti? Jisajili hapa"
        ),
        "pt" to mapOf(
            "language" to "🌐 Idioma",
            "login_prompt" to "Entrar na sua conta",
            "email" to "Email",
            "password" to "Senha",
            "login" to "Entrar",
            "register_prompt" to "Não tem uma conta? Registre-se aqui"
        )
    )

    val backgroundBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFF89CFF0), Color(0xFF00A4CC)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(
                            translations[selectedLanguage]?.get("language") ?: "🌐 Language",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        translations.keys.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.uppercase(), fontSize = 16.sp) },
                                onClick = {
                                    selectedLanguage = lang
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                TextButton(onClick = { isDarkMode = !isDarkMode }) {
                    Text(
                        if (isDarkMode) "☀ Light Mode" else "🌙 Dark Mode",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Lottie Animation
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login_animation))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )

//            Image(
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = "Serenity Logo",
//                modifier = Modifier.size(120.dp)
//            )

            Text(
                text = translations[selectedLanguage]?.get("login_prompt") ?: "Log in to your account",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color.DarkGray else Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(translations[selectedLanguage]?.get("email") ?: "Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(translations[selectedLanguage]?.get("password") ?: "Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            authViewModel.login(email, password, context) { firstname ->
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(translations[selectedLanguage]?.get("login") ?: "Log In")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = translations[selectedLanguage]?.get("register_prompt")
                            ?: "Don't have an account? Register here",
                        color = Color(0xFF008080),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                navController.navigate(ROUTE_REGISTER)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Serenity",
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "A Product of Light Studios 2025",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}
