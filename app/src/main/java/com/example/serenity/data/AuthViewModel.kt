package com.example.serenity.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.serenity.models.UserModel
import com.example.serenity.navigation.ROUTE_DASHBOARD
import com.example.serenity.navigation.ROUTE_LOGIN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AuthViewModel : ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    val currentUser get() = mAuth.currentUser

    // Sign up function
    fun signup(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        navController: NavController,
        context: Context
    ) {
        if (firstname.isBlank() || lastname.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
            return
        }

        _isLoading.value = true
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid ?: ""
                    val userData = UserModel(
                        firstname = firstname,
                        lastname = lastname,
                        email = email,
                        password = password,
                        userId = userId
                    )
                    saveUserToDatabase(userId, userData, navController, context)
                } else {
                    _errorMessage.value = task.exception?.message
                    Toast.makeText(context, "Registration failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Save user to Firebase database under "users/"
    private fun saveUserToDatabase(
        userId: String,
        userData: UserModel,
        navController: NavController,
        context: Context
    ) {
        val regRef = FirebaseDatabase.getInstance().getReference("users/$userId")
        regRef.setValue(userData).addOnCompleteListener { regRef ->
            if (regRef.isSuccessful) {
                Toast.makeText(context, "User successfully Registered", Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_LOGIN)
            } else {
                _errorMessage.value = regRef.exception?.message
                Toast.makeText(context, "Database error", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Login function
    fun login(
        email: String,
        password: String,
        context: Context,
        onLoginSuccess: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and Password required", Toast.LENGTH_LONG).show()
            return
        }

        _isLoading.value = true
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userId = user?.uid ?: ""
                    val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")
                    userRef.get().addOnSuccessListener { dataSnapshot ->
                        val username = dataSnapshot.child("UserExtras").child("username").getValue(String::class.java) ?: ""
                        onLoginSuccess(username)
                        Toast.makeText(context, "User Successfully logged in", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error fetching user data", Toast.LENGTH_LONG).show()
                    }
                } else {
                    _errorMessage.value = task.exception?.message
                    Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Logout
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    // Delete user and user data from database
    fun deleteUser(onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (user != null && uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue()
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "No user is currently signed in.")
        }
    }

    // Save extra user info
    fun saveUserExtraInfo(
        uid: String,
        username: String,
        bio: String,
        age: String,
        imageUrl: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        val extraInfo = mapOf(
            "username" to username,
            "bio" to bio,
            "age" to age,
            "imageUrl" to imageUrl
        )

        val database = FirebaseDatabase.getInstance().reference
        database.child("users/$uid/UserExtras").setValue(extraInfo)
            .addOnSuccessListener {
                onResult(true, "Info saved successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Unknown error occurred")
            }
    }

    // Get user extra info without default "Guest" data

    // Get user extra info without default "Guest" data
    // Get user extra info without default "Guest" data
    fun getUserExtraInfo(uid: String, callback: (Map<String, String>) -> Unit) {
        val rootRef = FirebaseDatabase.getInstance().getReference("users/$uid")
        Log.d("AuthViewModel", "getUserExtraInfo: Fetching data for UID: $uid")

        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfo = mutableMapOf<String, String>()

                Log.d("AuthViewModel", "getUserExtraInfo: DataSnapshot exists: ${snapshot.exists()}")

                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        Log.d("AuthViewModel", "getUserExtraInfo: Found child with key: ${child.key} and value: ${child.value}")
                    }

                    val firstnameSnapshot = snapshot.child("firstname")
                    val firstname = firstnameSnapshot.getValue(String::class.java)
                    Log.d("AuthViewModel", "getUserExtraInfo: firstnameSnapshot exists: ${firstnameSnapshot.exists()}, value: $firstname")
                    userInfo["firstname"] = firstname ?: ""

                    val lastnameSnapshot = snapshot.child("lastname")
                    val lastname = lastnameSnapshot.getValue(String::class.java)
                    Log.d("AuthViewModel", "getUserExtraInfo: lastnameSnapshot exists: ${lastnameSnapshot.exists()}, value: $lastname")
                    userInfo["lastname"] = lastname ?: ""

                    val extrasSnapshot = snapshot.child("UserExtras")
                    Log.d("AuthViewModel", "getUserExtraInfo: UserExtras exists: ${extrasSnapshot.exists()}")
                    if (extrasSnapshot.exists()) {
                        for (child in extrasSnapshot.children) {
                            Log.d("AuthViewModel", "getUserExtraInfo: UserExtras child - key: ${child.key}, value: ${child.value}")
                        }
                        userInfo["username"] = extrasSnapshot.child("username").getValue(String::class.java) ?: ""
                        userInfo["bio"] = extrasSnapshot.child("bio").getValue(String::class.java) ?: ""
                        userInfo["age"] = extrasSnapshot.child("age").getValue(String::class.java) ?: ""
                        userInfo["imageUrl"] = extrasSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    }
                }

                Log.d("AuthViewModel", "getUserExtraInfo: Callback with userInfo: $userInfo")
                callback(userInfo)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AuthViewModel", "getUserExtraInfo: Database error fetching user info: ${error.message}")
                callback(emptyMap())
            }
        })
    }

    // Update user extra info
    fun updateUserInfo(
        uid: String,
        username: String,
        bio: String,
        age: String,
        imageUrl: String = "",
        onResult: (Boolean, String?) -> Unit
    ) {
        val updatedUserInfo = mapOf(
            "username" to username,
            "bio" to bio,
            "age" to age,
            "imageUrl" to imageUrl
        )

        val database = FirebaseDatabase.getInstance().reference
        database.child("users/$uid/UserExtras").updateChildren(updatedUserInfo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "User info updated successfully")
                } else {
                    onResult(false, task.exception?.message ?: "Failed to update user info")
                }
            }
    }
}
