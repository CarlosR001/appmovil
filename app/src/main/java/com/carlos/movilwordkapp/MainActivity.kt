package com.carlos.movilwordkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.carlos.movilwordkapp.ui.theme.MovilwordkappTheme
import com.carlos.movilwordkapp.ui.theme.PrimaryPurple
import com.carlos.movilwordkapp.ui.theme.AccentPink

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovilwordkappTheme {
                // Usamos un Box para aplicar el degradado a toda la pantalla
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    PrimaryPurple,
                                    AccentPink
                                )
                            )
                        )
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent // Hacemos el Scaffold transparente
                    ) { innerPadding ->
                        AppNavigation(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}