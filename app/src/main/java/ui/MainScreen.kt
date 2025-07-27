package com.carlos.movilwordkapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carlos.movilwordkapp.viewmodel.HomeViewModel

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Search, "Explorar")
    object Activities : BottomNavItem("activities", Icons.Default.Favorite, "Actividades")
    object Profile : BottomNavItem("profile", Icons.Default.AccountCircle, "Perfil")
}

@Composable
fun MainScreen(navController: NavController) {
    val bottomBarNavController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Activities,
        BottomNavItem.Profile,
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            bottomBarNavController.navigate(screen.route) {
                                popUpTo(bottomBarNavController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(bottomBarNavController, startDestination = BottomNavItem.Home.route, Modifier.padding(innerPadding)) {
            composable(BottomNavItem.Home.route) { HomeScreen(homeViewModel = homeViewModel) }
            composable(BottomNavItem.Activities.route) { ActivitiesScreen(homeViewModel = homeViewModel, navController = bottomBarNavController) }

            composable(BottomNavItem.Profile.route) {
                MyProfileScreen(
                    mainNavController = navController,
                    bottomBarNavController = bottomBarNavController
                )
            }

            composable("chat") { ChatScreen(navController = bottomBarNavController) }

            composable("edit_profile") { EditProfileScreen(navController = bottomBarNavController) }
        }
    }
}