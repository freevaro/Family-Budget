package com.example.tfg.views

import BottomNavItem
import BottomNavigationBar
import com.example.tfg.*
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.room.util.copy
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.graphics.Canvas as AndroidCanvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

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
) {

    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val context = LocalContext.current
    if (!MainActivity.comprobanteMusica) {
        val mediaPlayer = remember {
            MediaPlayer.create(context, R.raw.background).apply {
                isLooping = true
                MainActivity.comprobanteMusica = true
                start()
            }
        }
    }
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
            Text(
                text = "FAMILY BUDGET",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteprincipal,
                modifier = Modifier.padding(top = 60.dp, bottom = 24.dp)
            )

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




@Preview(showBackground = true)
@Composable
fun GameHomeScreenPreview() {
    GameHomeScreen()
}
