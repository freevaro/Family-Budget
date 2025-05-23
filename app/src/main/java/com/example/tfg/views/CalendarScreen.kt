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
import com.example.tfg.R


/**
 * Pantalla de calendario que muestra los días del mes, el día actual y un resumen de actividad.
 *
 * Representa un calendario mensual con una cuadrícula de días, y un resumen inferior del día actual.
 * Se puede navegar entre meses (aunque la lógica de navegación aún no está implementada).
 *
 * @param currentDay Día actual seleccionado (por defecto, 10).
 * @param totalDays Número total de días del mes (por defecto, 31).
 * @param onNavigateToHome Acción al pulsar el botón "Inicio".
 * @param onNavigateToBusiness Acción al pulsar "Negocios".
 * @param onNavigateToCalendar Acción al pulsar "Calendario".
 * @param onNavigateToShop Acción al pulsar "Tienda".
 * @param onNavigateToSettings Acción al pulsar "Opciones".
 * @param modifier Modificador para aplicar al contenedor principal.
 */

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
                .padding(Dimensions.widthPercentage(4f))
        ) {
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
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendario",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(10f))
                        .padding(end = Dimensions.widthPercentage(2f))
                )
                Text(
                    text = "CALENDARIO",
                    color = Color.Black,
                    fontSize = Dimensions.responsiveSp(28f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal
                )
            }

            // Días de la semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.heightPercentage(1f), top = Dimensions.widthPercentage(7f)),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                    Text(
                        text = day,
                        color = Color.White,
                        fontSize = Dimensions.responsiveSp(16f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = fuenteprincipal,
                        modifier = Modifier.width(Dimensions.widthPercentage(8f)),
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
                    .padding(bottom = Dimensions.heightPercentage(1f))
            ) {
                // Espacios vacíos para alinear el primer día
                items(2) {
                    Box(modifier = Modifier.size(Dimensions.widthPercentage(12f)))
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
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.heightPercentage(4.5f)),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.widthPercentage(4f))
                ) {
                    Text(
                        text = "Día $currentDay - Turno X",
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(20f),
                        fontWeight = FontWeight.Bold,
                        color = darkGreen,
                        modifier = Modifier.padding(bottom = Dimensions.heightPercentage(2f))
                    )

                    Text(
                        text = "Resumen Actual:",
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(18f),
                        color = darkGreen,
                        modifier = Modifier.padding(bottom = Dimensions.heightPercentage(1f))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Dinero",
                            tint = darkGreen,
                            modifier = Modifier.size(Dimensions.widthPercentage(5f))
                        )
                        Text(
                            text = " Dinero: $1000",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(16f),
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Negocios",
                            tint = darkGreen,
                            modifier = Modifier.size(Dimensions.widthPercentage(5f))
                        )
                        Text(
                            text = " Negocios: 3",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(16f),
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
                            modifier = Modifier.size(Dimensions.widthPercentage(5f))
                        )
                        Text(
                            text = " Ingresos totales: $250/día",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(16f),
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
                            modifier = Modifier.size(Dimensions.widthPercentage(5f))
                        )
                        Text(
                            text = " Gastos diarios: $80/día",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(16f),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

/**
 * Representa un día individual en el calendario.
 *
 * El día actual se destaca con un fondo verde oscuro. Al hacer clic se podría implementar lógica adicional.
 *
 * @param day Día del mes.
 * @param isCurrentDay Indica si es el día actual.
 * @param fuenteprincipal Fuente personalizada del texto.
 * @param darkGreen Color del día seleccionado.
 * @param lightGreen Color base del calendario.
 */

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
            .padding(Dimensions.widthPercentage(1f))
            .size(Dimensions.widthPercentage(10f))
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
            fontSize = Dimensions.responsiveSp(16f),
            fontFamily = fuenteprincipal,
            textAlign = TextAlign.Center
        )
    }
}
