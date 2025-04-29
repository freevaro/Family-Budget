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
import androidx.compose.ui.unit.sp
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = "FAMILY BUDGET",
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal,
                    modifier = Modifier.padding(top = 60.dp, bottom = 24.dp, start = 99.dp)
                )

                IconButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier.padding(top = 63.dp, bottom = 24.dp, start = 63.dp).size(27.dp)
                ){
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = ""
                )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .offset(y = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.weight(0.7f),
                    colors = CardDefaults.cardColors(containerColor = lightGreen.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Turno de "+playerName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = fuenteprincipal,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Card(
                    modifier = Modifier.weight(0.3f),
                    colors = CardDefaults.cardColors(containerColor = darkGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Día $currentDay",
                            color = Color.White,
                            fontFamily = fuenteprincipal,
                            fontSize = 14.sp
                        )

                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .offset(y = 10.dp),
                colors = CardDefaults.cardColors(containerColor = lightGreen.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FinancialStatRow(Icons.Default.AttachMoney, "Dinero en efectivo", "$$cash")
                    Divider(color = darkGreen.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
                    FinancialStatRow(Icons.Default.TrendingUp, "Ingresos pasivos", "$$passiveIncome")
                    Divider(color = darkGreen.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
                    FinancialStatRow(Icons.Default.TrendingDown, "Gasto diario", "$$dailyExpenses")
                }
            }


            Button(
                onClick = onEndTurnClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .offset(y = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("FINALIZAR TURNO", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = fuenteprincipal)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

    }
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF9CCD5C))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Opciones",
                        fontSize = 30.sp,
                        fontFamily = fuenteprincipal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔊 Switch para el sonido
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🔊 Sonido:",
                            fontSize = 20.sp,
                            fontFamily = fuenteprincipal,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        androidx.compose.material3.Switch(
                            checked = musicEnabled,
                            onCheckedChange = { checked ->
                                onMusicToggle(!musicEnabled)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("🌐 Idioma: Español (Proximamente...)",
                        fontSize = 20.sp,
                        fontFamily = fuenteprincipal,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF759E73))
                            .clickable {
                                showDialog.value = false
                            }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            "Cerrar",
                            fontSize = 18.sp,
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
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6B9A2F),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            fontFamily = fuenteprincipal,
            color = Color(0xFF6B9A2F),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            color = Color(0xFF6B9A2F)
        )
    }
}
