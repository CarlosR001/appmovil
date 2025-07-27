package com.carlos.movilwordkapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.carlos.movilwordkapp.model.UserProfile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    val profiles = mutableStateOf<List<UserProfile>>(emptyList())
    private val _matches = MutableStateFlow<List<UserProfile>>(emptyList())
    val matches = _matches.asStateFlow()
    val showMatchAnimation = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = Firebase.auth.currentUser?.uid

    init {
        fetchProfiles()
    }

    fun fetchProfiles(minAge: Int = 18, maxAge: Int = 99) {
        // ... (esta función no cambia, la dejamos como está)
    }

    fun dismissProfile(direction: SwipeDirection) {
        if (currentUserId == null) return // No se puede dar like si no estás logueado

        val currentList = profiles.value.toMutableList()
        if (currentList.isNotEmpty()) {
            val dismissedProfile = currentList.first()

            if (direction == SwipeDirection.RIGHT) {
                // --- INICIO DE LA NUEVA LÓGICA ---
                // Registramos el "like" en el perfil de la otra persona
                db.collection("users").document(dismissedProfile.id)
                    .update("likedBy", FieldValue.arrayUnion(currentUserId))
                    .addOnSuccessListener {
                        Log.d("HomeViewModel", "Like de $currentUserId a ${dismissedProfile.id} guardado.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("HomeViewModel", "Error al guardar like", e)
                    }
                // --- FIN DE LA NUEVA LÓGICA ---


                // La lógica de match se mantiene como una simulación por ahora
                val profilesWhoLikedMe = setOf("2", "3") // Simulación
                if (profilesWhoLikedMe.contains(dismissedProfile.id)) {
                    val currentMatches = _matches.value.toMutableList()
                    currentMatches.add(dismissedProfile)
                    _matches.value = currentMatches
                    showMatchAnimation.value = true
                }
            }
            // Eliminamos el perfil de la pila local
            currentList.removeAt(0)
            profiles.value = currentList
        }
    }

    fun onMatchAnimationFinished() {
        showMatchAnimation.value = false
    }
}