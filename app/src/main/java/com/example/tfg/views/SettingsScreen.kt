package com.example.tfg.views

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.navigation.NavHostController
import com.example.tfg.R

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
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))

    // Elementos de inventario de ejemplo
    val inventoryItems = remember {
        listOf(
            InventoryItem("Cafetería", 1, Icons.Default.LocalCafe, "Negocio"),
            InventoryItem("Restaurante", 2, Icons.Default.Restaurant, "Negocio"),
            InventoryItem("Tienda", 1, Icons.Default.Store, "Negocio"),
            InventoryItem("Comida Semanal", 3, Icons.Default.Fastfood, "Comida"),
            InventoryItem("Tarjeta Dinero", 2, Icons.Default.CreditCard, "Tarjeta"),
            InventoryItem("Tarjeta Negocio", 1, Icons.Default.CardGiftcard, "Tarjeta"),
            InventoryItem("Comida Premium", 1, Icons.Default.RestaurantMenu, "Comida"),
            InventoryItem("Supermercado", 1, Icons.Default.ShoppingCart, "Negocio"),
            InventoryItem("Tarjeta Aleatorio", 4, Icons.Default.Casino, "Tarjeta")
        )
    }

    // Agrupar elementos por categoría
    val groupedItems = inventoryItems.groupBy { it.category }

    Box(
        modifier = Modifier
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
                .verticalScroll(rememberScrollState())
        ) {
            // Título con icono de inventario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimensions.heightPercentage(7f),
                        bottom = Dimensions.heightPercentage(4f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Inventory,
                    contentDescription = "Inventario",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(10f))
                        .padding(end = Dimensions.widthPercentage(2f))
                )
                Text(
                    text = "INVENTARIO",
                    color = Color.Black,
                    fontSize = Dimensions.responsiveSp(28f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal
                )
            }

            // Resumen del inventario
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
                        text = "Resumen de Inventario",
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(18f),
                        fontWeight = FontWeight.Bold,
                        color = darkGreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimensions.heightPercentage(1f)),
                        textAlign = TextAlign.Center
                    )

                    // Estadísticas del inventario
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InventoryStat(
                            icon = Icons.Default.Business,
                            label = "Negocios",
                            value = groupedItems["Negocio"]?.sumOf { it.quantity } ?: 0,
                            fuenteprincipal = fuenteprincipal,
                            darkGreen = darkGreen
                        )

                        InventoryStat(
                            icon = Icons.Default.Fastfood,
                            label = "Comidas",
                            value = groupedItems["Comida"]?.sumOf { it.quantity } ?: 0,
                            fuenteprincipal = fuenteprincipal,
                            darkGreen = darkGreen
                        )

                        InventoryStat(
                            icon = Icons.Default.CardGiftcard,
                            label = "Tarjetas",
                            value = groupedItems["Tarjeta"]?.sumOf { it.quantity } ?: 0,
                            fuenteprincipal = fuenteprincipal,
                            darkGreen = darkGreen
                        )
                    }
                }
            }

            // Categorías de inventario - Aseguramos un orden específico
            val categoryOrder = listOf("Negocio", "Comida", "Tarjeta")

            categoryOrder.forEach { category ->
                // Solo procesamos la categoría si tiene elementos
                val items = groupedItems[category] ?: return@forEach

                // Título de la categoría
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimensions.heightPercentage(1f)),
                    colors = CardDefaults.cardColors(
                        containerColor = darkGreen
                    ),
                    shape = RoundedCornerShape(Dimensions.widthPercentage(2f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                vertical = Dimensions.heightPercentage(1f),
                                horizontal = Dimensions.widthPercentage(4f)
                            )
                    ) {
                        // Icono según la categoría
                        Icon(
                            imageVector = when(category) {
                                "Negocio" -> Icons.Default.Business
                                "Comida" -> Icons.Default.Fastfood
                                "Tarjeta" -> Icons.Default.CardGiftcard
                                else -> Icons.Default.Inventory
                            },
                            contentDescription = category,
                            tint = Color.White,
                            modifier = Modifier
                                .size(Dimensions.widthPercentage(6f))
                                .padding(end = Dimensions.widthPercentage(2f))
                        )

                        Text(
                            text = when(category) {
                                "Negocio" -> "NEGOCIOS"
                                "Comida" -> "COMIDAS"
                                "Tarjeta" -> "TARJETAS"
                                else -> category.uppercase()
                            },
                            color = Color.White,
                            fontSize = Dimensions.responsiveSp(18f),
                            fontWeight = FontWeight.Bold,
                            fontFamily = fuenteprincipal
                        )
                    }
                }

                // Grid de elementos de la categoría
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(bottom = Dimensions.heightPercentage(2f), top = Dimensions.heightPercentage(4f)),
                    modifier = Modifier
                        .height(
                            Dimensions.heightPercentage(
                                when {
                                    items.size <= 3 -> 20f
                                    items.size <= 6 -> 40f
                                    else -> 60f
                                }
                            )
                        )
                        .fillMaxWidth()
                ) {
                    items(items) { item ->
                        InventoryItemCard(
                            item = item,
                            fuenteprincipal = fuenteprincipal,
                            darkGreen = darkGreen,
                            lightGreen = lightGreen
                        )
                    }
                }
            }
        }
    }
}

