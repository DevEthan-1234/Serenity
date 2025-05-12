package com.example.serenity.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TherapistModels(
    val therapistId: String = "",
    val name: String = "",
    val experience: String = "",
    val gender: String = "",
    val age: String = "",
    val location: String = "",
    val description: String = "",
    val contact: String = "",
    val email: String = "",
    val imageUrl: String = "",
    val suspendedUntil: String = "",
) : Parcelable
