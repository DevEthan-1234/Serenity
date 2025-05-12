package com.example.serenity.ui.theme.screens.Settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.serenity.ui.theme.screens.Care.BottomNavigationBar
import com.example.serenity.ui.theme.screens.Care.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    val context = LocalContext.current
    var showSubscriptionDialog by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }

    val cardColor = if (isDarkTheme) Color(0xFF2C2C2C) else Color.White
    val backgroundColor = if (isDarkTheme) Color(0xFF1B1B1B) else Color(0xFFF0F0F0)
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Scaffold(
        topBar = { TopBar(navController, isDarkTheme, onToggleTheme) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("profile") },
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Welcome to your Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                    Text("Tap to view or edit your personal information.", fontSize = 14.sp, color = textColor)
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPinDialog = true },
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text("Admin Privileges", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                        Text("Enter PIN to update or delete therapists.", fontSize = 14.sp, color = textColor)
                    }
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = textColor.copy(alpha = 0.4f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("About Serenity", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                    Text(
                        "Serenity is a mental health companion offering mood tracking, breathing exercises, journaling, therapist support, comfort features, and a smart chatbot.",
                        fontSize = 14.sp,
                        color = textColor
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            FeatureComparisonCard(cardColor = cardColor, textColor = textColor) {
                showSubscriptionDialog = true
            }

            Spacer(Modifier.height(32.dp))
        }

        if (showSubscriptionDialog) {
            SubscriptionDialog(
                onDismiss = { showSubscriptionDialog = false },
                onMonthlySelected = {
                    Toast.makeText(context, "Initiating Monthly Payment...", Toast.LENGTH_SHORT).show()
                    showSubscriptionDialog = false
                },
                onAnnualSelected = {
                    Toast.makeText(context, "Initiating Annual Payment...", Toast.LENGTH_SHORT).show()
                    showSubscriptionDialog = false
                }
            )
        }

        if (showPinDialog) {
            AdminPinDialog(
                onDismiss = { showPinDialog = false },
                onPinEntered = { pin ->
                    if (pin == "1234") {
                        navController.navigate("admin_access")
                        showPinDialog = false
                    } else {
                        Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun FeatureComparisonCard(cardColor: Color, textColor: Color, onSubscribeClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Feature Comparison", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Basic", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textColor, modifier = Modifier.weight(1f))
                Text("Premium", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textColor, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            Divider(Modifier.padding(vertical = 8.dp))

            FeatureRow("Basic Chatbot Response", "AI-powered Chatbot ✨", Color.Red, Color.Green)
            Divider()
            FeatureRow("Limited Mood Analysis", "In-depth Mood Analysis 🧠", Color.Red, Color.Green)
            Divider()
            FeatureRow("Limited Admin Privileges", "Full Admin Access 🔐", Color.Red, Color.Green)
            Divider()
            FeatureRow("Free Plan", "Monthly \$9.99 / Annual \$79.99 💳", Color.Red, Color.Green)

            Spacer(Modifier.height(16.dp))

            Text(
                "By subscribing, you agree to our Terms of Service and Privacy Policy. Subscriptions auto-renew until cancelled.",
                fontSize = 12.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onSubscribeClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Upgrade, contentDescription = "Upgrade", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Unlock Pro Features", color = Color.White)
            }
        }
    }
}

@Composable
fun FeatureRow(basic: String, premium: String, basicColor: Color, premiumColor: Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(basic, color = basicColor, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Text(premium, color = premiumColor, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontSize = 14.sp)
    }
}

@Composable
fun SubscriptionDialog(
    onDismiss: () -> Unit,
    onMonthlySelected: () -> Unit,
    onAnnualSelected: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Your Plan", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Select a subscription option:")
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onMonthlySelected,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Monthly - \$9.99", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAnnualSelected,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Annual - \$79.99", color = Color.White)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AdminPinDialog(onDismiss: () -> Unit, onPinEntered: (String) -> Unit) {
    var pin by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Admin Access", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Enter your admin PIN:")
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onPinEntered(pin) }) {
                Text("Enter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
