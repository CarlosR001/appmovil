package com.carlos.movilwordkapp.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.carlos.movilwordkapp.model.UserProfile
import com.carlos.movilwordkapp.ui.components.ProfileStatCard
import com.carlos.movilwordkapp.ui.theme.AccentPink
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(mainNavController: NavController, bottomBarNavController: NavController) {
    // Estado para guardar los datos del perfil cargado
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val context = LocalContext.current

    // Efecto para cargar los datos del perfil desde Firestore cuando la pantalla se abre
    LaunchedEffect(Unit) {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            Firebase.firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userProfile = UserProfile(
                            id = document.id,
                            name = document.getString("name") ?: "",
                            age = document.getLong("age")?.toInt() ?: 0,
                            bio = document.getString("bio") ?: "",
                            imageUrls = (document.get("imageUrls") as? List<String>) ?: emptyList()
                        )
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "No se pudo cargar tu perfil.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { bottomBarNavController.navigate("edit_profile") }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        // Si el perfil aún no ha cargado, muestra un indicador de progreso
        if (userProfile == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Cuando el perfil ya cargó, muestra la información
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(userProfile?.imageUrls?.firstOrNull()),
                    contentDescription = "Mi foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${userProfile?.name}, ${userProfile?.age}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Perfil Verificado",
                        tint = AccentPink,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStatCard(value = "1,280", label = "Me Gustas", icon = Icons.Default.FavoriteBorder, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfileStatCard(value = "85%", label = "Perfil Completo", icon = Icons.Default.AccountCircle, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfileStatCard(value = "7.8/10", label = "Popularidad", icon = Icons.Default.SignalCellularAlt, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

                // Botón para Cerrar Sesión
                Button(
                    onClick = {
                        Firebase.auth.signOut()
                        mainNavController.navigate("auth_screen") {
                            popUpTo(0) // Limpia todo el historial de navegación
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}