package com.example.serenity.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.serenity.models.TherapistModels
import com.example.serenity.network.ImgurService
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TherapistViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference.child("Therapists")

    // LiveData for the list of therapists
    private val _therapists = MutableLiveData<List<TherapistModels>>()
    val therapists: LiveData<List<TherapistModels>> get() = _therapists

    // LiveData for loading state (true when data is being fetched)
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    // LiveData for any error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        fetchTherapists()
    }

    private fun fetchTherapists() {
        _loadingState.value = true  // Start loading

        database.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val therapistList = snapshot.children.mapNotNull { dataSnapshot ->
                    Log.d("TherapistViewModel", "fetchTherapists - Raw data for ${dataSnapshot.key}: ${dataSnapshot.value}")
                    try {
                        val therapist = dataSnapshot.getValue(TherapistModels::class.java)
                        Log.d("TherapistViewModel", "fetchTherapists - Mapped therapist (before suspend format): $therapist")

                        val suspendedUntilValue = dataSnapshot.child("suspendedUntil").value
                        val formattedDate = when (suspendedUntilValue) {
                            is Long -> {
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(suspendedUntilValue))
                            }
                            is String -> {
                                // If it's already a String, use it directly (assuming it's in the correct format)
                                suspendedUntilValue
                            }
                            else -> {
                                "" // Or "Not Suspended", or handle as needed
                            }
                        }

                        val updatedTherapist = therapist?.copy(suspendedUntil = formattedDate)
                        Log.d("TherapistViewModel", "fetchTherapists - Mapped therapist (after suspend format): $updatedTherapist")
                        updatedTherapist
                    } catch (e: Exception) {
                        Log.e("TherapistViewModel", "fetchTherapists - Error mapping therapist: ${dataSnapshot.key}", e)
                        null // Return null for this therapist if mapping fails
                    }
                }
                _therapists.value = therapistList
            } else {
                _errorMessage.value = "No therapists found."
            }
        }.addOnFailureListener { exception ->
            _errorMessage.value = exception.localizedMessage
        }.addOnCompleteListener {
            _loadingState.value = false  // Stop loading
        }
    }
    private fun getImgurService(): ImgurService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ImgurService::class.java)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
            file.outputStream().use { output ->
                inputStream?.copyTo(output)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uploadTherapistWithImage(
        uri: Uri,
        context: Context,
        name: String,
        experience: String,
        gender: String,
        age: String,
        location: String,
        description: String,
        contact: String,
        email: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = getFileFromUri(context, uri)
                if (file == null) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to process image", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)

                val response = getImgurService().uploadImage(body, "Client-ID 2fec13ef0937d77")

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link ?: ""

                    val therapistId = database.push().key ?: return@launch
                    val therapist = TherapistModels(
                        name = name,
                        experience = experience,
                        gender = gender,
                        age = age,
                        location = location,
                        description = description,
                        email = email,
                        contact = contact,
                        imageUrl = imageUrl,
                        therapistId = therapistId,
                        suspendedUntil = "" // Changed null to an empty string
                    )

                    Log.d("TherapistViewModel", "uploadTherapistWithImage - New therapist to be saved: $therapist")

                    database.child(therapistId).setValue(therapist)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Therapist saved successfully", Toast.LENGTH_SHORT).show()
                            fetchTherapists() // Re-fetch after successful save
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to save Therapist", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, "Upload error", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Exception: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteTherapist(context: Context, therapistId: String) {
        database.child(therapistId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Therapist deleted successfully", Toast.LENGTH_SHORT).show()
                fetchTherapists()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete Therapist", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateTherapist(
        context: Context,
        therapistId: String,
        updatedTherapist: TherapistModels
    ) {
        database.child(therapistId).setValue(updatedTherapist)
            .addOnSuccessListener {
                Toast.makeText(context, "Therapist updated successfully", Toast.LENGTH_SHORT).show()
                fetchTherapists()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update Therapist", Toast.LENGTH_SHORT).show()
            }
    }

    fun suspendTherapist(context: Context, therapistId: String) {
        viewModelScope.launch {
            val suspensionDurationMillis = 30L * 24 * 60 * 60 * 1000 // 30 days
            val suspendedUntilTimestamp = System.currentTimeMillis() + suspensionDurationMillis
            val suspendedUntilDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(suspendedUntilTimestamp))

            val updates = mapOf("suspendedUntil" to suspendedUntilDateString) // Store as String

            Log.d("TherapistViewModel", "suspendTherapist - Updating therapist ID: $therapistId with: $updates")

            database.child(therapistId).updateChildren(updates)
                .addOnSuccessListener {
                    Toast.makeText(context, "Therapist suspended until $suspendedUntilDateString", Toast.LENGTH_SHORT).show()
                    fetchTherapists()
                }
                .addOnFailureListener { exception ->
                    Log.e("TherapistViewModel", "Failed to suspend therapist: ${exception.message}")
                    Toast.makeText(context, "Failed to suspend Therapist", Toast.LENGTH_SHORT).show()
                }
        }
    }


    fun uploadImageToImgur(context: Context, uri: Uri, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = getFileFromUri(context, uri)
                if (file == null) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to process image", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)

                val response = getImgurService().uploadImage(body, "Client-ID 2fec13ef0937d77")

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link ?: ""
                    launch(Dispatchers.Main) {
                        onResult(imageUrl)
                    }
                } else {
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Exception: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}