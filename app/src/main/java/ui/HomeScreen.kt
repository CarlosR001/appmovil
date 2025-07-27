package com.carlos.movilwordkapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carlos.movilwordkapp.R
import com.carlos.movilwordkapp.ui.components.FilterBottomSheet
import com.carlos.movilwordkapp.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val profiles = homeViewModel.profiles.value
    val isLoading by homeViewModel.isLoading
    val filterOptions = listOf("Cerca de ti", "Online", "Objetivo Común", "Verificados")
    var showFilterSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.main_screen_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.FlashOn, "Popularidad")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Default.FilterList, "Filtros")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(filterOptions) { option ->
                    FilterChip(selected = false, onClick = { /* TODO */ }, label = { Text(option) })
                }
            }

            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (profiles.isNotEmpty()) {
                    val profile = profiles.first()
                    key(profile.id) {
                        ProfileCard(
                            userProfile = profile,
                            onDismiss = { direction -> homeViewModel.dismissProfile(direction) }
                        )
                    }
                } else {
                    Text("No hay perfiles que coincidan con tu búsqueda.", color = Color.White)
                }
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                onDismiss = { showFilterSheet = false },
                onApplyFilters = { ageRange, gender ->
                    scope.launch {
                        homeViewModel.fetchProfiles(
                            minAge = ageRange.start.toInt(),
                            maxAge = ageRange.endInclusive.toInt()
                        )
                        showFilterSheet = false
                    }
                }
            )
        }
    }
}