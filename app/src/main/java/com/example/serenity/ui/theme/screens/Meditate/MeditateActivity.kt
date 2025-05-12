package com.example.serenity.ui.theme.screens.Meditate

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.serenity.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditateScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var isMeditationInProgress by remember { mutableStateOf(false) }
    var meditationTime by remember { mutableStateOf(0L) }
    var timer: CountDownTimer? by remember { mutableStateOf(null) }

    val startMeditationTimer: () -> Unit = {
        meditationTime = 0L
        timer?.cancel()
        timer = object : CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                meditationTime = 600000 - millisUntilFinished
            }
            override fun onFinish() {
                isMeditationInProgress = false
            }
        }
        timer?.start()
        isMeditationInProgress = true
    }

    val stopMeditation: () -> Unit = {
        timer?.cancel()
        isMeditationInProgress = false
    }

    val meditationTimeFormatted = String.format("%02d:%02d", meditationTime / 60000, (meditationTime / 1000) % 60)
    val backgroundColor = if (isDarkTheme) Color(0xFF263238) else Color(0xFFB2EBF2)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val secondaryTextColor = if (isDarkTheme) Color.LightGray else Color.Gray

    Scaffold(
        topBar = {
            TopBar(navController = navController, isDarkTheme = isDarkTheme, onToggleTheme = onToggleTheme)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = backgroundColor
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.meditation),
                    contentDescription = "Meditation Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Meditate",
                    fontSize = 24.sp,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = meditationTimeFormatted,
                    fontSize = 36.sp,
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (!isMeditationInProgress) {
                    Button(
                        onClick = startMeditationTimer,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                    ) {
                        Text("Start Meditation")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("comfort")
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                    ) {
                        Text("Want to take a deep rest and relax")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("exercise")
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                    ) {
                        Text("Start a Breathing Exercise")
                    }
                } else {
                    Button(
                        onClick = stopMeditation,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                    ) {
                        Text("Stop Meditation")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isMeditationInProgress) {
                    LinearProgressIndicator(
                        progress = meditationTime.toFloat() / 600000f,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Today's Challenge: Meditate for 10 minutes daily!",
                    fontSize = 16.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Remember: Meditation reduces stress and boosts focus.",
                    fontSize = 14.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Tip: Focus on your breath and clear your mind for a peaceful experience.",
                    fontSize = 14.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Meditation Achievements",
                    fontSize = 20.sp,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "• Meditated for 5 days in a row",
                        fontSize = 14.sp,
                        color = secondaryTextColor
                    )
                    Text(
                        text = "• Completed 10 minutes of meditation",
                        fontSize = 14.sp,
                        color = secondaryTextColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TopBar(
    navController: NavController? = null,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
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
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFB2EBF2)
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFFB2EBF2)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("dashboard") }
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
