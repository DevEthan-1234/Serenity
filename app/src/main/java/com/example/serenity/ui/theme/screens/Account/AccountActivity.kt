package com.example.serenity.ui.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenity.data.AuthViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.serenity.ui.theme.screens.Care.BottomNavigationBar
import com.example.serenity.ui.theme.screens.Care.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAccountInfoScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    // State variables to hold user information
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Get user data from ViewModel when user ID changes
    val currentUser = authViewModel.currentUser
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            authViewModel.getUserExtraInfo(uid) { userInfo ->
                firstname = userInfo["firstname"] ?: ""
                lastname = userInfo["lastname"] ?: ""
                username = userInfo["username"] ?: ""
                bio = userInfo["bio"] ?: ""
                age = userInfo["age"] ?: ""
                imageUrl = userInfo["imageUrl"] ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color(0xFFECEFF1) // Light background color for the container
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

            // Profile Image (if any, else show placeholder)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { /* Trigger image selection */ }
            ) {
                if (imageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Add your\nProfile Picture", fontSize = 12.sp, color = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // First Name field
            OutlinedTextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last Name field
            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Age field
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bio field
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Row with Update and Cancel buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Update button
                Button(
                    onClick = {
                        authViewModel.updateUserInfo(
                            uid = authViewModel.currentUser?.uid ?: "",
                            username = username,
                            bio = bio,
                            age = age,
                            imageUrl = imageUrl
                        ) { isSuccess, message ->
                            if (isSuccess) {
                                // Show success message
                                Toast.makeText(navController.context, "Info updated successfully", Toast.LENGTH_LONG).show()
                            } else {
                                // Show error message
                                Toast.makeText(navController.context, "Error: $message", Toast.LENGTH_LONG).show()
                            }
                            navController.popBackStack() // Navigate back to Profile Screen
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Update Info", color = Color.White)
                }

                // Cancel button
                Button(
                    onClick = {
                        navController.popBackStack() // Navigate back to Profile Screen
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
