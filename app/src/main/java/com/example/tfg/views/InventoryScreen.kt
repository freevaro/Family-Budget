package com.example.tfg.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.tfg.dao.InventarioComidaWithComida
import com.example.tfg.dao.InventarioNegocioWithNegocio
import com.example.tfg.dao.InventarioTarjetaWithTarjeta
import com.example.tfg.entity.Comida
import com.example.tfg.entity.Tarjeta
import com.example.tfg.viewmodel.ComidaViewModel
import com.example.tfg.viewmodel.EstadoTurno
import com.example.tfg.viewmodel.EstadoTurno.inventarioId
import com.example.tfg.viewmodel.InventarioComidaViewModel
import com.example.tfg.viewmodel.InventarioNegocioViewModel
import com.example.tfg.viewmodel.InventarioTarjetaViewModel
import com.example.tfg.viewmodel.JugadorEfectoViewModel
import com.example.tfg.viewmodel.NegocioViewModel
import com.example.tfg.viewmodel.TarjetaViewModel
import com.example.tfg.viewmodel.TurnoManager.aplicarEfectosNegocioActivos
import com.example.tfg.views.Count.comidaCount
import com.example.tfg.views.Count.negocioCount
import com.example.tfg.views.Count.tarjetaCount
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

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

object Count{
    var comidaCount = 0
    var negocioCount = 0
    var tarjetaCount = 0
}


@SuppressLint("FlowOperatorInvokedInComposition")
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
    var selectedNegocio by remember { mutableStateOf<InventarioNegocioWithNegocio?>(null) }
    var selectedComida by remember { mutableStateOf<InventarioComidaWithComida?>(null) }
    var selectedTarjeta by remember { mutableStateOf<InventarioTarjetaWithTarjeta?>(null) }



    // ViewModels
    val invNegVM: InventarioNegocioViewModel      = viewModel()
    val invComidaVM: InventarioComidaViewModel    = viewModel()
    val invTarjetaVM: InventarioTarjetaViewModel  = viewModel()
    val jugadorEfectoVM : JugadorEfectoViewModel    = viewModel()
    val negocioVM: NegocioViewModel           = viewModel()
    val comidaVM: InventarioComidaViewModel   = viewModel()
    val tarjetaVM: InventarioTarjetaViewModel = viewModel()
    val comidaEntityVM: ComidaViewModel           = viewModel()
    val tarjetaEntityVM: TarjetaViewModel         = viewModel()

    val currentPlayerId = EstadoTurno.idJugador
    val efectosPorJugador by jugadorEfectoVM
        .efectosPorJugador(currentPlayerId)  // ✔️ ahora sí filtramos por jugador
        .drop(1)   // descartamos el emptyList() inicial
        .collectAsState(initial = emptyList())



    val invNegList by remember {
        invNegVM.itemsFor(inventarioId)
            .drop(1)     // <— descartamos el primer emptyList() para evitar parpadeos
    }.collectAsState(initial = emptyList())

    val invComList by remember {
        invComidaVM.itemsFor(inventarioId)
            .drop(1)     // <— descartamos el primer emptyList() para evitar parpadeos
    }.collectAsState(initial = emptyList())

    val invTarjetaList by remember {
        invTarjetaVM.itemsFor(inventarioId)
            .drop(1)     // <— descartamos el primer emptyList() para evitar parpadeos
    }.collectAsState(initial = emptyList())


