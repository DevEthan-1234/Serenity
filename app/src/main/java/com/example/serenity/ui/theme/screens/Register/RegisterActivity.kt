package com.example.serenity.ui.theme.screens.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.serenity.R
import com.example.serenity.data.AuthViewModel
import com.example.serenity.navigation.ROUTE_LOGIN
import com.example.serenity.ui.theme.SerenityTheme
import com.airbnb.lottie.compose.LottieAnimation


@Composable
fun RegisterScreen(navController: NavController) {
    val authViewModel: AuthViewModel= viewModel ()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("en") }
    var expanded by remember { mutableStateOf(false) } // Controls dropdown menu
    val context = LocalContext.current
    val passwordVisible by remember { mutableStateOf(false) }


    // Language translations
    val translations = mapOf(
        "en" to mapOf(
            "language" to "🌐 Language",
            "create_account" to "Create an Account",
            "first_name" to "First Name",
            "last_name" to "Last Name",
            "email" to "Email",
            "password" to "Password",
            "register" to "Register",
            "login_prompt" to "Already have an account? Log in"
        ),
        "sw" to mapOf(
            "language" to "🌐 Lugha",
            "create_account" to "Fungua Akaunti",
            "first_name" to "Jina la Kwanza",
            "last_name" to "Jina la Mwisho",
            "email" to "Barua Pepe",
            "password" to "Nenosiri",
            "register" to "Jisajili",
            "login_prompt" to "Tayari na akaunti? Ingia"
        ),
        "fr" to mapOf(
            "language" to "🌐 Langue",
            "create_account" to "Créer un compte",
            "first_name" to "Prénom",
            "last_name" to "Nom de famille",
            "email" to "E-mail",
            "password" to "Mot de passe",
            "register" to "S'inscrire",
            "login_prompt" to "Vous avez déjà un compte ? Connectez-vous"
        ),
        "es" to mapOf(
            "language" to "🌐 Idioma",
            "create_account" to "Crear una cuenta",
            "first_name" to "Nombre",
            "last_name" to "Apellido",
            "email" to "Correo electrónico",
            "password" to "Contraseña",
            "register" to "Registrarse",
            "login_prompt" to "¿Ya tienes una cuenta? Inicia sesión"
        ),
        "zh" to mapOf(
            "language" to "🌐 语言",
            "create_account" to "创建账户",
            "first_name" to "名字",
            "last_name" to "姓氏",
            "email" to "电子邮件",
            "password" to "密码",
            "register" to "注册",
            "login_prompt" to "已有账户？登录"
        ),
        "pt" to mapOf(
            "language" to "🌐 Língua",
            "create_account" to "Criar uma conta",
            "first_name" to "Nome",
            "last_name" to "Sobrenome",
            "email" to "E-mail",
            "password" to "Senha",
            "register" to "Registrar",
            "login_prompt" to "Já tem uma conta? Entrar"
        ),
        "ru" to mapOf(
            "language" to "🌐 Язык",
            "create_account" to "Создать аккаунт",
            "first_name" to "Имя",
            "last_name" to "Фамилия",
            "email" to "Электронная почта",
            "password" to "Пароль",
            "register" to "Зарегистрироваться",
            "login_prompt" to "Уже есть аккаунт? Войти"
        ),
        "ar" to mapOf(
            "language" to "🌐 لغة",
            "create_account" to "إنشاء حساب",
            "first_name" to "الاسم الأول",
            "last_name" to "اسم العائلة",
            "email" to "البريد الإلكتروني",
            "password" to "كلمة المرور",
            "register" to "تسجيل",
            "login_prompt" to "هل لديك حساب؟ تسجيل الدخول"
        )
    )

    val backgroundBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFF89CFF0), Color(0xFF00A4CC)))
    }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_animation))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

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
                // Language Selection Dropdown
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(translations[selectedLanguage]?.get("language") ?: "🌐 Language", fontSize = 14.sp, color = Color.White)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("🇬🇧 English") }, onClick = {
                            selectedLanguage = "en"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇰🇪 Swahili") }, onClick = {
                            selectedLanguage = "sw"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇫🇷 French") }, onClick = {
                            selectedLanguage = "fr"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇪🇸 Spanish") }, onClick = {
                            selectedLanguage = "es"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇨🇳 Chinese") }, onClick = {
                            selectedLanguage = "zh"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇵🇹 Portuguese") }, onClick = {
                            selectedLanguage = "pt"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇷🇺 Russian") }, onClick = {
                            selectedLanguage = "ru"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("🇸🇦 Arabic") }, onClick = {
                            selectedLanguage = "ar"
                            expanded = false
                        })
                    }

                }

                // Dark Mode Toggle
                TextButton(onClick = { isDarkMode = !isDarkMode }) {
                    Text(if (isDarkMode) "☀ Light Mode" else "🌙 Dark Mode", fontSize = 14.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 16.dp)
            )

//            Image(
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = "Serenity Logo",
//                modifier = Modifier.size(120.dp)
//            )

            Text(
                text = translations[selectedLanguage]?.get("create_account") ?: "Create an Account",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
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
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomTextField(firstName, { firstName = it }, translations[selectedLanguage]?.get("first_name") ?: "First Name", isDarkMode)
                    CustomTextField(lastName, { lastName = it }, translations[selectedLanguage]?.get("last_name") ?: "Last Name", isDarkMode)
                    CustomTextField(email, { email = it }, translations[selectedLanguage]?.get("email") ?: "Email", isDarkMode, KeyboardType.Email)
                    CustomTextField(password, { password = it }, translations[selectedLanguage]?.get("password") ?: "Password", isDarkMode, KeyboardType.Password, true)

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            authViewModel.signup(firstName,lastName,email,password,navController,context)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008080)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text(text = translations[selectedLanguage]?.get("register") ?: "Register", fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = {
                        navController.navigate(ROUTE_LOGIN)

                    }) {
                        Text(translations[selectedLanguage]?.get("login_prompt") ?: "Already have an account? Log in", color = Color(0xFF008080))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Serenity",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "A Product of Light Studios 2025",
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String, isDarkMode: Boolean, keyboardType: KeyboardType = KeyboardType.Text, isPassword: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = if (isDarkMode) Color.White else Color.Black) },
        textStyle = TextStyle(color = if (isDarkMode) Color.White else Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewScreen() {
    SerenityTheme {
        Column(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
            RegisterScreen(rememberNavController())
        }
    }
}
