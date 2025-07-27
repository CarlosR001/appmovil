package com.carlos.movilwordkapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlos.movilwordkapp.ui.AuthScreen
import com.carlos.movilwordkapp.ui.CreateProfileScreen
import com.carlos.movilwordkapp.ui.MainScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val auth = Firebase.auth

    // Decidimos la pantalla inicial basándonos en si el usuario ya ha iniciado sesión
    val startDestination = if (auth.currentUser != null) {
        "main_screen" // Si ya hay sesión, vamos directo a la app
    } else {
        "auth_screen" // Si no, a la pantalla de login/registro
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Nueva ruta de autenticación que reemplaza a la de registro
        composable("auth_screen") {
            AuthScreen(navController = navController)
        }

        composable("create_profile") {
            CreateProfileScreen(navController = navController)
        }

        composable("main_screen") {
            MainScreen(navController = navController)
        }
    }
}