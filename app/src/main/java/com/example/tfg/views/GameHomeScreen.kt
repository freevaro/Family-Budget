package com.example.tfg.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday

import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tfg.R
import com.example.tfg.viewmodel.EstadoTurno
import com.example.tfg.viewmodel.EstadoTurno.diaId
import com.example.tfg.viewmodel.EstadoTurno.idJugador
import com.example.tfg.viewmodel.JugadorViewModel
import com.example.tfg.viewmodel.PartidaDatos.partidaId
import com.example.tfg.viewmodel.PartidaJugadorViewModel
import com.example.tfg.viewmodel.PositionsViewModel
import com.example.tfg.viewmodel.ShopViewModel
import com.example.tfg.viewmodel.TurnoManager
import com.example.tfg.viewmodel.TurnoManager.playerId

/**
 * Pantalla principal del juego donde se muestra la información del jugador actual,
 * estadísticas financieras y clasificación de los jugadores.
 *
 * También incluye el botón para finalizar turno y un diálogo de opciones.
 *
 * @param playerName Nombre del jugador activo.
 * @param currentDay Día actual del calendario.
 * @param totalDays Total de días del mes (no usado directamente, pero puede servir para lógica futura).
 * @param cash Dinero en efectivo actual del jugador.
 * @param passiveIncome Ingresos pasivos diarios.
 * @param dailyExpenses Gastos diarios.
 * @param onEndTurnClick Acción al pulsar "FINALIZAR TURNO".
 * @param onNavigateToHome Navegación a la pantalla principal.
 * @param onNavigateToBusiness Navegación a la pantalla de negocios.
 * @param onNavigateToCalendar Navegación a la pantalla de calendario.
 * @param onNavigateToShop Navegación a la tienda.
 * @param onNavigateToSettings Navegación a la configuración.
 * @param navController Controlador de navegación.
 * @param musicEnabled Estado actual de la música.
 * @param onMusicToggle Acción para activar/desactivar la música.
 * @param players Lista de jugadores y su posición.
 */