/**
 * Muestra un bloque estadístico dentro del resumen de inventario.
 *
 * Incluye un icono, una etiqueta y un valor numérico.
 *
 * @param icon Icono representativo de la categoría.
 * @param label Etiqueta del tipo de ítem (por ejemplo: "Negocios").
 * @param value Cantidad total de ítems de ese tipo.
 * @param fuenteprincipal Fuente a utilizar.
 * @param darkGreen Color temático del texto e icono.
 */

@Composable
fun InventoryStat(
    icon: ImageVector,
    label: String,
    value: Int,
    fuenteprincipal: FontFamily,
    darkGreen: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = darkGreen,
            modifier = Modifier.size(Dimensions.widthPercentage(8f))
        )

        Text(
            text = label,
            fontFamily = fuenteprincipal,
            fontSize = Dimensions.responsiveSp(14f),
            color = darkGreen
        )

        Text(
            text = value.toString(),
            fontFamily = fuenteprincipal,
            fontSize = Dimensions.responsiveSp(18f),
            fontWeight = FontWeight.Bold,
            color = darkGreen
        )
    }
}

/**
 * Representa visualmente un ítem del inventario en una tarjeta con icono y contador.
 *
 * El contador aparece solo si hay más de una unidad del mismo ítem.
 *
 * @param item Objeto [InventoryItem] con los datos a mostrar.
 * @param fuenteprincipal Fuente tipográfica personalizada.
 * @param darkGreen Color del texto y borde.
 * @param lightGreen Color de fondo para la tarjeta del ítem.
 */

@Composable
fun InventoryItemCard(
    item: InventoryItem,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimensions.widthPercentage(2f))
            .fillMaxWidth()
    ) {
        // Icono con contador de cantidad
        Box {
            // Fondo del icono
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(Dimensions.widthPercentage(18f))
                    .clip(RoundedCornerShape(Dimensions.widthPercentage(4f)))
                    .background(lightGreen.copy(alpha = 0.7f))
                    .border(
                        width = Dimensions.widthPercentage(0.5f),
                        color = darkGreen.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
                    )
                    .clickable { /* Lógica para ver detalles */ }
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    tint = darkGreen,
                    modifier = Modifier.size(Dimensions.widthPercentage(10f))
                )
            }

            // Contador de cantidad
            if (item.quantity > 1) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(Dimensions.widthPercentage(7f))
                        .clip(RoundedCornerShape(50))
                        .background(darkGreen)
                        .border(
                            width = Dimensions.widthPercentage(0.3f),
                            color = Color.White,
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Text(
                        text = "x${item.quantity}",
                        color = Color.White,
                        fontSize = Dimensions.responsiveSp(12f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = fuenteprincipal
                    )
                }
            }
        }

        // Nombre del elemento
        Text(
            text = item.name,
            fontFamily = fuenteprincipal,
            fontSize = Dimensions.responsiveSp(12f),
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = Dimensions.heightPercentage(1f))
        )
    }
}

/**
 * Modelo de datos para representar un ítem del inventario del jugador.
 *
 * @param name Nombre del ítem.
 * @param quantity Cantidad en posesión.
 * @param icon Icono gráfico representativo.
 * @param category Categoría del ítem (por ejemplo: "Negocio", "Comida", "Tarjeta").
 */

data class InventoryItem(
    val name: String,
    val quantity: Int,
    val icon: ImageVector,
    val category: String
)
