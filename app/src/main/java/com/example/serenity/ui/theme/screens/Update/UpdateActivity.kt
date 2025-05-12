package com.example.serenity.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.serenity.data.TherapistViewModel
import com.example.serenity.models.TherapistModels
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun ProfileImagePreview(imageUri: Uri?, imageUrl: String?) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUri ?: imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Therapist Image",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun UpdateTherapistScreen(
    therapist: TherapistModels,
    navController: NavController,
    viewModel: TherapistViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf(therapist.name) }
    var experience by remember { mutableStateOf(therapist.experience) }
    var gender by remember { mutableStateOf(therapist.gender) }
    var age by remember { mutableStateOf(therapist.age) }
    var location by remember { mutableStateOf(therapist.location) }
    var description by remember { mutableStateOf(therapist.description) }
    var contact by remember { mutableStateOf(therapist.contact) }
    var email by remember { mutableStateOf(therapist.email) }
    var imageUrl by remember { mutableStateOf(therapist.imageUrl) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            ProfileImagePreview(imageUri = imageUri, imageUrl = imageUrl)

            Text(
                text = if (imageUri != null) "New image selected" else "Current image in use",
                color = Color.Gray
            )

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Choose New Image")
            }

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            OutlinedTextField(value = experience, onValueChange = { experience = it }, label = { Text("Experience") })
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") })
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Contact") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }

                Button(
                    onClick = {
                        isUploading = true
                        if (imageUri != null) {
                            viewModel.uploadImageToImgur(context, imageUri!!) { uploadedUrl ->
                                val updated = therapist.copy(
                                    name = name,
                                    experience = experience,
                                    gender = gender,
                                    age = age,
                                    location = location,
                                    description = description,
                                    contact = contact,
                                    email = email,
                                    imageUrl = uploadedUrl
                                )
                                viewModel.updateTherapist(context, therapist.therapistId, updated)
                                isUploading = false
                                navController.popBackStack()
                            }
                        } else {
                            val updated = therapist.copy(
                                name = name,
                                experience = experience,
                                gender = gender,
                                age = age,
                                location = location,
                                description = description,
                                contact = contact,
                                email = email,
                                imageUrl = imageUrl
                            )
                            viewModel.updateTherapist(context, therapist.therapistId, updated)
                            isUploading = false
                            navController.popBackStack()
                        }
                    },
                    enabled = !isUploading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Update", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(60.dp)) // Bottom padding for nav bar
        }
    }
}
