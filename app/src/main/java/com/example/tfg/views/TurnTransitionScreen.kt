package com.example.tfg.views

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tfg.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla de transición que muestra el turno del jugador actual
 * Se muestra durante 1 segundo antes de navegar a la pantalla de juego
 *
 * @param playerName Nombre del jugador cuyo turno va a comenzar
 * @param navController Controlador de navegación
 * @param onTransitionComplete Callback que se ejecuta al completar la transición
 */
@Composable
fun TurnTransitionScreen(
    playerName: String,
    navController: NavController,
    onTransitionComplete: () -> Unit = {}
) {
    val fuentePrincipal = FontFamily(Font(R.font.barriecito_regular))

    // Colores del juego
    val backgroundGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFF9CCD5C)
    val textColor = Color.Black

    // Animaciones
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    // Efecto de entrada y salida
    LaunchedEffect(Unit) {
        // Animación de entrada
        launch {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(200, easing = FastOutSlowInEasing)
            )
        }

        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        }

        // Esperar 1 segundo
        delay(1000)

        // Animación de salida
        launch {
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(200, easing = FastOutSlowInEasing)
            )
        }

        launch {
            scale.animateTo(
                targetValue = 0.8f,
                animationSpec = tween(200, easing = FastOutSlowInEasing)
            )
        }

        // Esperar a que termine la animación de salida
        delay(200)

        // Navegar a la pantalla de juego
        navController.navigate("pantalla_juego") {
            popUpTo("turn_transition") { inclusive = true }
        }

        onTransitionComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(backgroundGreen, lightGreen)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "TURNO DE",
                fontSize = 32.sp,
                fontFamily = fuentePrincipal,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = alpha.value),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = playerName.uppercase(),
                fontSize = 48.sp,
                fontFamily = fuentePrincipal,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = alpha.value),
                textAlign = TextAlign.Center,
                lineHeight = 52.sp
            )
        }
    }
}