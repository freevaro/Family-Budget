package com.example.tfg.views

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.R


@Composable
fun CalendarScreen(
    currentDay: Int = 10,
    totalDays: Int = 31,
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))

    // Generar días del mes
    val days = (1..totalDays).toList()

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
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = "CALENDARIO",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteprincipal,
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Navegación del mes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Mes anterior */ }) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Mes anterior",
                        tint = Color.White
                    )
                }

                Text(
                    text = "MES 1",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal
                )

                IconButton(onClick = { /* Mes siguiente */ }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Mes siguiente",
                        tint = Color.White
                    )
                }
            }

            // Días de la semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                    Text(
                        text = day,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fuenteprincipal,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Calendario con peso para que no empuje el Card fuera de pantalla
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp)
            ) {
                // Espacios vacíos para alinear el primer día
                items(2) {
                    Box(modifier = Modifier.size(48.dp))
                }

                // Días del mes
                items(days) { day ->
                    CalendarDay(
                        day = day,
                        isCurrentDay = day == currentDay,
                        fuenteprincipal = fuenteprincipal,
                        darkGreen = darkGreen,
                        lightGreen = lightGreen
                    )
                }
            }

            // Información del día actual
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 38.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Día $currentDay - Turno X",
                        fontFamily = fuenteprincipal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = darkGreen,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Resumen Actual:",
                        fontFamily = fuenteprincipal,
                        fontSize = 18.sp,
                        color = darkGreen,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Negocios",
                            tint = darkGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = " Negocios: 3",
                            fontFamily = fuenteprincipal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Ingresos",
                            tint = darkGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = " Ingresos totales: $250/día",
                            fontFamily = fuenteprincipal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = "Gastos diarios",
                            tint = darkGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = " Gastos diarios: $80/día",
                            fontFamily = fuenteprincipal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDay(
    day: Int,
    isCurrentDay: Boolean,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (isCurrentDay) darkGreen else Color.Transparent
            )
            .border(
                width = if (isCurrentDay) 0.dp else 1.dp,
                color = if (isCurrentDay) Color.Transparent else Color.White.copy(alpha = 0.5f)
            )
            .clickable { /* Lógica para seleccionar día */ }
    ) {
        Text(
            text = day.toString(),
            color = if (isCurrentDay) Color.White else Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            fontFamily = fuenteprincipal,
            textAlign = TextAlign.Center
        )
    }
}
