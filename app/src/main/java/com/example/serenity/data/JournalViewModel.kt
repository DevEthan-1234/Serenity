package com.example.serenity.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.serenity.models.JournalModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JournalViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private val _journalText = MutableStateFlow("")
    val journalText: StateFlow<String> = _journalText

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveSuccess = MutableStateFlow<Boolean?>(null)
    val saveSuccess: StateFlow<Boolean?> = _saveSuccess

    private val _journals = MutableStateFlow<List<JournalModel>>(emptyList())
    val journals: StateFlow<List<JournalModel>> = _journals

    private var journalListener: ValueEventListener? = null

    init {
        loadLatestJournalEntry()
        loadJournals()
    }

    fun onJournalTextChanged(newText: String) {
        _journalText.value = newText
    }

    fun saveJournalEntry() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid == null) {
            _saveSuccess.value = false
            return
        }

        _isSaving.value = true

        val journalEntry = JournalModel(
            journalId = "",  // We'll assign ID manually
            entry = _journalText.value,
            timestamp = System.currentTimeMillis()
        )

        val journalRef = database.child("journals").child(currentUid).push()

        journalRef.setValue(journalEntry).addOnCompleteListener { task ->
            _isSaving.value = false
            if (task.isSuccessful) {
                _saveSuccess.value = true
            } else {
                _saveSuccess.value = false
            }
        }
    }

    private fun loadLatestJournalEntry() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        database.child("journals").child(currentUid)
            .orderByChild("timestamp")
            .limitToLast(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val lastEntry = snapshot.children.firstOrNull()
                    ?.child("entry")
                    ?.getValue(String::class.java)
                _journalText.value = lastEntry ?: ""
            }
    }

    fun clearSaveResult() {
        _saveSuccess.value = null
    }

    private fun loadJournals() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val journalRef = database.child("journals").child(currentUid)

        // Avoid attaching multiple listeners
        if (journalListener != null) {
            journalRef.removeEventListener(journalListener!!)
        }

        journalListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val journalList = snapshot.children.mapNotNull { child ->
                    val journal = child.getValue(JournalModel::class.java)
                    journal?.copy(journalId = child.key ?: "")
                }
                Log.d("JournalDebug", "Realtime loaded ${journalList.size} journals")
                _journals.value = journalList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("JournalDebug", "Realtime failed to load journals: ${error.message}")
            }
        }

        journalRef.orderByChild("timestamp").addValueEventListener(journalListener!!)
    }

    override fun onCleared() {
        super.onCleared()
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val journalRef = database.child("journals").child(currentUid)
        journalListener?.let {
            journalRef.removeEventListener(it)
        }
    }
    fun deleteJournal(journalId: String) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val journalRef = FirebaseDatabase.getInstance().reference
            .child("journals")  // lowercase 'journals' to match Firebase
            .child(currentUid)
            .child(journalId)

        journalRef.removeValue()
            .addOnSuccessListener {
                Log.d("JournalViewModel", "Journal deleted successfully from Firebase")
            }
            .addOnFailureListener { exception ->
                Log.e("JournalViewModel", "Failed to delete journal from Firebase", exception)
            }
    }
}
