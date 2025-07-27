package com.carlos.movilwordkapp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CreateProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crea tu Perfil", style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tu nombre") }, enabled = !isLoading)
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Tu edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !isLoading
        )
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Tu biografía") },
            modifier = Modifier.height(120.dp),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                isLoading = true
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                // --- INICIO DE LA CORRECCIÓN ---
                val ageAsInt = age.toIntOrNull() // Convierte a Int o devuelve null si no es un número

                if (name.isBlank() || age.isBlank() || bio.isBlank()) {
                    Toast.makeText(context, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                    isLoading = false
                } else if (ageAsInt == null) {
                    Toast.makeText(context, "Por favor, introduce una edad válida", Toast.LENGTH_SHORT).show()
                    isLoading = false
                } else if (userId != null) {
                    val userProfile = hashMapOf(
                        "name" to name,
                        "age" to ageAsInt, // Usamos la edad ya convertida y validada
                        "bio" to bio,
                        "imageUrls" to listOf("https://picsum.photos/seed/$userId/400/600")
                    )

                    FirebaseFirestore.getInstance().collection("users").document(userId)
                        .set(userProfile)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Perfil guardado", Toast.LENGTH_SHORT).show()
                            navController.navigate("main_screen") {
                                popUpTo(0)
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                } else {
                    isLoading = false
                }
                // --- FIN DE LA CORRECCIÓN ---
            }) {
                Text("Guardar y Empezar")
            }
        }
    }
}