@Composable
fun GameHomeScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    navController: NavHostController,
    musicEnabled: Boolean,
    onMusicToggle: (Boolean)-> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val context = LocalContext.current
    val fuenteprincipal = FontFamily(
        Font(R.font.barriecito_regular)
    )
    val jugadoresViewModel : JugadorViewModel = viewModel()
    val jugadoresPPViewModel : PartidaJugadorViewModel = viewModel()
    val viewModel: PositionsViewModel = viewModel()
    val jugadores by viewModel.playersInGame.observeAsState(emptyList())

    val shopVM: ShopViewModel = viewModel()

    LaunchedEffect(partidaId) {
        viewModel.setPartidaId(partidaId)
    }

    val players = remember(jugadores) {
        jugadores
            .sortedByDescending { it.dinero }
            .mapIndexed { idx, jug ->
                Player(
                    name     = jug.nombre,
                    money    = jug.dinero.toInt(),
                    position = idx + 1
                )
            }
    }
    val diaNum by produceState(initialValue = 0) {
        snapshotFlow { EstadoTurno.diaNum }
            .collect { value = it }
    }
    val turnoName by produceState(initialValue = "") {
        snapshotFlow { EstadoTurno.nombre }
            .collect { value = it }
    }
    val efectivo by produceState(initialValue = 0) {
        snapshotFlow { EstadoTurno.dinero }
            .collect { value = it }
    }
    val ingresos by produceState(initialValue = 0) {
        snapshotFlow { EstadoTurno.ingresos }
            .collect { value = it }
    }
    val gastos by produceState(initialValue = 0) {
        snapshotFlow { EstadoTurno.costes }
            .collect { value = it }
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondo con patrón repetido desde un VectorDrawable
        PatternBackground()

        // Resto del contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.widthPercentage(4f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {


            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.heightPercentage(3f), top = Dimensions.heightPercentage(3f))
                    .offset(y = Dimensions.heightPercentage(1f)),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier.weight(0.3f),
                    colors = CardDefaults.cardColors(containerColor = darkGreen),
                    shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(Dimensions.widthPercentage(2f))
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = Color.White,
                            modifier = Modifier.size(Dimensions.widthPercentage(6f))
                        )
                        Text(
                            text = "Día $diaNum",
                            color = Color.White,
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(14f)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))


                Card(
                    modifier = Modifier.weight(0.7f),
                    colors = CardDefaults.cardColors(containerColor = lightGreen.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
                ) {
                    Text(
                        text = "Turno de $turnoName",
                        fontSize = Dimensions.responsiveSp(24f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = fuenteprincipal,
                        modifier = Modifier
                            .padding(vertical = Dimensions.heightPercentage(1.5f))
                            .fillMaxWidth()
                    )
                }


                Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))

                IconButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .padding(
                            top = Dimensions.heightPercentage(3f),
                            bottom = Dimensions.heightPercentage(3f),
                            start = Dimensions.widthPercentage(2f),
                            end = Dimensions.widthPercentage(3f)
                        )
                        .size(Dimensions.widthPercentage(7f))
                ){
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = ""
                    )
                }

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.heightPercentage(4f))
                    .offset(y = Dimensions.heightPercentage(1f)),
                colors = CardDefaults.cardColors(containerColor = lightGreen.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
            ) {
                Column(modifier = Modifier.padding(Dimensions.widthPercentage(4f))) {
                    FinancialStatRow(Icons.Default.AttachMoney, "Dinero en efectivo", "$$efectivo")
                    Divider(
                        color = darkGreen.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = Dimensions.heightPercentage(1f))
                    )
                    FinancialStatRow(Icons.Default.TrendingUp, "Ingresos pasivos", "$$ingresos")
                    Divider(
                        color = darkGreen.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = Dimensions.heightPercentage(1f))
                    )
                    FinancialStatRow(Icons.Default.TrendingDown, "Gasto diario", "$$gastos")
                }
            }

            val activity = LocalContext.current as ComponentActivity
            val shopVM: ShopViewModel = viewModel(
                viewModelStoreOwner = activity
            )


            Button(
                onClick = { TurnoManager.next()
                    shopVM.generarTiendaNueva(idJugador, diaId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.heightPercentage(9f))
                    .offset(y = Dimensions.heightPercentage(1f)),
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
            ) {
                Text(
                    "FINALIZAR TURNO",
                    fontSize = Dimensions.responsiveSp(18f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal
                )
            }

            Spacer(modifier = Modifier.height(Dimensions.heightPercentage(4.7f)))

            // Sección de posiciones de jugadores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.heightPercentage(2f)),
                colors = CardDefaults.cardColors(containerColor = lightGreen.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
            ) {
                Column(
                    modifier = Modifier.padding(
                        vertical = Dimensions.heightPercentage(1.5f),
                        horizontal = Dimensions.widthPercentage(4f)
                    )
                ) {
                    Text(
                        text = "POSICIONES",
                        fontSize = Dimensions.responsiveSp(16f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = fuenteprincipal,
                        color = darkGreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimensions.heightPercentage(1f)),
                        textAlign = TextAlign.Center
                    )

                    players.sortedByDescending { it.money }.forEachIndexed { index, player ->
                        PlayerPositionRow(
                            position = index + 1,
                            player = player,
                            isCurrentPlayer = player.name == turnoName,
                            fuenteprincipal = fuenteprincipal,
                            darkGreen = darkGreen
                        )

                        if (index < players.size - 1) {
                            Divider(
                                color = darkGreen.copy(alpha = 0.2f),
                                modifier = Modifier.padding(vertical = Dimensions.heightPercentage(0.5f))
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimensions.widthPercentage(5f)))
                    .background(Color(0xFF9CCD5C))
                    .padding(Dimensions.widthPercentage(6f))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Opciones",
                        fontSize = Dimensions.responsiveSp(30f),
                        fontFamily = fuenteprincipal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2.5f)))

                    // 🔊 Switch para el sonido
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🔊 Sonido:",
                            fontSize = Dimensions.responsiveSp(20f),
                            fontFamily = fuenteprincipal,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(Dimensions.widthPercentage(2.5f)))
                        androidx.compose.material3.Switch(
                            checked = musicEnabled,
                            onCheckedChange = { checked ->
                                onMusicToggle(!musicEnabled)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(1.2f)))

                    Text(
                        "🌐 Idioma: Español (Proximamente...)",
                        fontSize = Dimensions.responsiveSp(20f),
                        fontFamily = fuenteprincipal,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(3.7f)))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimensions.widthPercentage(3f)))
                            .background(Color(0xFF759E73))
                            .clickable {
                                showDialog.value = false
                            }
                            .padding(
                                horizontal = Dimensions.widthPercentage(5f),
                                vertical = Dimensions.heightPercentage(1.2f)
                            )
                    ) {
                        Text(
                            "Cerrar",
                            fontSize = Dimensions.responsiveSp(18f),
                            fontFamily = fuenteprincipal,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fondo de pantalla con degradado vertical.
 */

@Composable
fun PatternBackground() {
    Box(modifier = Modifier
        .fillMaxSize()
        // degradado de fondo
        .background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFF6B9A2F), Color(0xFF9CCD5C))
            )
        )
    )
}

