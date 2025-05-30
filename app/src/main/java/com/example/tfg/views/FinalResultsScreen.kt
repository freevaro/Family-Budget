package com.example.tfg.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.dao.PartidaDao
import com.example.tfg.entity.Jugador
import com.example.tfg.entity.ResumenDia
import com.example.tfg.viewmodel.*
import com.example.tfg.viewmodel.PartidaDatos.partidaId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalResultsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val jugadorViewModel: JugadorViewModel = viewModel()
    val resumenDiaViewModel: ResumenDiaViewModel = viewModel()

    // Colores consistentes con las otras pantallas
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val verdeclarotexto = Color(0xFF4f7123)

    // Fuente consistente
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))

    // Estado para mostrar el detalle del jugador seleccionado
    var selectedPlayer by remember { mutableStateOf<Jugador?>(null) }
    var selectedPlayerSummary by remember { mutableStateOf<ResumenDia?>(null) }

    // Obtener todos los jugadores y ordenarlos por dinero (descendente)
    val allPlayers by jugadorViewModel.getPlayersPartida(partidaId).observeAsState(emptyList())
    val sortedPlayers = allPlayers.sortedByDescending { it.dinero }

    // Colores para las posiciones (más consistentes con el tema)
    val positionColors = listOf(
        Color(0xFFFFD700), // Oro
        Color(0xFFC0C0C0), // Plata
        Color(0xFFCD7F32), // Bronce
        verdeclarotexto     // Cuarto lugar usando el color del tema
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(darkGreen, primaryGreen)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.widthPercentage(4f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con icono - estilo consistente
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimensions.heightPercentage(6f),
                        bottom = Dimensions.heightPercentage(3f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trofeo",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(10f))
                        .padding(end = Dimensions.widthPercentage(2f))
                )
                Text(
                    text = "RESULTADOS FINALES",
                    color = Color.Black,
                    fontSize = Dimensions.responsiveSp(28f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal
                )
            }

            // Subtítulo en card - estilo consistente
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.heightPercentage(3f)),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
            ) {
                Text(
                    text = "Día 31 - Fin de la partida",
                    fontSize = Dimensions.responsiveSp(18f),
                    fontFamily = fuenteprincipal,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(
                            vertical = Dimensions.heightPercentage(1.5f),
                            horizontal = Dimensions.widthPercentage(4f)
                        )
                        .fillMaxWidth()
                )
            }

            // Lista de jugadores con estilo consistente
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.heightPercentage(1.5f))
            ) {
                itemsIndexed(sortedPlayers) { index, player ->
                    PlayerResultCard(
                        player = player,
                        position = index + 1,
                        positionColor = positionColors.getOrElse(index) { verdeclarotexto },
                        fuenteprincipal = fuenteprincipal,
                        lightGreen = lightGreen,
                        darkGreen = darkGreen,
                        verdeclarotexto = verdeclarotexto
                    )
                }
            }

            // Botón para volver al menú principal - estilo consistente
            Button(
                onClick = {
                    // Resetear el juego y volver al menú principal
                    navController.navigate("pantalla_principal") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.heightPercentage(7f))
                    .padding(top = Dimensions.heightPercentage(2f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkGreen
                ),
                shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
            ) {
                Text(
                    text = "VOLVER AL MENÚ PRINCIPAL",
                    fontSize = Dimensions.responsiveSp(16f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal,
                    color = Color.White
                )
            }

            // Espacio para la navegación
            Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PlayerResultCard(
    player: Jugador,
    position: Int,
    positionColor: Color,
    fuenteprincipal: FontFamily,
    lightGreen: Color,
    darkGreen: Color,
    verdeclarotexto: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.widthPercentage(4f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posición con estilo circular consistente
            Box(
                modifier = Modifier
                    .size(Dimensions.widthPercentage(12f))
                    .clip(CircleShape)
                    .background(positionColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$position°",
                    fontSize = Dimensions.responsiveSp(18f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(Dimensions.widthPercentage(4f)))

            // Información del jugador con iconos
            Column(modifier = Modifier.weight(1f)) {
                // Nombre con icono
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Jugador",
                        tint = verdeclarotexto,
                        modifier = Modifier.size(Dimensions.widthPercentage(5f))
                    )
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(2f)))
                    Text(
                        text = player.nombre,
                        fontSize = Dimensions.responsiveSp(20f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = fuenteprincipal,
                        color = Color.Black
                    )
                }

                // Dinero con icono
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.3f))
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Dinero",
                        tint = verdeclarotexto,
                        modifier = Modifier.size(Dimensions.widthPercentage(4f))
                    )
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(1f)))
                    Text(
                        text = "Dinero: $${String.format("%.0f", player.dinero)}",
                        fontSize = Dimensions.responsiveSp(16f),
                        fontFamily = fuenteprincipal,
                        color = verdeclarotexto
                    )
                }

                // Ingresos y gastos con iconos
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Ingresos",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(Dimensions.widthPercentage(4f))
                    )
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(1f)))
                    Text(
                        text = "Ingresos: $${String.format("%.0f", player.ingresos)}",
                        fontSize = Dimensions.responsiveSp(14f),
                        fontFamily = fuenteprincipal,
                        color = verdeclarotexto.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))

                    Icon(
                        imageVector = Icons.Default.TrendingDown,
                        contentDescription = "Gastos",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(Dimensions.widthPercentage(4f))
                    )
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(1f)))
                    Text(
                        text = "Gastos: $${String.format("%.0f", player.gastos)}",
                        fontSize = Dimensions.responsiveSp(14f),
                        fontFamily = fuenteprincipal,
                        color = verdeclarotexto.copy(alpha = 0.8f)
                    )
                }
            }

            // Icono de posición especial para el ganador
            if (position == 1) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Ganador",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(Dimensions.widthPercentage(8f))
                )

            }
        }
    }
}