package com.carlos.movilwordkapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.carlos.movilwordkapp.model.UserProfile
import com.carlos.movilwordkapp.viewmodel.HomeViewModel

@Composable
fun ActivitiesScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("MATCHES", "LE GUSTAS", "VISITANTES")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color.White
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        // Contenido que cambia según la pestaña seleccionada
        when (selectedTabIndex) {
            0 -> MatchesContent(homeViewModel, navController)
            1 -> LikesYouContent()
            2 -> VisitorsContent()
        }
    }
}

@Composable
fun MatchesContent(homeViewModel: HomeViewModel, navController: NavController) {
    val matches by homeViewModel.matches.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (matches.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no tienes matches. ¡Sigue deslizando!", color = Color.White.copy(alpha = 0.7f))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(matches) { profile ->
                    MatchCard(profile, navController)
                }
            }
        }
    }
}

@Composable
fun LikesYouContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Aquí aparecerán las personas a las que les gustas.\n(Función Premium Próximamente)", color = Color.White, textAlign = TextAlign.Center)
    }
}

@Composable
fun VisitorsContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Aquí aparecerán las personas que han visitado tu perfil.\n(Función Premium Próximamente)", color = Color.White, textAlign = TextAlign.Center)
    }
}

@Composable
fun MatchCard(profile: UserProfile, navController: NavController) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { navController.navigate("chat") },
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = rememberAsyncImagePainter(profile.imageUrls.firstOrNull()),
            contentDescription = "Foto de ${profile.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.BottomStart) {
            Text(profile.name, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}