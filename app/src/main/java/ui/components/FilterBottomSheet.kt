package com.carlos.movilwordkapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (ClosedFloatingPointRange<Float>, String) -> Unit // Usamos el tipo explícito
) {
    val sheetState = rememberModalBottomSheetState()
    var ageRange by remember { mutableStateOf(18f..40f) }
    var selectedGender by remember { mutableStateOf("Todos") }
    val genderOptions = listOf("Todos", "Hombres", "Mujeres")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Filtros", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Edad: ${ageRange.start.toInt()} - ${ageRange.endInclusive.toInt()}")
            RangeSlider(
                value = ageRange,
                onValueChange = { ageRange = it },
                valueRange = 18f..99f,
                steps = 80
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Mostrar")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                genderOptions.forEach { gender ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (gender == selectedGender),
                            onClick = { selectedGender = gender }
                        )
                        Text(text = gender)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onApplyFilters(ageRange, selectedGender) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio extra al final
        }
    }
}