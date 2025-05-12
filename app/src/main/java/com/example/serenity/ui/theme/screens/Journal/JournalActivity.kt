package com.example.serenity.ui.theme.screens.journal

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.serenity.R
import com.example.serenity.data.JournalViewModel

/* ------------ TOP BAR ------------- */
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = { navController?.navigate("profile") }) {
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

/* ------------ BOTTOM NAV BAR ------------- */
@Composable
fun BottomNavigationBar(navController: NavController, firstname: String) {
    NavigationBar(containerColor = Color.Transparent) {

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = {
                navController.navigate("dashboard") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }
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
            selected = true,
            onClick = { /* already here */ }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { navController.navigate("settings") }
        )
    }
}

/* ------------ JOURNAL SCREEN ------------- */
@Composable
fun JournalScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val viewModel: JournalViewModel = viewModel()
    val journalText by viewModel.journalText.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    var showSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(navController, isDarkTheme, onToggleTheme)
        },
        bottomBar = {
            BottomNavigationBar(navController, firstname = "")
        },
        containerColor = if (isDarkTheme) Color(0xFF1B1B1B) else Color(0xFFC8E6C9)
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /* --- Journal text field --- */
            OutlinedTextField(
                value = journalText,
                onValueChange = viewModel::onJournalTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text("Write your thoughts here...") },
                textStyle = LocalTextStyle.current.copy(
                    color = if (isDarkTheme) Color.White else Color.Black,
                    fontSize = 16.sp
                ),
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )

            /* --- Save button --- */
            Button(
                onClick = {
                    viewModel.saveJournalEntry()
                    showSnackbar = true // Show the snackbar when the entry is saved
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Journal")
            }

            /* --- Journals button --- */
            Button(
                onClick = { navController.navigate("view") },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Icon(Icons.Default.Book, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("View Journals")
            }

            // --- Show Snackbar with success/error message ---
            if (showSnackbar) {
                val message = if (saveSuccess == true) {
                    "Your journal has been successfully saved."
                } else {
                    "Your journal could not be saved. Please try again."
                }

                Snackbar(
                    modifier = Modifier.padding(bottom = 16.dp),
                    content = { Text(text = message) },
                    action = {
                        IconButton(onClick = { showSnackbar = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss")
                        }
                    }
                )
            }
        }
    }
}