package com.example.serenity.ui.theme.screens.List

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.serenity.data.JournalViewModel
import com.example.serenity.models.JournalModel
import com.example.serenity.ui.screens.Continue.TopBar
import com.example.serenity.ui.theme.screens.journal.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ListScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val viewModel: JournalViewModel = viewModel()
    val journals by viewModel.journals.collectAsState()

    Scaffold(
        topBar = { TopBar(navController, isDarkTheme, onToggleTheme) },
        bottomBar = { BottomNavigationBar(navController, firstname = "") },
        containerColor = if (isDarkTheme) Color(0xFF1B1B1B) else Color(0xFFC8E6C9)
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (journals.isEmpty()) {
                Text("No journals found", color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(journals) { journal ->
                        JournalCard(journal = journal, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun JournalCard(journal: JournalModel, viewModel: JournalViewModel) {
    val context = LocalContext.current
    val formattedDate = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(journal.timestamp))

    var showDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("") }
    var customReason by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF81C784), Color(0xFF388E3C))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Journal ID: ${journal.journalId}", style = TextStyle(color = Color.Gray, fontSize = 12.sp))
            Text(journal.entry, style = TextStyle(color = Color.Black, fontSize = 16.sp))
            Text("Saved on: $formattedDate", style = TextStyle(color = Color.Gray, fontSize = 12.sp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GradientOutlinedButton(
                    text = "Like",
                    icon = Icons.Filled.ThumbUp,
                    gradient = Brush.horizontalGradient(listOf(Color(0xFF64B5F6), Color(0xFF1976D2))),
                    onClick = { Toast.makeText(context, "Liked!", Toast.LENGTH_SHORT).show() }
                )
                GradientOutlinedButton(
                    text = "Report",
                    icon = Icons.Filled.Flag,
                    gradient = Brush.horizontalGradient(listOf(Color(0xFFFFB74D), Color(0xFFF57C00))),
                    onClick = { showDialog = true }
                )
                GradientOutlinedButton(
                    text = "Delete",
                    icon = Icons.Filled.Delete,
                    gradient = Brush.horizontalGradient(listOf(Color(0xFFFF5252), Color(0xFFD32F2F))),
                    onClick = { showDeleteConfirmation = true }
                )
            }
        }
    }

    // Report Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = android.net.Uri.parse("mailto:ethannganga1@outlook.com")
                        putExtra(Intent.EXTRA_SUBJECT, "Journal Report - ID: ${journal.journalId}")
                        val body = buildString {
                            append("📝 Journal ID: ${journal.journalId}\n")
                            append("📅 Timestamp: $formattedDate\n")
                            append("📄 Text: ${journal.entry}\n\n")
                            append("🚩 Reason: $selectedReason\n")
                            if (selectedReason == "Other Reasons") {
                                append("🗒️ Details: $customReason\n")
                            }
                        }
                        putExtra(Intent.EXTRA_TEXT, body)
                    }
                    if (emailIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(emailIntent)
                    } else {
                        Toast.makeText(context, "No email app found.", Toast.LENGTH_SHORT).show()
                    }
                    showDialog = false
                }) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
            title = { Text("Report Journal") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val reasons = listOf(
                        "Sexual Content", "Inappropriate", "Propaganda",
                        "Insulting", "Violent", "Other Reasons"
                    )

                    reasons.forEach { reason ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            RadioButton(selected = selectedReason == reason, onClick = { selectedReason = reason })
                            Text(reason)
                        }
                    }

                    if (selectedReason == "Other Reasons") {
                        OutlinedTextField(
                            value = customReason,
                            onValueChange = { customReason = it },
                            label = { Text("Please describe") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteJournal(journal.journalId)
                    showDeleteConfirmation = false
                    Toast.makeText(context, "Journal deleted", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Confirm Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) { Text("Cancel") }
            },
            title = { Text("Delete Journal") },
            text = { Text("Are you sure you want to delete this journal? This action cannot be undone.") }
        )
    }
}

@Composable
fun GradientOutlinedButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: Brush,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .padding(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        border = BorderStroke(2.dp, gradient),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.padding(end = 4.dp))
        Text(text)
    }
}
