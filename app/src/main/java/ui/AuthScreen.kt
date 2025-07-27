package com.carlos.movilwordkapp.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme // <-- ESTA ES LA LÍNEA QUE FALTABA
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Conecta2", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // Botón de Iniciar Sesión
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            showToast(context, "Por favor, completa todos los campos.")
                            return@Button
                        }
                        isLoading = true
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showToast(context, "¡Bienvenido de nuevo!")
                                    navController.navigate("main_screen") {
                                        popUpTo(0) // Limpia todo el historial
                                    }
                                } else {
                                    showToast(context, "Error al iniciar sesión: ${task.exception?.message}")
                                }
                                isLoading = false
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de Registrarse
                OutlinedButton(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            showToast(context, "Por favor, completa todos los campos.")
                            return@OutlinedButton
                        }
                        isLoading = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showToast(context, "¡Registro exitoso!")
                                    navController.navigate("create_profile") {
                                        popUpTo("auth_screen") { inclusive = true }
                                    }
                                } else {
                                    showToast(context, "Error en el registro: ${task.exception?.message}")
                                }
                                isLoading = false
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }
        }
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}