// Cargar datos
    LaunchedEffect(inventarioId) {
        invNegVM.refreshAll(inventarioId)
        invComidaVM.refreshAll(inventarioId)
        invTarjetaVM.refreshAll(inventarioId)
    }
    val allComidas       by comidaEntityVM.allComidas.observeAsState(emptyList())
    val allTarjetas      by tarjetaEntityVM.allTarjetas.observeAsState(emptyList())



    // Mapear a items genéricos
    val negocioItems = invNegList.map { withNeg ->
        InventoryItem(
            name     = withNeg.negocio.nombre,
            quantity = withNeg.invNegocio.cantidad,
            icon     = iconFromString(withNeg.negocio.icon),
            category = "Negocio"
        )
    }
    // Mapear a items genéricos
    val comidaItems = invComList.map { withCom ->
        InventoryItem(
            name     = withCom.comida.nombre,
            quantity = withCom.invComida.cantidad,
            icon     = when (withCom.comida.nombre) {
                "Comida Diaria"  -> Icons.Default.Fastfood
                "Comida Semanal" -> Icons.Default.Restaurant
                "Comida Premium" -> Icons.Default.RestaurantMenu
                else             -> Icons.Default.Fastfood
            },
            category = "Negocio"
        )
    }
    // Mapear a items genéricos
    val tarjetaItems = invTarjetaList.map { withTar ->
        InventoryItem(
            name     = withTar.tarjeta.nombre,
            quantity = withTar.invTarjeta.cantidad,
            icon     = when (withTar.tarjeta.nombre) {
                "Tarjeta Negocio"  -> Icons.Default.CardGiftcard
                "Tarjeta Dinero" -> Icons.Default.CardGiftcard
                "Tarjeta Aleatoria" -> Icons.Default.QuestionMark
                else             -> Icons.Default.CardGiftcard
            },
            category = "Negocio"
        )
    }
    val inventoryItems = negocioItems + comidaItems + tarjetaItems

    // Totales
    comidaCount  = comidaItems.size
    tarjetaCount = tarjetaItems.size
    negocioCount = negocioItems.size

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
                    .padding(vertical = 60.dp),
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
                        color      = Color(0xFF4f7123),
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

            // Sección Negocios
            CategorySection(
                title = "NEGOCIOS",
                fuenteprincipal = fuente,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (invNegList.isEmpty()){
                    item(span = { GridItemSpan(3) }) {
                        EmptySectionMessage(
                            icon = Icons.Default.Info,
                            message = "En este momento no tienes adquirido ningún negocio. Si quieres adquirir alguno, prueba en la tienda.",
                            color = Color.White,
                            fuente = fuente
                        )
                    }
                }else{
                    items(invNegList) { withNeg ->
                        val item = InventoryItem(withNeg.negocio.nombre, withNeg.invNegocio.cantidad, iconFromString(withNeg.negocio.icon), "Negocio")
                        InventoryItemCardNegocio(item) { selectedNegocio = withNeg }
                    }
                }
            }

            // Sección Comidas
            CategorySection(
                title = "COMIDAS",
                fuenteprincipal = fuente,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (invComList.isEmpty()){
                    item(span = { GridItemSpan(3) }) {
                        EmptySectionMessage(
                            icon = Icons.Default.Info,
                            message = "En este momento no tienes adquirida ninguna comida. Si quieres adquirir alguna, prueba en la tienda.",
                            color = Color.White,
                            fuente = fuente
                        )
                    }
                }else{
                    items(invComList) { withCom ->
                        val icon = when (withCom.comida.nombre) {
                            "Comida Diaria" -> Icons.Default.Fastfood
                            "Comida Semanal" -> Icons.Default.Restaurant
                            "Comida Premium" -> Icons.Default.RestaurantMenu
                            else -> Icons.Default.Fastfood
                        }
                        val item = InventoryItem(withCom.comida.nombre, withCom.invComida.cantidad, icon, "Comida")
                        InventoryItemCardNegocio(item) {
                            selectedComida = withCom  // guardamos el withCom completo
                        }                    }
                }
            }

            // Sección Tarjetas Bonus
            CategorySection(
                title = "TARJETAS BONUS",
                fuenteprincipal = fuente,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (invTarjetaList.isEmpty()){
                    item(span = { GridItemSpan(3) }) {
                        EmptySectionMessage(
                            icon = Icons.Default.Info,
                            message = "En este momento no tienes adquirida ninguna tarjeta. Si quieres adquirir alguna, prueba en la tienda.",
                            color = Color.White,
                            fuente = fuente
                        )
                    }
                }else{
                    items(invTarjetaList) { withTar ->
                        val icon = when (withTar.tarjeta.nombre) {
                            "Tarjeta Negocio", "Tarjeta Dinero" -> Icons.Default.CardGiftcard
                            "Tarjeta Aleatoria" -> Icons.Default.QuestionMark
                            else -> Icons.Default.CardGiftcard
                        }
                        val item = InventoryItem(withTar.tarjeta.nombre, withTar.invTarjeta.cantidad, icon, "Tarjeta")
                        InventoryItemCardNegocio(item) {
                            selectedTarjeta = withTar  // guardamos el withTar completo
                        }
                    }
                }
            }
        }

        // Modal de detalles del negocio
        selectedNegocio?.let { negocioWithDetails ->
            val negocio = negocioWithDetails.negocio
            AlertDialog(
                onDismissRequest = { selectedNegocio = null },
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
                        onClick = { selectedNegocio = null },
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
        selectedComida?.let { withCom ->

            val comidaEntity = withCom.comida
            val invCom       = withCom.invComida


            AlertDialog(
                onDismissRequest = { selectedComida = null },
                containerColor   = lightGreen.copy(alpha = 0.9f),
                shape            = RoundedCornerShape(12.dp),
                titleContentColor= Color.Black,
                textContentColor = Color.Black,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Icono circular
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(darkGreen.copy(alpha = 0.7f))
                        ) {
                            val icon = when (withCom.comida.nombre) {
                                "Comida Diaria"  -> Icons.Default.Fastfood
                                "Comida Semanal" -> Icons.Default.Restaurant
                                "Comida Premium" -> Icons.Default.RestaurantMenu
                                else             -> Icons.Default.Fastfood
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = withCom.comida.nombre,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text       = withCom.comida.nombre,
                            fontFamily = fuente,
                            fontSize   = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        DetalleComidaItem(
                            icon   = Icons.Default.Timer,
                            texto  = "Duración:",
                            valor  = "${withCom.comida.duracion} días",
                            fuentePrincipal = fuente
                        )
                        Spacer(Modifier.height(8.dp))
                        DetalleComidaItem(
                            icon   = Icons.Default.AttachMoney,
                            texto  = "Precio:",
                            valor  = "$${withCom.comida.precio}",
                            fuentePrincipal = fuente
                        )
                        Spacer(Modifier.height(8.dp))
                        DetalleComidaItem(
                            icon   = Icons.Default.TrendingUp,
                            texto  = "Efecto:",
                            valor  = "+${withCom.comida.efecto}",
                            fuentePrincipal = fuente
                        )
                        Spacer(Modifier.height(8.dp))
                        DetalleComidaItem(
                            icon   = Icons.Default.Timer,
                            texto  = "Duración restante:",
                            valor  = "${invCom.duracion} días",
                            fuentePrincipal = fuente
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick  = { selectedComida = null },
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = darkGreen,
                            contentColor   = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "CERRAR",
                            fontFamily = fuente,
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        // ——————————
// Modal Detalle de Tarjeta Bonus
// ——————————
        selectedTarjeta?.let { withTar ->

            val tarjetaEntity = withTar.tarjeta
            val cantidad      = withTar.invTarjeta.cantidad
            // Buscamos el efecto que tenga esta tarjeta y el jugador actual:
// DESPUÉS: filtramos todos los efectos de esa tarjeta y cogemos el que tenga la máxima duración
            val efectosMismaTarjeta = efectosPorJugador
                .filter { it.fkTarjeta == tarjetaEntity.id }
            // 1) Recoger la duración
            val duracion by remember(withTar.invTarjeta.id) {
                invTarjetaVM
                    .duracionInventarioTarjeta(withTar.invTarjeta.id)
            }.collectAsState(initial = 0)

            AlertDialog(
                onDismissRequest = { selectedTarjeta = null },
                containerColor   = lightGreen.copy(alpha = 0.9f),
                shape            = RoundedCornerShape(12.dp),
                titleContentColor= Color.Black,
                textContentColor = Color.Black,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(darkGreen.copy(alpha = 0.7f))
                        ) {
                            Icon(
                                imageVector    = Icons.Default.CardGiftcard,
                                contentDescription = withTar.tarjeta.nombre,
                                tint           = Color.White,
                                modifier       = Modifier.size(24.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text       = withTar.tarjeta.nombre,
                            fontFamily = fuente,
                            fontSize   = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    if (withTar.tarjeta.nombre == "Tarjeta Negocio" || withTar.tarjeta.nombre == "Tarjeta Dinero" || withTar.tarjeta.nombre == "Tarjeta Aleatoria"){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            DetalleTarjetaItem(
                                icon   = Icons.Default.Info,
                                texto  = "Efecto:",
                                valor  = tarjetaEntity.nombreEfecto,
                                fuentePrincipal = fuente
                            )
                            Spacer(Modifier.height(8.dp))
                            DetalleTarjetaItem(
                                icon   = Icons.Default.Numbers,
                                texto  = "Cantidad:",
                                valor  = cantidad.toString(),
                                fuentePrincipal = fuente
                            )

                        }
                    }else{
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Spacer(Modifier.height(8.dp))
                            var tipo = ""
                            if (withTar.tarjeta.tipoTarjeta == "negocio") {
                                tipo = "Negocio"
                            }else{
                                tipo = "Dinero"
                            }
                            DetalleTarjetaItem(
                                icon   = Icons.Default.Build,
                                texto  = "Tipo:",
                                valor  = tipo,
                                fuentePrincipal = fuente
                            )
                            Spacer(Modifier.height(8.dp))
                            DetalleTarjetaItem(
                                icon   = Icons.Default.Person,
                                texto  = "Dirigido a:",
                                valor  = withTar.tarjeta.dirigidoA,
                                fuentePrincipal = fuente
                            )
                            Spacer(Modifier.height(8.dp))
                            var afectado = ""
                            if (withTar.tarjeta.queModifica == "ingresos"){
                                afectado = "Ingresos:"
                            }else{
                                afectado = "Costes Diarios:"
                            }
                            if (withTar.tarjeta.tipoEfecto == "Positivo" && afectado == "Ingresos:"){
                                DetalleTarjetaItem(
                                    icon   = Icons.Default.TrendingUp,
                                    texto  = afectado,
                                    valor  = "+${withTar.tarjeta.efectoValor}%",
                                    fuentePrincipal = fuente
                                )
                            }else if(withTar.tarjeta.tipoEfecto == "Negativo" && afectado == "Ingresos:"){
                                DetalleTarjetaItem(
                                    icon   = Icons.Default.TrendingUp,
                                    texto  = afectado,
                                    valor  = "-${withTar.tarjeta.efectoValor}%",
                                    fuentePrincipal = fuente
                                )
                            }else if (withTar.tarjeta.tipoEfecto == "Positivo" && afectado == "Costes Diarios:"){
                                DetalleTarjetaItem(
                                    icon   = Icons.Default.TrendingUp,
                                    texto  = afectado,
                                    valor  = "-${withTar.tarjeta.efectoValor}%",
                                    fuentePrincipal = fuente
                                )
                            }else if(withTar.tarjeta.tipoEfecto == "Negativo" && afectado == "Costes Diarios:"){
                                DetalleTarjetaItem(
                                    icon   = Icons.Default.TrendingUp,
                                    texto  = afectado,
                                    valor  = "+${withTar.tarjeta.efectoValor}%",
                                    fuentePrincipal = fuente
                                )
                            }
                            if(tipo == "Dinero" && withTar.tarjeta.tipoEfecto == "Positivo") {
                                Spacer(Modifier.height(8.dp))
                                DetalleTarjetaItem(
                                    icon = Icons.Default.Timer,
                                    texto = "Duración restante:",
                                    valor  = "+$${withTar.tarjeta.efectoValor}",
                                    fuentePrincipal = fuente
                                )
                            }else if (tipo == "Dinero" && withTar.tarjeta.tipoEfecto == "Negativo"){
                                Spacer(Modifier.height(8.dp))
                                DetalleTarjetaItem(
                                    icon   = Icons.Default.TrendingUp,
                                    texto  = afectado,
                                    valor  = "-$${withTar.tarjeta.efectoValor}",
                                    fuentePrincipal = fuente
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            DetalleTarjetaItem(
                                icon   = Icons.Default.Timer,
                                texto  = "Duración restante:",
                                valor  = "$duracion turnos",           // mostramos la duración real
                                fuentePrincipal = fuente
                            )
                        }
                    }

                },
                confirmButton = {
                    val uiScope = rememberCoroutineScope()
                    if (withTar.tarjeta.nombre == "Tarjeta Negocio" || withTar.tarjeta.nombre == "Tarjeta Dinero" || withTar.tarjeta.nombre == "Tarjeta Aleatoria") {
                            Button(
                                onClick  = { selectedTarjeta = null },
                                colors   = ButtonDefaults.buttonColors(
                                    containerColor = darkGreen,
                                    contentColor   = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    "CERRAR",
                                    fontFamily = fuente,
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        Button(
                            onClick = {
                                uiScope.launch{
                                    jugadorEfectoVM.reemplazarTarjeta(withTar.tarjeta)
                                    aplicarEfectosNegocioActivos()
                                    selectedTarjeta = null
                                }
                                      },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = darkGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                "CANJEAR",
                                fontFamily = fuente,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                            Button(
                                onClick  = { selectedTarjeta = null },
                                colors   = ButtonDefaults.buttonColors(
                                    containerColor = darkGreen,
                                    contentColor   = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    "CERRAR",
                                    fontFamily = fuente,
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
        Icon(icon, label, tint = Color(0xFF4f7123), modifier = Modifier.size(24.dp))
        Text(label, fontFamily = fuente, fontSize = 14.sp, color = Color(0xFF4f7123))
        Text(value.toString(), fontFamily = fuente, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4f7123))
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

@Composable
fun EmptySectionMessage(
    icon: ImageVector,
    message: String,
    color: Color,
    fuente: FontFamily
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = Color.White,
                fontFamily = fuente,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}




