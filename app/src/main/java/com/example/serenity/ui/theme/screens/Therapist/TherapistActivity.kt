// Package and imports
package com.example.serenity.ui.theme.screens.Therapist

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.serenity.R
import com.example.serenity.data.TherapistViewModel
import com.example.serenity.models.TherapistModels
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController?, isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    Column {
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
                    IconButton(onClick = { navController?.navigate("profile") }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "Profile",
                            modifier = Modifier.size(36.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
        Text(
            text = "Take a look at our Therapists...",
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, bottom = 8.dp),
            textAlign = TextAlign.Start
        )
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapistScreen(navController: NavController, viewModel: TherapistViewModel = viewModel()) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 45f else 0f)

    // Observing therapists' list and making sure it's always non-null
    val therapists by viewModel.therapists.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopBar(navController = navController, isDarkTheme = isDarkTheme) {
                isDarkTheme = !isDarkTheme
            }
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(visible = expanded) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            expanded = false
                            navController.navigate("add")
                        },
                        icon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
                        text = { Text("Add Therapist") },
                        containerColor = if (isDarkTheme) Color(0xFF6A1B9A) else Color(0xFF8E24AA),
                        contentColor = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    containerColor = if (isDarkTheme) Color(0xFF4CAF50) else Color(0xFF388E3C),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = if (expanded) "Close" else "Add",
                        modifier = Modifier.graphicsLayer { rotationZ = rotation }
                    )
                }
            }
        },
        containerColor = if (isDarkTheme) Color(0xFF1B1B1B) else Color(0xFFC8E6C9)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Ensure LazyColumn handles the list properly
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Safely map over the list
                items(therapists) { therapist ->
                    // Pass only the therapist data to TherapistCard
                    TherapistCard(therapist = therapist)
                }
            }
        }
    }
}



@Composable
fun TherapistCard(therapist: TherapistModels, context: Context = LocalContext.current) {
    var expanded by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("") }
    var otherReason by remember { mutableStateOf(TextFieldValue("")) }

    // Parse suspendedUntil date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val suspendedDate: Date? = try {
        dateFormat.parse(therapist.suspendedUntil ?: "")
    } catch (e: Exception) {
        null
    }
    val isSuspended = suspendedDate != null && Date().before(suspendedDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberImagePainter(therapist.imageUrl),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = therapist.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Age: ${therapist.age}")
                        Text(text = "Gender: ${therapist.gender}")
                        Text(text = "Location: ${therapist.location}")
                    }
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Show Details")
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Experience: ${therapist.experience} years")
                Text(text = "Contact: ${therapist.contact}")
                Text(text = "Email: ${therapist.email}")
                Text(text = "Description: ${therapist.description}")
            }

            // Show "SUSPENDED" if applicable
            if (isSuspended) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "SUSPENDED",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${therapist.contact}"))
                        context.startActivity(intent)
                    },
                    enabled = !isSuspended
                ) { Icon(Icons.Default.Phone, contentDescription = "Call") }

                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${therapist.contact}"))
                        context.startActivity(intent)
                    },
                    enabled = !isSuspended
                ) { Icon(Icons.Default.Message, contentDescription = "Message") }

                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Check out therapist ${therapist.name}, contact: ${therapist.contact}")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Therapist"))
                    },
                    enabled = !isSuspended
                ) { Icon(Icons.Default.Share, contentDescription = "Share") }

                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${therapist.email}")
                        }
                        context.startActivity(intent)
                    },
                    enabled = !isSuspended
                ) { Icon(Icons.Default.Email, contentDescription = "Email") }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showReportDialog = true }
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFE53935), Color(0xFFD81B60))
                        )
                    )
                    .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Report, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Report", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            if (showReportDialog) {
                AlertDialog(
                    onDismissRequest = { showReportDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (selectedReason.isNotBlank()) {
                                val finalReason = if (selectedReason == "Other Reason") otherReason.text else selectedReason
                                val emailBody = """
                                    🚨 Therapist Report Alert 🚨

                                    👤 Name: ${therapist.name}
                                    🎂 Age: ${therapist.age}
                                    🚻 Gender: ${therapist.gender}
                                    📍 Location: ${therapist.location}
                                    🧠 Experience: ${therapist.experience} years
                                    📞 Contact: ${therapist.contact}
                                    📧 Email: ${therapist.email}
                                    📝 Description: ${therapist.description}

                                    ❗ Reason for report:
                                    $finalReason
                                """.trimIndent()

                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:ethannganga1@outlook.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "🚩 Report: Therapist ${therapist.name}")
                                    putExtra(Intent.EXTRA_TEXT, emailBody)
                                }

                                try {
                                    context.startActivity(intent)
                                    Toast.makeText(context, "Launching email to report ${therapist.name}", Toast.LENGTH_SHORT).show()
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "No email app found. Please install one.", Toast.LENGTH_LONG).show()
                                }

                                showReportDialog = false
                            }
                        }) {
                            Text("Submit")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showReportDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Report Therapist") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val reasons = listOf("Sexual Harassment", "Propaganda", "Violent", "Gender-Biased", "Rude", "Other Reason")
                            reasons.forEach { reason ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedReason = reason }
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedReason == reason,
                                        onClick = { selectedReason = reason }
                                    )
                                    Text(text = reason)
                                }
                            }
                            if (selectedReason == "Other Reason") {
                                OutlinedTextField(
                                    value = otherReason,
                                    onValueChange = { otherReason = it },
                                    label = { Text("Specify reason") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TherapistScreenPreview() {
    val navController = rememberNavController()
    TherapistScreen(navController = navController)
}
