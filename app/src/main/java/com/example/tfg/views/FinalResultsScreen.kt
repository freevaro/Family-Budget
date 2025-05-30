package com.example.tfg.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tfg.entity.Jugador
import com.example.tfg.entity.ResumenDia
import com.example.tfg.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalResultsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val jugadorViewModel: JugadorViewModel = viewModel()
    val resumenDiaViewModel: ResumenDiaViewModel = viewModel()
    
    // Estado para mostrar el detalle del jugador seleccionado
    var selectedPlayer by remember { mutableStateOf<Jugador?>(null) }
    var selectedPlayerSummary by remember { mutableStateOf<ResumenDia?>(null) }
    
    // Obtener todos los jugadores y ordenarlos por dinero (descendente)
    val allPlayers by jugadorViewModel.allJugadores.observeAsState(emptyList())
    val sortedPlayers = allPlayers.sortedByDescending { it.dinero }
    
    // Colores para las posiciones
    val positionColors = listOf(
        Color(0xFFFFD700), // Oro
        Color(0xFFC0C0C0), // Plata
        Color(0xFFCD7F32), // Bronce
        Color(0xFF8B4513)  // Cuarto lugar
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF6B9A2F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "🏆 RESULTADOS FINALES 🏆",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Subtítulo
            Text(
                text = "Día 31 - Fin de la partida",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Lista de jugadores
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(sortedPlayers) { index, player ->
                    PlayerResultCard(
                        player = player,
                        position = index + 1,
                        positionColor = positionColors.getOrElse(index) { Color.Gray },
                        onClick = { 
                            selectedPlayer = player
                            // Cargar resumen del día 31 para este jugador
                            resumenDiaViewModel.getResumen(31, player.id).observeForever { summary ->
                                selectedPlayerSummary = summary
                            }
                        }
                    )
                }
            }
            
            // Botón para volver al menú principal
            Button(
                onClick = { 
                    // Resetear el juego y volver al menú principal
                    navController.navigate("pantalla_principal") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A7C59)
                )
            ) {
                Text(
                    text = "Volver al Menú Principal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        
        // Modal para mostrar detalles del jugador
        selectedPlayer?.let { player ->
            PlayerDetailModal(
                player = player,
                summary = selectedPlayerSummary,
                onDismiss = { 
                    selectedPlayer = null
                    selectedPlayerSummary = null
                }
            )
        }
    }
}

@Composable
fun PlayerResultCard(
    player: Jugador,
    position: Int,
    positionColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posición
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(positionColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$position°",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del jugador
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "Dinero: $${String.format("%.0f", player.dinero)}",
                    fontSize = 16.sp,
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = "Ingresos: $${String.format("%.0f", player.ingresos)} | Gastos: $${String.format("%.0f", player.gastos)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            // Indicador de que es clickeable
            Text(
                text = "👆",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun PlayerDetailModal(
    player: Jugador,
    summary: ResumenDia?,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable { }, // Evita que se cierre al hacer click en el contenido
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "📊 Resumen del Día 31",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = player.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color.Gray.copy(alpha = 0.3f)
                )
                
                // Datos del resumen
                summary?.let { s ->
                    DetailRow("💰 Dinero Final", "$${s.dinero}")
                    DetailRow("🏢 Negocios Totales", "${s.negocios}")
                    DetailRow("📈 Ingresos", "$${s.ingresos}")
                    DetailRow("📉 Gastos", "$${s.gastos}")
                    DetailRow("🔄 Turnos Jugados", "${s.turno}")
                } ?: run {
                    Text(
                        text = "No hay datos disponibles para el día 31",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
                
                // Botón cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B9A2F)
                    )
                ) {
                    Text(
                        text = "Cerrar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF424242)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B5E20)
        )
    }
}