package com.example.tfg.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tfg.R
import com.example.tfg.dao.InventarioNegocioWithNegocio
import com.example.tfg.viewmodel.EstadoTurno.inventarioId
import com.example.tfg.viewmodel.InventarioComidaViewModel
import com.example.tfg.viewmodel.InventarioNegocioViewModel
import com.example.tfg.viewmodel.InventarioTarjetaViewModel
import com.example.tfg.viewmodel.NegocioViewModel
import kotlinx.coroutines.flow.drop

/**
 * Pantalla de configuración e inventario del juego.
 *
 * Muestra un resumen agrupado del inventario del jugador (negocios, comidas, tarjetas),
 * junto con los elementos en una cuadrícula categorizada.
 * También integra navegación entre pantallas del juego.
 *
 * @param onNavigateToHome Navegación a la pantalla de inicio del juego.
 * @param onNavigateToBusiness Navegación a la pantalla de negocios.
 * @param onNavigateToCalendar Navegación a la pantalla de calendario.
 * @param onNavigateToShop Navegación a la pantalla de tienda.
 * @param onNavigateToSettings Navegación a la propia pantalla de ajustes.
 * @param navController Controlador de navegación.
 * @param musicEnabled Estado actual de la música.
 * @param onMusicToggle Callback para activar o desactivar la música.
 */

@Composable
fun SettingsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    navController : NavHostController,
    musicEnabled: Boolean,
    onMusicToggle: (Boolean)-> Unit
) {
    // Colores y fuente
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen    = Color(0xFF6B9A2F)
    val lightGreen   = Color(0xFFB5E878)
    val fuente       = FontFamily(Font(R.font.barriecito_regular))
    var selectedItem by remember { mutableStateOf<InventarioNegocioWithNegocio?>(null) }


    // ViewModels
    val invNegVM: InventarioNegocioViewModel = viewModel()
    val invFoodVM: InventarioComidaViewModel = viewModel()
    val invCardVM: InventarioTarjetaViewModel = viewModel()
    val negocioVM: NegocioViewModel           = viewModel()
    val comidaVM: InventarioComidaViewModel   = viewModel()
    val tarjetaVM: InventarioTarjetaViewModel = viewModel()



    val invNegList by remember {
        invNegVM.itemsFor(inventarioId)
            .drop(1)     // <— descartamos el primer emptyList() para evitar parpadeos
    }.collectAsState(initial = emptyList())

    LaunchedEffect(inventarioId) {
        invNegVM.refreshAll(inventarioId)
        invFoodVM.refreshAll(inventarioId)
        invCardVM.refreshAll(inventarioId)
    }
    val comidaItems by comidaVM.allItems.collectAsState(initial = emptyList())
    val tarjetaItems by tarjetaVM.allItems.collectAsState(initial = emptyList())



    val inventoryItems = invNegList.map { withNeg ->
        InventoryItem(
            name     = withNeg.negocio.nombre,
            quantity = withNeg.invNegocio.cantidad,
            icon     = iconFromString(withNeg.negocio.icon),
            category = "Negocio"
        )
    }

    // Totales
    val comidaCount  = comidaItems.size
    val tarjetaCount = tarjetaItems.size
    val negocioCount = inventoryItems.sumOf { it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(darkGreen, primaryGreen))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector   = Icons.Default.Inventory,
                    contentDescription = "Inventario",
                    tint          = Color.Black,
                    modifier      = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text      = "INVENTARIO",
                    fontSize  = 28.sp,
                    fontWeight= FontWeight.Bold,
                    fontFamily= fuente,
                    color     = Color.Black
                )
            }

            // Resumen estadístico
            Card(
                modifier    = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors      = CardDefaults.cardColors(containerColor = lightGreen.copy(alpha = 0.7f)),
                shape       = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "Resumen de Inventario",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fuente,
                        color      = darkGreen,
                        textAlign  = TextAlign.Center,
                        modifier   = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InventoryStat(Icons.Default.Business,    "Negocios", negocioCount, fuente, darkGreen)
                        InventoryStat(Icons.Default.Fastfood,     "Comidas",  comidaCount,  fuente, darkGreen)
                        InventoryStat(Icons.Default.CardGiftcard, "Tarjetas", tarjetaCount, fuente, darkGreen)
                    }
                }
            }

            // Grid con los items mapeados
            LazyVerticalGrid(
                columns             = GridCells.Fixed(3),
                modifier            = Modifier.height(260.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(inventoryItems) { item ->
                    val negocioWithDetails = invNegList.find { it.negocio.nombre == item.name }
                    InventoryItemCardNegocio(
                        item = item,
                        onClick = { negocioWithDetails?.let { selectedItem = it } }
                    )
                }
            }
        }

        // Modal de detalles del negocio
        selectedItem?.let { negocioWithDetails ->
            val negocio = negocioWithDetails.negocio
            AlertDialog(
                onDismissRequest = { selectedItem = null },
                containerColor = lightGreen.copy(alpha = 0.9f),
                shape = RoundedCornerShape(12.dp),
                titleContentColor = Color.Black,
                textContentColor = Color.Black,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val icon = iconFromString(negocio.icon)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(darkGreen.copy(alpha = 0.7f))
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = negocio.nombre,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = negocio.nombre,
                            fontFamily = fuente,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        DetalleNegocioItemInventario(
                            icon = Icons.Default.TrendingUp,
                            texto = "Ingresos diarios:",
                            valor = "$${negocio.ingresos.toInt()}/día",
                            fuente = fuente
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DetalleNegocioItemInventario(
                            icon = Icons.Default.Store,
                            texto = "Coste tienda:",
                            valor = "$${negocio.costeTienda.toInt()}",
                            fuente = fuente
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DetalleNegocioItemInventario(
                            icon = Icons.Default.Build,
                            texto = "Coste mantenimiento:",
                            valor = "$${negocio.costeMantenimiento.toInt()}",
                            fuente = fuente
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DetalleNegocioItemInventario(
                            icon = Icons.Default.Category,
                            texto = "Categoría:",
                            valor = negocio.categoria,
                            fuente = fuente
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DetalleNegocioItemInventario(
                            icon = Icons.Default.Numbers,
                            texto = "Cantidad:",
                            valor = negocioWithDetails.invNegocio.cantidad.toString(),
                            fuente = fuente
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedItem = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "CERRAR",
                            fontFamily = fuente,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun InventoryStat(
    icon: ImageVector,
    label: String,
    value: Int,
    fuente: FontFamily,
    darkGreen: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = darkGreen, modifier = Modifier.size(24.dp))
        Text(label, fontFamily = fuente, fontSize = 14.sp, color = darkGreen)
        Text(value.toString(), fontFamily = fuente, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = darkGreen)
    }
}

@Composable
fun InventoryItemCardNegocio(
    item: InventoryItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val fuente = FontFamily(Font(R.font.barriecito_regular))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        // Icono circular del negocio
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(lightGreen.copy(alpha = 0.7f))
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.name,
                tint = darkGreen,
                modifier = Modifier.size(40.dp)
            )
        }

        // Nombre del negocio
        Text(
            text = item.name,
            fontFamily = fuente,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Cantidad (si es mayor que 1)
        if (item.quantity > 1) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.quantity.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGreen
                )
            }
        }
    }
}


data class InventoryItem(
    val name: String,
    val quantity: Int,
    val icon: ImageVector,
    val category: String
)

@Composable
fun DetalleNegocioItemInventario(
    icon: ImageVector,
    texto: String,
    valor: String,
    fuente: FontFamily
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            fontFamily = fuente,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            fontFamily = fuente,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}