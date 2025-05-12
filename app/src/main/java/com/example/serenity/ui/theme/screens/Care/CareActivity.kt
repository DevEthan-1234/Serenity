package com.example.serenity.ui.theme.screens.Care

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.compose.rememberNavController
import com.example.serenity.R
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

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
    NavigationBar(
        containerColor = Color.Transparent
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

@Composable
fun SelfCareScreen(navController: NavController) {
    val navController = rememberNavController()
    var isDarkTheme by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
        containerColor = if (isDarkTheme) Color(0xFF1B1B1B) else Color(0xFFE8F5E9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Self Care Tips",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF43A047),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Below is a detailed way of taking care of your mental health:",
                fontSize = 16.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("1. Practice mindfulness and meditation.", fontSize = 18.sp, color = Color(0xFF1B5E20))
            Text("2. Maintain a balanced diet and regular sleep schedule.", fontSize = 18.sp, color = Color(0xFF2E7D32))
            Text("3. Stay physically active and get regular exercise.", fontSize = 18.sp, color = Color(0xFF388E3C))
            Text("4. Connect with supportive people and talk about your feelings.", fontSize = 18.sp, color = Color(0xFF43A047))
            Text("5. Avoid alcohol and drugs.", fontSize = 18.sp, color = Color(0xFF4CAF50))

            Spacer(modifier = Modifier.height(24.dp))

            Text("📊 Mental Health Disorders Rising Globally:", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Red)

            Spacer(modifier = Modifier.height(16.dp))

            val chartModel = entryModelOf(10, 25, 45, 60, 80, 95)
            Chart(
                chart = lineChart(),
                model = chartModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("📃 Common Causes of Poor Mental Health:", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6A1B9A))
            Text("• Stress from work, school, or relationships.", fontSize = 16.sp)
            Text("• Trauma or history of abuse.", fontSize = 16.sp)
            Text("• Loneliness or social isolation.", fontSize = 16.sp)
            Text("• Financial problems.", fontSize = 16.sp)
            Text("• Chronic medical conditions.", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text("🧠 Other Important Facts:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF00838F))
            Text("• According to WHO, depression affects over 280 million people globally.", fontSize = 16.sp)
            Text("• Anxiety disorders are the most common mental illnesses worldwide.", fontSize = 16.sp)
            Text("• Suicide is the fourth leading cause of death among 15-29-year-olds.", fontSize = 16.sp)
            Text("• Early intervention and awareness reduce long-term mental health risks.", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelfCareScreenPreview() {
    SelfCareScreen(rememberNavController())
}
