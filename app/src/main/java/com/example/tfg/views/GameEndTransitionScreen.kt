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
 * Pantalla de transición que se muestra al final de la partida
 * Muestra "FIN DE LA PARTIDA" y "TENEMOS UN GANADOR"
 * Se muestra durante 5 segundos antes de navegar a la pantalla de resultados finales
 *
 * @param navController Controlador de navegación
 * @param onTransitionComplete Callback que se ejecuta al completar la transición
 */
@Composable
fun GameEndTransitionScreen(
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
        // Animación de entrada más lenta y dramática
        launch {
            scale.animateTo(
                targetValue = 1.3f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }

        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        }

        // Esperar 5 segundos (mucho más tiempo para que se vea bien)
        delay(5000)

        // Animación de salida más lenta
        launch {
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }

        launch {
            scale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }

        // Esperar a que termine la animación de salida
        delay(600)

        // Navegar a la pantalla de resultados finales
        navController.navigate("pantalla_resultados_finales") {
            popUpTo("game_end_transition") { inclusive = true }
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
                text = "FIN DE LA PARTIDA",
                fontSize = 40.sp, // Aumentado el tamaño
                fontFamily = fuentePrincipal,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = alpha.value),
                textAlign = TextAlign.Center,
                lineHeight = 44.sp
            )

            Spacer(modifier = Modifier.height(32.dp)) // Más espacio

            Text(
                text = "TENEMOS UN GANADOR",
                fontSize = 24.sp, // Aumentado el tamaño
                fontFamily = fuentePrincipal,
                fontWeight = FontWeight.Normal,
                color = textColor.copy(alpha = alpha.value * 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
