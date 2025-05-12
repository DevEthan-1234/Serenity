package com.example.serenity.ui.theme.screens.Dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.serenity.R
import com.example.serenity.navigation.ROUTE_CARE
import com.example.serenity.navigation.ROUTE_DASHBOARD
import com.example.serenity.navigation.ROUTE_EXERCISE
import com.example.serenity.navigation.ROUTE_LOGIN
import com.example.serenity.ui.theme.SerenityTheme
import java.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/* ─────── top bar ─────── */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController? = null,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) { /* unchanged */
    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.NightsStay,
                        contentDescription = "Toggle Theme",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Serenity",
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    navController?.navigate("profile")
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                // Safely navigate to the profile screen if navController is not null
                                navController?.navigate("profile")
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

/* ─────── dashboard screen ─────── */
@Composable
fun DashboardScreen(navController: NavController, modifier: Modifier = Modifier) {
    var isDarkTheme by remember { mutableStateOf(false) }

    SerenityTheme(darkTheme = isDarkTheme) {
        val backgroundColor = if (isDarkTheme) Color(0xFF1B1B1B) else Color(0xFFC8E6C9)

        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            },
            containerColor = backgroundColor
        ) { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(backgroundColor)
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                /* greeting animation */
                val bounceAnim = rememberInfiniteTransition()
                val bounceOffset by bounceAnim.animateFloat(
                    initialValue = 0f,
                    targetValue = 10f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing, delayMillis = 5000),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Text(
                    text = "${getGreeting()}", // Always use firstname
                    fontSize = 38.sp,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .offset(y = bounceOffset.dp),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 40.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                /* quotes carousel */
                val quotes = listOf(
                    "You are one decision away from a completely different life. – Mel Robbins",
                    "Be the change you wish to see in the world. – Mahatma Gandhi",
                    "Your future is created by what you do today. – Robert Kiyosaki",
                    "Change is hard at first, messy in the middle and gorgeous at the end. – Robin Sharma",
                    "Turn your wounds into wisdom. – Oprah Winfrey",
                    "Suffering is a test. That’s all it is. – David Goggins",
                    "Act as if! Act as if you're a wealthy man. – Jordan Belfort"
                )
                var currentQuoteIndex by remember { mutableStateOf(0) }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(6000L)
                        currentQuoteIndex = (currentQuoteIndex + 1) % quotes.size
                    }
                }
                Text(
                    text = quotes[currentQuoteIndex],
                    fontSize = 16.sp,
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = 12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                /* four animated round buttons */
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .offset(y = 25.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedCircularButton(
                                text = "Therapists",
                                isDarkTheme = isDarkTheme,
                                onClick = { navController.navigate("therapist") }
                            )
                            AnimatedCircularButton(
                                text = "Chill Spot",
                                isDarkTheme = isDarkTheme,
                                onClick = { navController.navigate("comfort") }
                            )
                        }
                        Spacer(modifier = Modifier.height(26.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedCircularButton(
                                text = "Talk with Serene",
                                isDarkTheme = isDarkTheme,
                                onClick = { navController.navigate("chatbot") }
                            )
                            AnimatedCircularButton(
                                text = "Mood Tracking",
                                isDarkTheme = isDarkTheme,
                                onClick = { navController.navigate("mood") }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                /* two rectangular buttons */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.navigate(ROUTE_EXERCISE) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkTheme) Color(0xFF9575CD) else Color(0xFF6200EA))
                    ) {
                        Text(text = "Breathing Exercise", fontSize = 14.sp, color = Color.White)
                    }

                    Button(
                        onClick = { navController.navigate(ROUTE_CARE) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkTheme) Color(0xFF4FC3F7) else Color(0xFF03A9F4))
                    ) {
                        Text(text = "Self Care Tips", fontSize = 14.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}


/* ─────── bottom nav bar ─────── */
@Composable
fun BottomNavigationBar(navController: NavController) { /* unchanged */
    NavigationBar(containerColor = Color.Transparent) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(ROUTE_DASHBOARD) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.SelfImprovement, contentDescription = "Meditate") },
            label = { Text("Meditate") },
            selected = false,
            onClick = { navController.navigate("meditate") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = "Journal") },
            label = { Text("Journal") },
            selected = false,
            onClick = { navController.navigate("journal") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { navController.navigate("settings") }
        )
    }
}

/* ─────── animated circular button ─────── */
@Composable
fun AnimatedCircularButton(
    text: String,
    isDarkTheme: Boolean,
    onClick: () -> Unit           // new param
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    /* looping pulse */
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Button(
        onClick = {
            coroutineScope.launch {   // bounce on tap
                scale.snapTo(0.9f)
                scale.animateTo(1.05f)
            }
            onClick()                // navigate
        },
        modifier = Modifier
            .size(120.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) Color(0xFFAB47BC) else Color(0xFF8BC34A)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center)
    }
}

/* ─────── helpers & preview ─────── */
fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12      -> "Good Morning"
        hour in 12..17 -> "Good Afternoon"
        else           -> "Good Evening"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    val navController = rememberNavController()
    DashboardScreen(
        navController = navController,
        modifier = Modifier
    )
}
