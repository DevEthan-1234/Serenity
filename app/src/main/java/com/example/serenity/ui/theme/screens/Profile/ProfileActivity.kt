package com.example.serenity.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.serenity.R
import com.example.serenity.data.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser = authViewModel.currentUser

    // State variables to hold user data
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Get user extra info from Firebase when current user changes
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            authViewModel.getUserExtraInfo(uid) { userInfo ->
                // Only set fields if they are non-null and not empty.
                firstname = userInfo["firstname"].takeIf { it.isNullOrEmpty().not() } ?: "Not Available"
                lastname = userInfo["lastname"].takeIf { it.isNullOrEmpty().not() } ?: "Not Available"
                username = userInfo["username"].takeIf { it.isNullOrEmpty().not() } ?: "Not Available"
                bio = userInfo["bio"].takeIf { it.isNullOrEmpty().not() } ?: "Not Available"
                age = userInfo["age"].takeIf { it.isNullOrEmpty().not() } ?: "Not Available"
                imageUrl = userInfo["imageUrl"].takeIf { it.isNullOrEmpty().not() } ?: ""
                email = currentUser.email ?: "Not Available"
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController, isDarkTheme, onToggleTheme)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = Color(0xFFECEFF1)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(Color(0xFFECEFF1))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome, $firstname",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { /* Add image picker */ }
            ) {
                if (imageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Add your\nProfile Picture", fontSize = 12.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bio Data Card with added Bio field
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Bio Data", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("First Name: $firstname")
                    Text("Last Name: $lastname")
                    Text("Username: $username")
                    Text("Age: $age")
                    Text("Email: $email")
                    Text("Bio: $bio")  // Display the Bio fetched from Firebase
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Journal Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("journal") },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("My Journals", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("View and manage your mental health journals.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Therapist Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("therapist") },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your Therapists", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Connect with mental health professionals.")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Continue Setup Button
            Button(
                onClick = { navController.navigate("continue") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("Continue Setup", color = Color.White)
            }

            // Update Account Info Button
            Button(
                onClick = { navController.navigate("account") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("Update Account Info", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("Logout", color = Color.White)
            }

            // Delete Account Button
            Button(
                onClick = {
                    authViewModel.deleteUser { success, message ->
                        if (success) {
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        } else {
                            println("Delete failed: $message")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("Delete Account", color = Color.White)
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
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
                        tint = Color.White
                    )
                }
                Text(
                    text = "Serenity",
                    fontSize = 22.sp,
                    color = Color.White,
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
            containerColor = Color(0xFF00838F)
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFF00838F)
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
