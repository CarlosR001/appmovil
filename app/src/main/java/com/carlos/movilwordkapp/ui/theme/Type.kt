package com.carlos.movilwordkapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.carlos.movilwordkapp.R

// Reemplaza 'montserrat' con el nombre de tu fuente si elegiste otra
val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Montserrat, // Usamos nuestra fuente
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Montserrat, // Usamos nuestra fuente
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    )
    /* Puedes definir otros estilos aquí si los necesitas */
)