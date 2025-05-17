package com.example.tfg.views

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.tfg.R
import kotlin.random.Random

/**
 * Datos necesarios para representar un icono animado cayendo en la pantalla.
 */
data class FallingIcon(var x: Float, val speed: Float, var y: Float, val rotation: Float, val scale: Float)

/**
 * Pantalla principal del juego que actúa como menú de inicio.
 */
@SuppressLint("RememberReturnType")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    musicEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit
) {
    val configuracion = LocalConfiguration.current
    val totalHeight = configuracion.screenHeightDp
    val totalWidth = configuracion.screenWidthDp
    val density = LocalDensity.current
    val widthPx = with(density) { totalWidth.dp.toPx() }
    val heightPx = with(density) { totalHeight.dp.toPx() }

    val fuentePrincipal = FontFamily(Font(R.font.barriecito_regular))

    // Colores del juego
    val backgroundGreen = Color(0xFFc1ff72)
    val buttonGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF759E73)

    // Estados para diálogos
    val showOptionsDialog = remember { mutableStateOf(false) }
    val showPlayerDialog = remember { mutableStateOf(false) }

    // Estados para selección de jugadores
    val playersCount = remember { mutableStateOf(2) }
    val playerNames = remember { mutableStateListOf("", "", "", "") }

    // Animaciones título, iconos y botones (omitidas en este bloque para brevedad)
    val titleScale = remember { Animatable(0.9f) }
    LaunchedEffect(Unit) {
        while (true) {
            titleScale.animateTo(1.01f, tween(700, easing = FastOutSlowInEasing))
            titleScale.animateTo(1f, tween(700, easing = FastOutSlowInEasing))
        }
    }
    val icons = remember {
        mutableStateOf(List(80) {
            FallingIcon(
                x = Random.nextFloat() * widthPx,
                speed = Random.nextFloat() * 3f + 1f,
                y = Random.nextFloat() * -heightPx,
                rotation = Random.nextFloat() * 360f,
                scale = Random.nextFloat() * 0.5f + 0.5f
            )
        })
    }
    LaunchedEffect(Unit) {
        while (true) {
            icons.value = icons.value.map { icon ->
                val newY = icon.y + icon.speed
                if (newY > heightPx * 1.2f) {
                    icon.copy(
                        y = Random.nextFloat() * -heightPx,
                        x = Random.nextFloat() * widthPx,
                        rotation = Random.nextFloat() * 360f
                    )
                } else icon.copy(y = newY)
            }
            kotlinx.coroutines.delay(16L)
        }
    }
    val buttonScale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            buttonScale.animateTo(1.05f, tween(800, easing = FastOutSlowInEasing))
            buttonScale.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
        }
    }

    Box(modifier = modifier.fillMaxSize().background(backgroundGreen)) {
        // Fondo animado
        Canvas(modifier = Modifier.fillMaxSize()) {
            icons.value.forEach { icon ->
                drawContext.canvas.nativeCanvas.apply {
                    save(); translate(icon.x, icon.y); rotate(icon.rotation); scale(icon.scale, icon.scale)
                    drawText("$", 0f, 0f, android.graphics.Paint().apply {
                        textSize = 80f; color = android.graphics.Color.argb(100, 0, 100, 0); isFakeBoldText = true
                    })
                    restore()
                }
            }
        }
        // Contenido principal
        Column(modifier = Modifier.fillMaxSize()) {
            // Título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.heightPercentage(10f), start = Dimensions.widthPercentage(6f), end = Dimensions.widthPercentage(6f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(titleScale.value)
                        .border(1.dp, buttonGreen, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(buttonGreen)
                ) {
                    Text(
                        "FAMILY BUDGET",
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimensions.heightPercentage(4f)),
                        textAlign = TextAlign.Center,
                        fontFamily = fuentePrincipal,
                        fontSize = 50.sp
                    )
                }
            }
            Spacer(Modifier.height(Dimensions.heightPercentage(10f)))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón JUGAR
                Box(
                    modifier = Modifier
                        .padding(horizontal = Dimensions.widthPercentage(15f), vertical = Dimensions.heightPercentage(2f))
                        .fillMaxWidth()
                        .height(Dimensions.heightPercentage(8f))
                        .scale(buttonScale.value)
                        .border(1.dp, buttonGreen, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(buttonGreen)
                        .clickable { showPlayerDialog.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Black)
                        Spacer(Modifier.width(8.dp))
                        Text("JUGAR", color = Color.Black, fontFamily = fuentePrincipal, fontSize = 36.sp)
                    }
                }
                Spacer(Modifier.height(Dimensions.heightPercentage(4f)))
                // Botón OPCIONES
                Box(
                    modifier = Modifier
                        .padding(horizontal = Dimensions.widthPercentage(15f), vertical = Dimensions.heightPercentage(2f))
                        .fillMaxWidth()
                        .height(Dimensions.heightPercentage(8f))
                        .border(1.dp, buttonGreen, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(buttonGreen)
                        .clickable { showOptionsDialog.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.Black)
                        Spacer(Modifier.width(8.dp))
                        Text("OPCIONES", color = Color.Black, fontFamily = fuentePrincipal, fontSize = 36.sp)
                    }
                }

                // Monedas decorativas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimensions.heightPercentage(6f)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 1..3) {
                        val rotation = remember { Animatable(0f) }
                        LaunchedEffect(Unit) {
                            while (true) {
                                rotation.animateTo(
                                    targetValue = 360f,
                                    animationSpec = tween(2000, easing = LinearEasing)
                                )
                                rotation.snapTo(0f)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .rotate(rotation.value)
                                .background(darkGreen, CircleShape)
                                .border(2.dp, Color(0xFF63BB4C).copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$",
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                    }
                }
                }
        }

        // Diálogo de Opciones (igual que antes)
        if (showOptionsDialog.value) {
            Dialog(onDismissRequest = { showOptionsDialog.value = false }) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(buttonGreen)
                        .border(3.dp, darkGreen, RoundedCornerShape(20.dp)).padding(Dimensions.heightPercentage(3f))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("OPCIONES", fontSize = 36.sp, fontFamily = fuentePrincipal, color = Color.Black, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(20.dp)); Divider(color = darkGreen, thickness = 2.dp); Spacer(Modifier.height(20.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("🔊 Sonido:", fontSize = 24.sp, fontFamily = fuentePrincipal, color = Color.Black)
                            Switch(checked = musicEnabled, onCheckedChange = { onMusicToggle(!musicEnabled) }, colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = darkGreen
                            ))
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("🌐 Idioma: Español", fontSize = 24.sp, fontFamily = fuentePrincipal, color = Color.Black)
                            Text("(Próximamente...)", fontSize = 16.sp, fontFamily = fuentePrincipal, color = Color.Black.copy(alpha = 0.6f), modifier = Modifier.padding(start = 8.dp))
                        }
                        Spacer(Modifier.height(30.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(darkGreen).clickable { showOptionsDialog.value = false }.padding(horizontal = 40.dp, vertical = 12.dp)) {
                            Text("CERRAR", fontSize = 24.sp, fontFamily = fuentePrincipal, color = Color.White)
                        }
                    }
                }
            }
        }

        // Diálogo de Selección de Jugadores
        if (showPlayerDialog.value) {
            Dialog(onDismissRequest = { showPlayerDialog.value = false }) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(buttonGreen)
                        .border(3.dp, darkGreen, RoundedCornerShape(20.dp)).padding(Dimensions.heightPercentage(3f))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("JUGADORES", fontSize = 32.sp, fontFamily = fuentePrincipal, color = Color.Black)
                        Spacer(Modifier.height(16.dp))
                        // Selección de número de jugadores
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (2..4).forEach { num ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (playersCount.value == num) darkGreen else buttonGreen)
                                        .clickable { playersCount.value = num }
                                ) {
                                    Text(num.toString(), fontFamily = fuentePrincipal, fontSize = 20.sp, color = Color.Black)
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        // Campos de texto para nombres
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            (0 until playersCount.value).forEach { index ->
                                OutlinedTextField(
                                    value = playerNames[index],
                                    onValueChange = { playerNames[index] = it },
                                    label = { Text("Nombre Jugador ${index + 1}", fontFamily = fuentePrincipal) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        // Botón JUGAR dentro del diálogo
                        val allNamesFilled = playerNames.take(playersCount.value).all { it.isNotBlank() }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (allNamesFilled) darkGreen else darkGreen.copy(alpha = 0.5f))
                                .clickable(enabled = allNamesFilled) {
                                    showPlayerDialog.value = false
                                    // Navegar a la pantalla de juego pasando parámetros
                                    navController.navigate("pantalla_juego")
                                }
                                .padding(horizontal = 40.dp, vertical = 12.dp)
                        ) {
                            Text("JUGAR", fontSize = 24.sp, fontFamily = fuentePrincipal, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
