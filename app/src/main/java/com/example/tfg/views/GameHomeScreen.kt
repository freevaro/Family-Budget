package com.example.tfg.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import com.example.tfg.R

@Composable
fun GameHomeScreen(
    playerName: String = "David",
    currentDay: Int = 7,
    totalDays: Int = 30,
    cash: Int = 600,
    passiveIncome: Int = 80,
    dailyExpenses: Int = 50,
    onEndTurnClick: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    navController: NavHostController,
    musicEnabled: Boolean,
    onMusicToggle: (Boolean)-> Unit,
    players: List<Player> = listOf(
        Player("David", 600, 1),
        Player("Ana", 550, 2),
        Player("Carlos", 500, 3),
        Player("Elena", 450, 4)
    )
) {
    val showDialog = remember { mutableStateOf(false) }
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val context = LocalContext.current
    val fuenteprincipal = FontFamily(
        Font(R.font.barriecito_regular)
    )

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
                            text = "Día $currentDay",
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
                        text = "Turno de "+playerName,
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
                    FinancialStatRow(Icons.Default.AttachMoney, "Dinero en efectivo", "$$cash")
                    Divider(
                        color = darkGreen.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = Dimensions.heightPercentage(1f))
                    )
                    FinancialStatRow(Icons.Default.TrendingUp, "Ingresos pasivos", "$$passiveIncome")
                    Divider(
                        color = darkGreen.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = Dimensions.heightPercentage(1f))
                    )
                    FinancialStatRow(Icons.Default.TrendingDown, "Gasto diario", "$$dailyExpenses")
                }
            }

            Button(
                onClick = onEndTurnClick,
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
                            isCurrentPlayer = player.name == playerName,
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