/**
 * Fila reutilizable que muestra una estadística financiera con icono, etiqueta y valor.
 *
 * @param icon Icono representativo de la métrica.
 * @param label Texto descriptivo (por ejemplo, "Ingresos pasivos").
 * @param value Valor asociado (por ejemplo, "$80").
 */

@Composable
fun FinancialStatRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    val fuenteprincipal = FontFamily(
        Font(R.font.barriecito_regular)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(0.5f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6B9A2F),
            modifier = Modifier.size(Dimensions.widthPercentage(6f))
        )

        Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))

        Text(
            text = label,
            fontSize = Dimensions.responsiveSp(16f),
            fontFamily = fuenteprincipal,
            color = Color(0xFF6B9A2F),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = Dimensions.responsiveSp(18f),
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            color = Color(0xFF6B9A2F)
        )
    }
}

data class Player(
    val name: String,
    val money: Int,
    val position: Int
)

/**
 * Representa la información de un jugador en la tabla de posiciones.
 *
 * @param position Posición actual del jugador.
 * @param player Datos del jugador (nombre, dinero, posición).
 * @param isCurrentPlayer Indica si este jugador es el actual.
 * @param fuenteprincipal Fuente personalizada usada para el texto.
 * @param darkGreen Color principal para resaltar al jugador actual.
 */

@Composable
fun PlayerPositionRow(
    position: Int,
    player: Player,
    isCurrentPlayer: Boolean,
    fuenteprincipal: FontFamily,
    darkGreen: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(0.5f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Posición
        Text(
            text = "$position.",
            fontSize = Dimensions.responsiveSp(14f),
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            color = if (isCurrentPlayer) darkGreen else darkGreen.copy(alpha = 0.7f),
            modifier = Modifier.width(Dimensions.widthPercentage(6f))
        )

        // Nombre del jugador
        Text(
            text = player.name,
            fontSize = Dimensions.responsiveSp(14f),
            fontWeight = if (isCurrentPlayer) FontWeight.Bold else FontWeight.Normal,
            fontFamily = fuenteprincipal,
            color = if (isCurrentPlayer) darkGreen else darkGreen.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )

        // Dinero del jugador
        Text(
            text = "$${player.money}",
            fontSize = Dimensions.responsiveSp(14f),
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            color = if (isCurrentPlayer) darkGreen else darkGreen.copy(alpha = 0.7f)
        )
    }
}
