package com.example.serenity.models

data class JournalModel(
    val journalId: String = "",
    val entry: String = "",
    val timestamp: Long = 0L,
    val userId: String = ""  // Optionally, add user ID if needed
)
