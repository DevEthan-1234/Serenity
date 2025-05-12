package com.example.serenity.ui.theme.screens.Chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.serenity.R
import com.example.serenity.ui.theme.screens.Care.BottomNavigationBar
import com.example.serenity.ui.theme.screens.Care.TopBar

data class Message(val role: String, val content: String)

class ChatBotViewModel : ViewModel() {
    var messages by mutableStateOf<List<Message>>(
        listOf(Message("assistant", "Hello! How are you feeling today?"))
    )

    fun sendMessage(userText: String) {
        val userMsg = Message("user", userText)
        val botMsg = Message("assistant", generateResponse(userText))
        messages = messages + userMsg + botMsg
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: ChatBotViewModel = viewModel()
) {
    var userInput by remember { mutableStateOf("") }
    val messages = viewModel.messages
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val animatedText = "Hi, I’m Serene — your A.I. Mental Health Care Provider. How can I help?"
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in 1..animatedText.length) {
            visibleText = animatedText.take(i)
            delay(40L)
        }
    }

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
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFCCE2FF), Color(0xFFE7EAF6))
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = visibleText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(8.dp)
                        .alpha(0.9f),
                    color = Color(0xFF374785),
                )

                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        placeholder = { Text("Type your message...") },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (userInput.isNotBlank()) {
                                viewModel.sendMessage(userInput)
                                userInput = ""
                                coroutineScope.launch {
                                    delay(100)
                                    scrollState.animateScrollToItem(messages.size)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color(0xFF3F72AF)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isUser = message.role == "user"
    val bubbleColor = if (isUser) Color(0xFFBEE3F8) else Color.White
    val horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.widthIn(0.dp, 300.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = Color.DarkGray
            )
        }
    }
}

fun generateResponse(input: String): String {
    val lowercase = input.lowercase()

    return when {
        lowercase.contains("hello") || lowercase.contains("hi") ->
            "Hello! I’m Serenity 💬, your mental health companion. How are you feeling today?"

        lowercase.contains("help") || lowercase.contains("support") ->
            "I'm here to support you. Are you feeling anxious, sad, lonely, or just overwhelmed? I can also connect you to breathing exercises, meditation, self-care, journaling, mood tracking, or a therapist."

        lowercase.contains("anxious") || lowercase.contains("panic") ->
            "I understand how tough anxiety can be. Let's try a calming breathing exercise 🌬️. Would you like me to take you there?"

        lowercase.contains("depressed") || lowercase.contains("sad") ->
            "I'm sorry you're feeling this way. You're not alone. Maybe journaling 📝 or a short meditation 🧘 could help. Want to try one?"

        lowercase.contains("stressed") || lowercase.contains("overwhelmed") ->
            "Stress can be heavy. A deep breath or a moment of mindfulness might help. Want to go to the meditation screen?"

        lowercase.contains("lonely") || lowercase.contains("alone") ->
            "Feeling lonely is hard. Talking about it helps. Would you like to write in your journal or connect to a therapist?"

        lowercase.contains("self care") || lowercase.contains("care myself") ->
            "Self-care is so important 💖. I can take you to the self-care screen where you'll find relaxing and inspiring ideas."

        lowercase.contains("track my mood") || lowercase.contains("mood") ->
            "Let’s take a quick mood check-in 📊. It only takes a moment and can really help understand how you're doing."

        lowercase.contains("journal") || lowercase.contains("write") ->
            "Writing down your thoughts can be powerful. I’ll open the journal for you 📝."

        lowercase.contains("meditate") || lowercase.contains("calm") ->
            "Let’s find your calm together. I’ll guide you to a peaceful meditation session 🧘."

        lowercase.contains("therapist") || lowercase.contains("professional") || lowercase.contains("match") ->
            "I can help match you with a mental health professional 🧑‍⚕️. Would you like to continue?"

        lowercase.contains("profile") || lowercase.contains("my data") ->
            "You can update your profile here, including your age, bio, and interests 👤."

        lowercase.contains("thank") ->
            "You're very welcome 😊. I'm always here to talk or guide you."

        else ->
            "I’m here for you. You can talk to me about anything — feelings, fears, hopes, or questions. Or say 'meditate', 'journal', 'track my mood', 'self-care', or 'therapist' to explore more 💙."
    }
}
