package com.carlos.movilwordkapp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser

    // Efecto para cargar los datos del perfil una sola vez
    LaunchedEffect(key1 = currentUser?.uid) {
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("name") ?: ""
                        age = document.getLong("age")?.toString() ?: ""
                        bio = document.getString("bio") ?: ""
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al cargar el perfil.", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tu nombre") })
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Tu edad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Tu biografía") },
                    modifier = Modifier.height(120.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    val ageAsInt = age.toIntOrNull()
                    if (ageAsInt == null) {
                        Toast.makeText(context, "Por favor, introduce una edad válida.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val updatedData = mapOf(
                        "name" to name,
                        "age" to ageAsInt,
                        "bio" to bio
                    )

                    currentUser?.uid?.let {
                        db.collection("users").document(it)
                            .update(updatedData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Perfil actualizado.", Toast.LENGTH_SHORT).show()
                                navController.popBackStack() // Volver a la pantalla anterior
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}