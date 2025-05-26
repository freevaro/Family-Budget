package com.example.tfg.views

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.R
import com.example.tfg.viewmodel.EstadoTurno
import com.example.tfg.viewmodel.EstadoTurno.diaNum
import com.example.tfg.viewmodel.EstadoTurno.idJugador
import com.example.tfg.viewmodel.ResumenDiaViewModel
import com.example.tfg.viewmodel.TurnoManager


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
    val resumenDiaVM: ResumenDiaViewModel = viewModel()
    val currentDay = diaNum
    var selectedDay by remember { mutableStateOf(EstadoTurno.diaNum) }
    val resumen by resumenDiaVM
        .getResumen(selectedDay, idJugador)
        .observeAsState()


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

            val weekDays = listOf("L","M","X","J","V","S","D")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weekDays.forEach { wd ->
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = wd,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fuenteprincipal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(Modifier.height(Dimensions.heightPercentage(3f)))

            // 2) Grid de días
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),           // permite 5 filas en lugar de clipping
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(8.dp),  // menos espacio vertical
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(days) { day ->
                    CalendarDay(
                        day             = day,
                        isCurrentDay    = day == EstadoTurno.diaNum,
                        isSelected      = day == selectedDay,
                        fuenteprincipal = fuenteprincipal,
                        darkGreen       = darkGreen,
                        lightGreen      = lightGreen,
                        onClick         = { selectedDay = day },
                        modifier        = Modifier
                            .fillMaxWidth()             // ocupa todo el ancho de la columna
                            .aspectRatio(1f)            // fuerza ratio 1:1
                    )
                }
            }

            Spacer(Modifier.height(Dimensions.heightPercentage(3f)))


            if (selectedDay == EstadoTurno.diaNum) {
                // Día actual: mostramos aviso
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
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Día $selectedDay  –  Turno ${TurnoManager.turno}",
                            fontFamily  = fuenteprincipal,
                            fontSize    = 18.sp,
                            color = Color(0xFF4f7123),
                            fontWeight  = FontWeight.Bold
                        )
                        Text(
                            text = "El día actual no ha acabado y todavía no se ha generado un resumen.",
                            fontFamily = fuenteprincipal,
                            color = Color.Black,
                            fontSize   = 20.sp
                        )
                    }
                }
            } else if (selectedDay > EstadoTurno.diaNum){
                // Día posterior: mostramos aviso
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
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Día $selectedDay  –  Turno X",
                            fontFamily  = fuenteprincipal,
                            fontSize    = 18.sp,
                            fontWeight  = FontWeight.Bold,
                            color = Color(0xFF4f7123)
                        )
                        Text(
                            text = "El día seleccionado no ha ocurrido y todavía no se ha generado un resumen.",
                            fontFamily = fuenteprincipal,
                            color = Color.Black,
                            fontSize   = 20.sp
                        )
                    }
                }
            }else{
                // Información del día actual
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimensions.heightPercentage(3f)),
                    colors = CardDefaults.cardColors(
                        containerColor = lightGreen.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.widthPercentage(4f))
                    ) {
                        Text(
                            text = "Día $selectedDay - Turno " + resumen?.turno,
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(20f),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4f7123),
                            modifier = Modifier.padding(bottom = Dimensions.heightPercentage(1f))
                        )

                        Text(
                            text = "Resumen Actual:",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(18f),
                            color = Color(0xFF4f7123),
                            modifier = Modifier.padding(bottom = Dimensions.heightPercentage(1f))
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = "Dinero",
                                tint = Color(0xFF4f7123),
                                modifier = Modifier.size(Dimensions.widthPercentage(5f))
                            )
                            Text(
                                text = " Dinero: ${resumen?.dinero}",
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
                                tint = Color(0xFF4f7123),
                                modifier = Modifier.size(Dimensions.widthPercentage(5f))
                            )
                            Text(
                                text = " Negocios: ${resumen?.negocios}",
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
                                tint = Color(0xFF4f7123),
                                modifier = Modifier.size(Dimensions.widthPercentage(5f))
                            )
                            Text(
                                text = " Ingresos diarios: ${resumen?.ingresos}",
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
    isSelected: Boolean,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = when {
        isCurrentDay -> darkGreen
        isSelected   -> lightGreen.copy(alpha = 0.5f)
        else         -> Color.Transparent
    }
    val border = when {
        isCurrentDay -> null
        isSelected   -> BorderStroke(2.dp, darkGreen)
        else         -> null
    }

    Box(
        modifier = modifier
            .padding(2.dp)
            .clickable(onClick = onClick)
            .background(background, shape = CircleShape)
            .then(if (border != null) Modifier.border(border, CircleShape) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 18.sp,            // número más grande
            fontWeight = if (isCurrentDay) FontWeight.Bold else FontWeight.Medium,
            fontFamily = fuenteprincipal,
            color = if (isCurrentDay) Color.White else Color(0xFF354B18)
        )
    }
}

data class DiaCalendar(
    var dia : Int,
    var turno : Int,
    var dinero : Int,
    var negocios : Int,
    var ingresos : Int,
    var gastos : Int
)

object Resumen{
    var id : Long = 0
    var fk_jugador : Long = idJugador
    var numDia : Int = diaNum
    var dinero : Int = EstadoTurno.dinero
    var negocios : Int = Count.negocioCount
    var ingresos : Int = EstadoTurno.ingresos
    var gastos : Int = EstadoTurno.costes
    var turno : Int = TurnoManager.turno
}
