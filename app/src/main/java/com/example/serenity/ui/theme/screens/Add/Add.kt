package com.example.serenity.ui.theme.screens.Add

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.serenity.R
import com.example.serenity.data.TherapistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTherapistScreen(navController: NavController, viewModel: TherapistViewModel = TherapistViewModel()) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(true) }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri.value = it
    }

    val rotation by animateFloatAsState(if (expanded) 45f else 0f)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Therapist") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    expanded = !expanded
                    navController.popBackStack()
                },
                containerColor = Color(0xFF388E3C),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Close",
                    modifier = Modifier.graphicsLayer { rotationZ = rotation }
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // 👈 Makes it scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image placeholder
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(10.dp)
                    .size(200.dp)
                    .clickable { launcher.launch("image/*") }
            ) {
                if (imageUri.value != null) {
                    AsyncImage(
                        model = imageUri.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(200.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(50.dp),
                        tint = Color.Gray
                    )
                }
            }

            Text(text = "Upload Picture Here")

            // Text fields
            fun Modifier.field() = this.fillMaxWidth()
            OutlinedTextField(name, { name = it }, label = { Text("Name") }, leadingIcon = { Icon(Icons.Default.Person, null) }, modifier = Modifier.field())
            OutlinedTextField(experience, { experience = it }, label = { Text("Years of Experience") }, leadingIcon = { Icon(Icons.Default.Timeline, null) }, modifier = Modifier.field())
            OutlinedTextField(age, { age = it }, label = { Text("Age") }, leadingIcon = { Icon(Icons.Default.Cake, null) }, modifier = Modifier.field())
            OutlinedTextField(gender, { gender = it }, label = { Text("Gender") }, leadingIcon = { Icon(Icons.Default.PersonOutline, null) }, modifier = Modifier.field())
            OutlinedTextField(contact, { contact = it }, label = { Text("Contact") }, leadingIcon = { Icon(Icons.Default.Phone, null) }, modifier = Modifier.field())
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, null) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.field())
            OutlinedTextField(location, { location = it }, label = { Text("Location") }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, modifier = Modifier.field(), maxLines = 1)
            OutlinedTextField(description, { description = it }, label = { Text("Description") }, leadingIcon = { Icon(Icons.Default.Description, null) }, modifier = Modifier.field(), maxLines = 3)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    if (imageUri.value != null && name.isNotBlank() && experience.isNotBlank()
                        && gender.isNotBlank() && location.isNotBlank() && description.isNotBlank()
                        && contact.isNotBlank() && email.isNotBlank()
                    ) {
                        viewModel.uploadTherapistWithImage(
                            uri = imageUri.value!!,
                            context = context,
                            name = name,
                            experience = experience,
                            gender = gender,
                            age = age,
                            location = location,
                            description = description,
                            contact = contact,
                            email = email
                        )
                    } else {
                        Toast.makeText(context, "Please fill in all fields and upload an image", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Save")
                }

                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("Cancel")
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // for padding under the buttons
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTherapistPreview() {
    AddTherapistScreen(navController = rememberNavController())
}
