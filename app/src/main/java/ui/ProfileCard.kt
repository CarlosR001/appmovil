package com.carlos.movilwordkapp.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.carlos.movilwordkapp.model.UserProfile
import com.carlos.movilwordkapp.viewmodel.SwipeDirection
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ProfileCard(userProfile: UserProfile, onDismiss: (SwipeDirection) -> Unit, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var currentImageIndex by remember { mutableStateOf(0) }

    fun animateDismiss(direction: SwipeDirection) {
        coroutineScope.launch {
            val target = if (direction == SwipeDirection.RIGHT) 1000f else -1000f
            // --- CAMBIO DE VELOCIDAD AQUÍ ---
            offsetX.animateTo(target, animationSpec = tween(durationMillis = 200)) // ANTES: 300
            onDismiss(direction)
        }
    }

    val combinedModifier = modifier
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch { offsetX.snapTo(offsetX.value + dragAmount.x) }
                },
                onDragEnd = {
                    coroutineScope.launch {
                        when {
                            offsetX.value > 300 -> animateDismiss(SwipeDirection.RIGHT)
                            offsetX.value < -300 -> animateDismiss(SwipeDirection.LEFT)
                            // --- CAMBIO DE VELOCIDAD AQUÍ ---
                            else -> offsetX.animateTo(0f, animationSpec = tween(durationMillis = 200)) // ANTES: 300
                        }
                    }
                }
            )
        }
        .graphicsLayer { rotationZ = (offsetX.value / 60).coerceIn(-15f, 15f) }

    Box(modifier = combinedModifier.fillMaxWidth().height(600.dp).clip(RoundedCornerShape(24.dp))) {
        Image(
            painter = rememberAsyncImagePainter(userProfile.imageUrls[currentImageIndex]),
            contentDescription = "Foto de ${userProfile.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxHeight().weight(1f).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                if (currentImageIndex > 0) { currentImageIndex-- }
            })
            Box(modifier = Modifier.fillMaxHeight().weight(1f).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                if (currentImageIndex < userProfile.imageUrls.size - 1) { currentImageIndex++ }
            })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            userProfile.imageUrls.forEachIndexed { index, _ ->
                Box(modifier = Modifier.weight(1f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(if (index == currentImageIndex) Color.White else Color.White.copy(alpha = 0.5f)))
            }
        }
        Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            if (offsetX.value > 0) {
                Text("LIKE", color = Color(0xFF00C853).copy(alpha = (offsetX.value / 300).coerceIn(0f, 1f)), fontSize = 48.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopStart).rotate(-20f))
            } else if (offsetX.value < 0) {
                Text("NOPE", color = Color(0xFFD50000).copy(alpha = (abs(offsetX.value) / 300).coerceIn(0f, 1f)), fontSize = 48.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopEnd).rotate(20f))
            }
        }
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f).align(Alignment.BottomCenter).background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart).padding(24.dp)) {
            Text(text = "${userProfile.name}, ${userProfile.age}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = userProfile.bio, color = Color.White, fontSize = 16.sp, maxLines = 3, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { animateDismiss(SwipeDirection.LEFT) }, modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                    Icon(Icons.Default.Close, "Pasar", tint = Color.White, modifier = Modifier.size(40.dp))
                }
                IconButton(onClick = { animateDismiss(SwipeDirection.RIGHT) }, modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                    Icon(Icons.Default.Favorite, "Me gusta", tint = Color(0xFFE91E63), modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}