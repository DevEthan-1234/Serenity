package com.example.serenity.ui.theme.screens.MoodTracking

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.serenity.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MoodTrackingScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val scrollState = rememberScrollState()

    var showQuestions by remember { mutableStateOf(true) }
    var selectedAnswers by remember { mutableStateOf(List(7) { "" }) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE3F2FD), Color(0xFFFCE4EC))
    )

    val questionsWithOptions = listOf(
        "How did you sleep last night?" to listOf("Very poorly", "Poorly", "Okay", "Well", "Very well"),
        "How is your energy level right now?" to listOf("Very low", "Low", "Neutral", "High", "Very high"),
        "How is your mood today?" to listOf("Very bad", "Bad", "Neutral", "Good", "Excellent"),
        "How social do you feel?" to listOf("Very isolated", "Isolated", "Neutral", "Sociable", "Very sociable"),
        "How anxious do you feel?" to listOf("Extremely anxious", "Anxious", "Neutral", "Calm", "Very calm"),
        "How motivated are you today?" to listOf("Not at all", "A little", "Neutral", "Motivated", "Very motivated"),
        "How often have you smiled today?" to listOf("Not at all", "Rarely", "Sometimes", "Often", "A lot")
    )

    Scaffold(
        topBar = {
            TopBar(navController = navController, isDarkTheme = isDarkTheme, onToggleTheme = onToggleTheme)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Mood Tracking",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Answer the questions below to analyze your mood today.",
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = showQuestions,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        questionsWithOptions.forEachIndexed { index, (question, options) ->
                            Text(question, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            DropdownMenuComponent(
                                options = options,
                                selectedOption = selectedAnswers[index],
                                onOptionSelected = {
                                    val newAnswers = selectedAnswers.toMutableList()
                                    newAnswers[index] = it
                                    selectedAnswers = newAnswers
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Button(onClick = {
                            showQuestions = false
                        }) {
                            Text("Submit")
                        }
                    }
                }

                AnimatedVisibility(
                    visible = !showQuestions,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Mood Analysis:", fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))

                        val moodScores = mapOf(
                            "Very poorly" to -2,
                            "Poorly" to -1,
                            "Okay" to 0,
                            "Well" to 1,
                            "Very well" to 2,
                            "Very low" to -2,
                            "Low" to -1,
                            "Neutral" to 0,
                            "High" to 1,
                            "Very high" to 2,
                            "Very bad" to -2,
                            "Bad" to -1,
                            "Good" to 1,
                            "Excellent" to 2,
                            "Very isolated" to -2,
                            "Isolated" to -1,
                            "Sociable" to 1,
                            "Very sociable" to 2,
                            "Extremely anxious" to -2,
                            "Anxious" to -1,
                            "Calm" to 1,
                            "Very calm" to 2,
                            "Not at all" to -2,
                            "A little" to -1,
                            "Motivated" to 1,
                            "Very motivated" to 2,
                            "Rarely" to -1,
                            "Sometimes" to 0,
                            "Often" to 1,
                            "A lot" to 2
                        )

                        val totalScore = selectedAnswers.mapNotNull { moodScores[it] }.sum()

                        val (mood, emoji, suggestions) = when {
                            totalScore >= 10 -> Triple("Excited", "😄", listOf("Go for a walk", "Write in your journal", "Call a friend"))
                            totalScore >= 5 -> Triple("Happy", "😃", listOf("Celebrate small wins", "Share your joy on Instagram", "Listen to music"))
                            totalScore >= 1 -> Triple("Calm", "🌊", listOf("Try meditation", "Read a book", "Enjoy nature"))
                            totalScore == 0 -> Triple("Neutral", "😐", listOf("Reflect on your day", "Take a break", "Do breathing exercises"))
                            totalScore >= -2 -> Triple("Boredom", "😴", listOf("Try something new", "Watch a movie", "Take photos"))
                            totalScore >= -4 -> Triple("Lonely", "😔", listOf("Talk to someone", "Journal your feelings", "Engage on social media"))
                            totalScore >= -6 -> Triple("Sad", "😢", listOf("Watch comedy", "Breathe deeply", "Reach out to someone"))
                            totalScore >= -8 -> Triple("Frustration", "😠", listOf("Take a walk", "Try journaling", "Take deep breaths"))
                            totalScore >= -10 -> Triple("Anxious", "😯", listOf("Do a breathing exercise", "Avoid screens for a while", "Drink water"))
                            else -> Triple("Depression", "😞", listOf("Seek help", "Talk to a therapist", "Use a support group"))
                        }

                        Text("Mood: $mood $emoji", fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Suggestions:", fontSize = 18.sp, color = Color.Black)
                        suggestions.forEach { suggestion ->
                            MoodSuggestionButton(suggestion) {
                                when (suggestion) {
                                    "Write in your journal", "Journal your feelings" -> navController.navigate("journal")
                                    "Do a breathing exercise", "Take deep breaths", "Breathe deeply" -> navController.navigate("breathing")
                                    "Share your joy on Instagram", "Engage on social media" -> {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
                                        context.startActivity(intent)
                                    }
                                    "Take photos" -> {
                                        if (cameraPermissionState.status.isGranted) {
                                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                            context.startActivity(intent)
                                        } else {
                                            cameraPermissionState.launchPermissionRequest()
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = if (selectedOption.isEmpty()) "Select" else selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MoodSuggestionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2EBF2))
    ) {
        Text(text)
    }
}

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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
