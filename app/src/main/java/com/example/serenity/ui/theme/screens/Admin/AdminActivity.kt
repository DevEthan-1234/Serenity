package com.example.serenity.ui.theme.screens.Admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.serenity.R
import com.example.serenity.data.TherapistViewModel
import com.example.serenity.models.TherapistModels
import com.example.serenity.navigation.ROUTE_LIST
import com.example.serenity.navigation.ROUTE_UPDATE
import com.example.serenity.ui.theme.screens.Care.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    navController: NavController? = null,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
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

        Column(Modifier.padding(start = 16.dp, bottom = 8.dp)) {
            Text(
                text = "Admin Privileges",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
            Text(
                text = "You have unlocked Admin Privileges",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAccessScreen(
    navController: NavController,
    viewModel: TherapistViewModel = viewModel(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 45f else 0f)
    val therapists by viewModel.therapists.observeAsState(initial = emptyList())
    val loadingState by viewModel.loadingState.observeAsState(initial = false)
    val errorMessageState by viewModel.errorMessage.observeAsState(initial = "")

    LaunchedEffect(therapists) {
        Log.d("AdminAccessScreen", "Therapists list updated: $therapists")
    }

    Scaffold(
        topBar = {
            AdminTopBar(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(visible = expanded) {
                    Column {
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

                        ExtendedFloatingActionButton(
                            onClick = {
                                expanded = false
                                navController.navigate(ROUTE_LIST)
                            },
                            icon = { Icon(Icons.Default.Note, contentDescription = null) },
                            text = { Text("Journals") },
                            containerColor = if (isDarkTheme) Color(0xFF1976D2) else Color(0xFF2196F3),
                            contentColor = Color.White,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
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
            when {
                loadingState -> CircularProgressIndicator(color = Color(0xFF6A1B9A))
                errorMessageState.isNotEmpty() -> Text(
                    text = errorMessageState,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(therapists) { therapist ->
                        AdminTherapistCard(therapist = therapist, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminTherapistCard(
    therapist: TherapistModels,
    navController: NavController,
    context: Context = LocalContext.current
) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel: TherapistViewModel = viewModel()
    val isSuspended = !therapist.suspendedUntil.isNullOrEmpty() && therapist.suspendedUntil != "Unknown" && therapist.suspendedUntil != ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(therapist.imageUrl),
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
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Show Details"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (expanded) {
                    Text(text = "Experience: ${therapist.experience} years")
                    Text(text = "Contact: ${therapist.contact}")
                    Text(text = "Email: ${therapist.email}")
                    Text(text = "Description: ${therapist.description}")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isSuspended) {
                        Text(
                            text = "Suspended until: ${therapist.suspendedUntil}",
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                } else {
                    if (isSuspended) {
                        Text(
                            text = "SUSPENDED",
                            color = Color.Red,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

            if (isSuspended) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red.copy(alpha = 0.2f)) // Optional red overlay
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("therapist", therapist)
                    navController.navigate(ROUTE_UPDATE)
                },
                enabled = !isSuspended // Disable if suspended
            ) {
                Text("Update")
            }

            Button(
                onClick = {
                    viewModel.suspendTherapist(context, therapist.therapistId)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSuspended) Color.Gray else Color(0xFF6A1B9A)
                ),
                enabled = !isSuspended // Disable if suspended
            ) {
                Text("Suspend", color = Color.White)
            }

            Button(
                onClick = {
                    viewModel.deleteTherapist(context, therapist.therapistId)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSuspended) Color.Gray else Color.Red
                ),
                enabled = !isSuspended // Disable if suspended
            ) {
                Text("Delete", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminAccessScreenPreview() {
    val navController = rememberNavController()
    AdminAccessScreen(
        navController = navController,
        isDarkTheme = false,
        onToggleTheme = {}
    )
}