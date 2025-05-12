package com.example.serenity.ui.theme.screens.Exercise

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.serenity.R
import com.example.serenity.ui.theme.screens.Comfort.BottomNavigationBar
import com.example.serenity.ui.theme.screens.Comfort.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.Transparent) {
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

@Composable
fun ExerciseScreenContent(navController: NavController) {
    var timer by remember { mutableStateOf(10) }
    var cycle by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }
    var isHolding by remember { mutableStateOf(true) }
    var totalDuration by remember { mutableStateOf(10) }

    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            if (!isPaused) {
                totalDuration = if (isHolding) 10 + (cycle * 5) else 5
                val duration = totalDuration

                scope.launch {
                    scale.animateTo(
                        targetValue = if (isHolding) 1.3f else 1f,
                        animationSpec = tween(duration * 1000, easing = LinearEasing)
                    )
                }

                for (i in duration downTo 0) {
                    if (isPaused) break
                    timer = i
                    delay(1000L)
                }

                if (!isPaused) {
                    isHolding = !isHolding
                    if (isHolding) cycle++
                }
            } else {
                delay(200L)
            }
        }
    }

    val progress = remember(timer, totalDuration) {
        if (totalDuration == 0) 0f else (totalDuration - timer) / totalDuration.toFloat()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .clip(CircleShape)
                .background(if (isHolding) Color(0xFF80DEEA) else Color(0xFFB2EBF2)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isHolding) "Hold Breath" else "Release",
                    fontSize = 24.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$timer",
                    fontSize = 40.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { isPaused = !isPaused },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPaused) Color(0xFFEF5350) else Color(0xFFEF9A9A),
                contentColor = Color.White
            ),
            shape = CircleShape,
            modifier = Modifier
                .height(50.dp)
                .width(150.dp)
        ) {
            Text(text = if (isPaused) "Resume" else "Pause", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = if (isHolding) Color(0xFF80DEEA) else Color(0xFFB2EBF2),
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopAndBottomBarPreview() {
    val navController = rememberNavController()
    var isDarkTheme by remember { mutableStateOf(false) }

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
        containerColor = Color.Transparent // to ensure background shows through
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ExerciseScreenContent(navController)
        }
    }
}
