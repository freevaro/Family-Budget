package com.example.tfg.views

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.R
import com.example.tfg.entity.Comida
import com.example.tfg.entity.Negocio
import com.example.tfg.entity.Tarjeta
import com.example.tfg.viewmodel.ComidaViewModel
import com.example.tfg.viewmodel.InventarioComidaViewModel
import com.example.tfg.viewmodel.InventarioNegocioViewModel
import com.example.tfg.viewmodel.InventarioTarjetaViewModel
import com.example.tfg.viewmodel.JugadorViewModel
import com.example.tfg.viewmodel.NegocioViewModel
import com.example.tfg.viewmodel.ShopViewModel
import com.example.tfg.viewmodel.TarjetaViewModel
import com.example.tfg.viewmodel.TurnoManager
import com.example.tfg.viewmodel.TurnoManager.playerId
import com.example.tfg.viewmodel.TurnoManager.diaId
import com.example.tfg.viewmodel.TurnoManager.procesarIngresosYCostesDeNegocios
import com.example.tfg.viewmodel.TurnoManager.turno
import com.example.tfg.viewmodel.TurnoManager.ultimoTurnoGenerado
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin


/**
 * Pantalla de la tienda del juego.
 *
 * Permite al jugador visualizar productos categorizados como negocios, comidas y tarjetas bonus.
 * Los productos están organizados por secciones y subcategorías, cada uno con su nombre, icono, precio y botón de compra.
 *
 * @param onNavigateToHome Navega a la pantalla de inicio del juego.
 * @param onNavigateToBusiness Navega a la pantalla de negocios.
 * @param onNavigateToCalendar Navega a la pantalla del calendario.
 * @param onNavigateToShop Navega a esta misma pantalla (tienda).
 * @param onNavigateToSettings Navega a la pantalla de ajustes/inventario.
 */

@Composable
fun ShopScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    // Variables para el modal de confirmación de compra
    var showPurchaseModal by remember { mutableStateOf(false) }
    var purchasedProduct by remember { mutableStateOf<Triple<String, ImageVector, String>?>(null) }
    val activity = LocalContext.current as ComponentActivity
    val shopVM: ShopViewModel = viewModel(
        viewModelStoreOwner = activity
    )
    val descuento by shopVM.descuentoComida.observeAsState(0)

    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))

    var selectedNegocio by remember { mutableStateOf<Negocio?>(null) }
    var selectedComida  by remember { mutableStateOf<Comida?>(null) }
    var selectedTarjeta by remember { mutableStateOf<Tarjeta?>(null) }
    val invNegVM: InventarioNegocioViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()    // 📌
    val playerVM : JugadorViewModel = viewModel()







    // Guardamos el último turno para el que ya generamos la tienda

    LaunchedEffect(turno) {
        // Si nunca hemos generado para este turno (incluye el arranque, porque es null)
        if (ultimoTurnoGenerado != turno) {
            // Genera la tienda para el turno actual
            shopVM.generarTiendaNueva(playerId, diaId)
            // Marca que ya la hemos generado
            ultimoTurnoGenerado = turno
        }
    }

    val comidas by shopVM.comidas.observeAsState(emptyList())
    val negocios by shopVM.negociosEnTienda.observeAsState(emptyList())
    val negociosBajo = remember(negocios) {
        negocios.filter { it.categoria.equals("Baja", true) }.take(3)
    }
    val negociosMedio = remember(negocios) {
        negocios.filter { it.categoria.equals("Media", true) }.take(3)
    }
    val negociosAlto = remember(negocios) {
        negocios.filter { it.categoria.equals("Alta", true) }.take(3)
    }

    val tarjetas by shopVM.tarjetas.observeAsState(emptyList())

    val primerasTarjetas = remember(tarjetas) { tarjetas.take(3) }
    val tarjetasBonus = primerasTarjetas.map { tarjeta ->
        ProductTarjeta(
            name    = tarjeta.nombre,
            price   = shopVM.aplicarDescuento(tarjeta.efectoValor.toDouble(),descuento).toInt(),
            icon    = Icons.Default.CardGiftcard,
            onClick = { selectedTarjeta = tarjeta }
        )
    }

    var comidaIni = ComidaTienda("",0,0,0,Icons.Default.BuildCircle)

    var comidaDiaria : ComidaTienda = comidaIni
    var comidaSemanal : ComidaTienda = comidaIni
    var comidaPremium : ComidaTienda = comidaIni

    comidas.forEach { comida ->
        if (comida.nombre.equals("Comida Diaria")){
            comidaDiaria = ComidaTienda(comida.nombre,comida.duracion,comida.precio,comida.efecto,Icons.Default.Fastfood)
        }
        if (comida.nombre.equals("Comida Semanal")){
            comidaSemanal = ComidaTienda(comida.nombre,comida.duracion,comida.precio,comida.efecto,Icons.Default.Restaurant)
        }
        if (comida.nombre.equals("Comida Premium")){
            comidaPremium = ComidaTienda(comida.nombre,comida.duracion,comida.precio,comida.efecto,Icons.Default.RestaurantMenu)
        }

    }

    val invComidaVM: InventarioComidaViewModel    = viewModel()   // nuevo
    val invTarjetaVM: InventarioTarjetaViewModel  = viewModel()   // nuevo

    val primerasComidas = comidas.take(3)
    val tarjetasComida = primerasComidas.map { comida ->
        val precioConDesc = shopVM.aplicarDescuento(
            comida.precio.toDouble(), descuento
        ).toInt()

        val icon = when (comida.nombre) {
            "Comida Diaria"  -> Icons.Default.Fastfood
            "Comida Semanal" -> Icons.Default.Restaurant
            "Comida Premium" -> Icons.Default.RestaurantMenu
            else              -> Icons.Default.BuildCircle
        }
        ProductComida(
            name    = comida.nombre,
            price   = precioConDesc,
            icon    = icon,
            onClick = { selectedComida = comida }
        )
    }


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
            // Título
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
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shop",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(10f))
                        .padding(end = Dimensions.widthPercentage(2f))
                )
                Text(
                    text = "TIENDA" +
                            "",
                    color = Color.Black,
                    fontSize = Dimensions.responsiveSp(28f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuenteprincipal
                )
            }

            DiscountBanner(
                descuento = descuento,
                fuenteprincipal = fuenteprincipal
            )

            // Sección Negocios
            CategorySection(
                title = "NEGOCIOS",
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Subsección Bajo
            SubcategorySection(
                title = "Bajo",
                products = negociosBajo.map { negocio ->
                    Product(
                        name = negocio.nombre,
                        price = shopVM.aplicarDescuento(negocio.costeTienda,descuento),
                        icon  = iconFromString(negocio.icon),
                        onClick = { selectedNegocio = negocio }
                    )
                },
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen,

            )

            // Subsección Medio
            SubcategorySection(
                title = "Medio",
                products = negociosMedio.map { negocio ->
                    Product(
                        name = negocio.nombre,
                        price = shopVM.aplicarDescuento(negocio.costeTienda,descuento),
                        icon  = iconFromString(negocio.icon),
                        onClick = { selectedNegocio = negocio }
                    )
                },

                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Subsección Alto
            SubcategorySection(
                title = "Alto",
                products = negociosAlto.map { negocio ->
                    Product(
                        name = negocio.nombre,
                        price = shopVM.aplicarDescuento(negocio.costeTienda,descuento),
                        icon  = iconFromString(negocio.icon),
                        onClick = { selectedNegocio = negocio }
                    )
                },
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Sección Comidas
            CategorySection(
                title = "COMIDAS",
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen,
            )

            // Productos de comida
            ProductRowComidas(
                products = tarjetasComida,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Sección Tarjetas Bonus
            CategorySection(
                title = "TARJETAS BONUS",
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Productos de tarjetas bonus
            ProductRowTarjetas(
                products = tarjetasBonus,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Espacio para la barra de navegación
            Spacer(modifier = Modifier.height(Dimensions.heightPercentage(10f)))
        }
    }
// Diálogo con detalles del negocio
    selectedNegocio?.let { negocio ->

        AlertDialog(
            onDismissRequest = { selectedNegocio = null },
            containerColor = lightGreen.copy(alpha = 0.9f),
            shape = MaterialTheme.shapes.large,
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
                            .size(Dimensions.widthPercentage(12f))
                            .clip(CircleShape)
                            .background(darkGreen.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = negocio.nombre,
                            tint = Color.White,
                            modifier = Modifier.size(Dimensions.widthPercentage(6f))
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))
                    Text(
                        text = negocio.nombre,
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(24f),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.widthPercentage(2f))
                ) {
                    DetalleNegocioItem(
                        icon = Icons.Default.TrendingUp,
                        texto = "Ingresos diarios:",
                        valor = "$${negocio.ingresos.toInt()}/día",
                        fuentePrincipal = fuenteprincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleNegocioItem(
                        icon = Icons.Default.Store,
                        texto = "Coste tienda:",
                        valor = "$${negocio.costeTienda.toInt()}",
                        fuentePrincipal = fuenteprincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleNegocioItem(
                        icon = Icons.Default.Build,
                        texto = "Coste mantenimiento:",
                        valor = "$${negocio.costeMantenimiento.toInt()}",
                        fuentePrincipal = fuenteprincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleNegocioItem(
                        icon = Icons.Default.Category,
                        texto = "Categoría:",
                        valor = negocio.categoria,
                        fuentePrincipal = fuenteprincipal
                    )
                }
            },
            confirmButton = {
                val negocioVM : NegocioViewModel = viewModel()
                val uiScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        uiScope.launch {
                            negocio.costeTienda = shopVM.aplicarDescuento(negocio.costeTienda, descuento)
                            val job = invNegVM.comprarNegocio(negocio)
                            job.join()
                            procesarIngresosYCostesDeNegocios(negocio)

                            // Mostrar el modal de confirmación
                            purchasedProduct = Triple(
                                negocio.nombre,
                                iconFromString(negocio.icon),
                                "$${negocio.costeTienda.toInt()}"
                            )
                            showPurchaseModal = true

                            selectedNegocio = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimensions.widthPercentage(4f))
                ) {
                    Text(
                        "COMPRAR",
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(16f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
    // Diálogo de comida
    selectedComida?.let { comida ->
        AlertDialog(
            onDismissRequest = { selectedComida = null },
            containerColor = lightGreen.copy(alpha = 0.9f),
            shape = MaterialTheme.shapes.large,
            titleContentColor = Color.Black,
            textContentColor = Color.Black,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Seleccionar el icono adecuado según el nombre de la comida
                    val icon = when (comida.nombre) {
                        "Comida Diaria" -> Icons.Default.Fastfood
                        "Comida Semanal" -> Icons.Default.Restaurant
                        "Comida Premium" -> Icons.Default.RestaurantMenu
                        else -> Icons.Default.Fastfood
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(Dimensions.widthPercentage(12f))
                            .clip(CircleShape)
                            .background(darkGreen.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = comida.nombre,
                            tint = Color.White,
                            modifier = Modifier.size(Dimensions.widthPercentage(6f))
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))
                    Text(
                        text = comida.nombre,
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(24f),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.widthPercentage(2f))
                ) {
                    DetalleComidaItem(
                        icon = Icons.Default.Timer,
                        texto = "Duración:",
                        valor = "${comida.duracion} días",
                        fuentePrincipal = fuenteprincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleComidaItem(
                        icon = Icons.Default.AttachMoney,
                        texto = "Precio:",
                        valor = "$${comida.precio}",
                        fuentePrincipal = fuenteprincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleComidaItem(
                        icon = Icons.Default.TrendingUp,
                        texto = "Efecto:",
                        valor = "+${comida.efecto}",
                        fuentePrincipal = fuenteprincipal
                    )
                }
            },
            confirmButton = {
                val comidaVM : ComidaViewModel = viewModel()
                val uiScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        uiScope.launch {
                            val inventario = shopVM.getInventarioSync(playerId)
                            val count = shopVM.countComidaEnInventario(inventario.id, comida.nombre)
                            val precioDescontado = shopVM.aplicarDescuento(
                                comida.precio.toDouble(), descuento
                            ).toInt()
                            invComidaVM.comprarComida(comida, precioDescontado)

                            if (count == 0) {
                                shopVM.actualizarDescuento(playerId)
                            }

                            // Mostrar el modal de confirmación
                            val icon = when (comida.nombre) {
                                "Comida Diaria" -> Icons.Default.Fastfood
                                "Comida Semanal" -> Icons.Default.Restaurant
                                "Comida Premium" -> Icons.Default.RestaurantMenu
                                else -> Icons.Default.Fastfood
                            }
                            purchasedProduct = Triple(comida.nombre, icon, "$${precioDescontado}")
                            showPurchaseModal = true

                            selectedComida = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimensions.widthPercentage(4f))
                ) {
                    Text(
                        "COMPRAR",
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(16f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }

// Diálogo de tarjeta bonus
    selectedTarjeta?.let { tarjeta ->
        AlertDialog(
            onDismissRequest = { selectedTarjeta = null },
            containerColor = lightGreen.copy(alpha = 0.9f),
            shape = MaterialTheme.shapes.large,
            titleContentColor = Color.Black,
            textContentColor = Color.Black,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(Dimensions.widthPercentage(12f))
                            .clip(CircleShape)
                            .background(darkGreen.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = tarjeta.nombre,
                            tint = Color.White,
                            modifier = Modifier.size(Dimensions.widthPercentage(6f))
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))
                    Text(
                        text = tarjeta.nombre,
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(24f),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.widthPercentage(2f))
                ) {
                    // Descripción de la tarjeta (si existe)
                    if (tarjeta.nombreEfecto.isNotEmpty()) {
                        DetalleTarjetaItem(
                            icon = Icons.Default.Info,
                            texto = "Descripción:",
                            valor = tarjeta.nombreEfecto,
                            fuentePrincipal = fuenteprincipal
                        )
                    }
                }
            },
            confirmButton = {
                val tarjetaVM : TarjetaViewModel = viewModel()
                Button(
                    onClick = {
                        tarjeta.efectoValor = shopVM.aplicarDescuento(tarjeta.efectoValor.toDouble(), descuento).toInt()
                        invTarjetaVM.comprarTarjeta(tarjeta)

                        // Mostrar el modal de confirmación
                        purchasedProduct = Triple(
                            tarjeta.nombre,
                            Icons.Default.CardGiftcard,
                            "$${tarjeta.efectoValor}"
                        )
                        showPurchaseModal = true

                        selectedTarjeta = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimensions.widthPercentage(4f))
                ) {
                    Text(
                        "COMPRAR",
                        fontFamily = fuenteprincipal,
                        fontSize = Dimensions.responsiveSp(16f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
    // Modal de confirmación de compra
    purchasedProduct?.let { (name, icon, price) ->
        PurchaseConfirmationModal(
            isVisible = showPurchaseModal,
            productName = name,
            productIcon = icon,
            productPrice = price,
            onDismiss = {
                showPurchaseModal = false
                purchasedProduct = null
            },
            fuenteprincipal = fuenteprincipal,
            primaryGreen = primaryGreen,
            darkGreen = darkGreen,
            lightGreen = lightGreen
        )
    }
}

/**
 * Componente que representa el encabezado de una categoría (como "NEGOCIOS", "COMIDAS", etc).
 *
 * @param title Título de la categoría.
 * @param fuenteprincipal Fuente personalizada del texto.
 * @param darkGreen Color de fondo.
 * @param lightGreen Color no usado aquí, pero mantenido por consistencia con otras funciones.
 */

@Composable
fun CategorySection(
    title: String,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(2f)),
        colors = CardDefaults.cardColors(
            containerColor = darkGreen
        ),
        shape = RoundedCornerShape(Dimensions.widthPercentage(2f))
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = Dimensions.responsiveSp(18f),
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            modifier = Modifier
                .padding(
                    vertical = Dimensions.heightPercentage(1f),
                    horizontal = Dimensions.widthPercentage(4f)
                )
                .fillMaxWidth()
        )
    }
}

/**
 * Sección para mostrar una subcategoría de productos dentro de una categoría principal.
 *
 * @param title Nombre de la subcategoría (por ejemplo, "Bajo", "Medio", "Alto").
 * @param products Lista de productos que pertenecen a esta subcategoría.
 * @param fuenteprincipal Fuente para los textos.
 * @param darkGreen Color del texto principal.
 * @param lightGreen Color del fondo de las tarjetas de productos.
 */

@Composable
fun SubcategorySection(
    title: String,
    products: List<Product>,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(1f))
    ) {
        // Título de la subcategoría
        Text(
            text = title,
            color = Color.White,
            fontSize = Dimensions.responsiveSp(16f),
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            modifier = Modifier
                .padding(
                    vertical = Dimensions.heightPercentage(0.5f),
                    horizontal = Dimensions.widthPercentage(4f)
                )
                .fillMaxWidth()
        )

        // Productos de la subcategoría
        ProductRow(
            products = products,
            fuenteprincipal = fuenteprincipal,
            darkGreen = darkGreen,
            lightGreen = lightGreen
        )
    }
}

/**
 * Fila horizontal de productos con disposición uniforme.
 *
 * @param products Lista de productos que se mostrarán en una sola fila.
 * @param fuenteprincipal Fuente tipográfica.
 * @param darkGreen Color temático oscuro.
 * @param lightGreen Color temático claro.
 */

@Composable
fun ProductRow(
    products: List<Product>,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(0.5f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        products.forEach { product ->
            ProductCard(
                product = product,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Fila horizontal de productos con disposición uniforme.
 *
 * @param products Lista de productos que se mostrarán en una sola fila.
 * @param fuenteprincipal Fuente tipográfica.
 * @param darkGreen Color temático oscuro.
 * @param lightGreen Color temático claro.
 */

@Composable
fun ProductRowComidas(
    products: List<ProductComida>,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(0.5f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        products.forEach { product ->
            ProductCardComida(
                product = product,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProductRowTarjetas(
    products: List<ProductTarjeta>,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.heightPercentage(0.5f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        products.forEach { product ->
            ProductCardTarjeta(
                product = product,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Tarjeta visual para representar un producto en la tienda.
 *
 * Contiene icono, nombre, precio y un botón de compra.
 *
 * @param product Objeto del producto a mostrar.
 * @param fuenteprincipal Fuente para los textos.
 * @param darkGreen Color base del botón y textos destacados.
 * @param lightGreen Color del fondo de la tarjeta.
 * @param modifier Modificador opcional para personalizar el diseño exterior.
 */

@Composable
fun ProductCard(
    product: Product,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = Dimensions.widthPercentage(1.25f))
            .aspectRatio(0.65f)
            .clickable{product.onClick()},
        colors = CardDefaults.cardColors(
            containerColor = lightGreen.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
    ) {
        // Usamos Box para posicionar el botón de forma fija en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.widthPercentage(1f))
        ) {
            // Contenido superior (icono, nombre, precio)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Icon(
                    imageVector = product.icon,
                    contentDescription = product.name,
                    tint = darkGreen,
                    modifier = Modifier
                        .padding(
                            bottom = Dimensions.heightPercentage(0.5f),
                            top = Dimensions.heightPercentage(2f)
                        )
                        .size(Dimensions.widthPercentage(10f))
                )

                Text(
                    text = product.name,
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(12f),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = Dimensions.responsiveSp(14f),
                    modifier = Modifier
                        .padding(
                            vertical = Dimensions.heightPercentage(0.5f),
                            horizontal = Dimensions.widthPercentage(1f)
                        )
                        .fillMaxWidth()
                )

                Text(
                    text = "${'$'}${product.price}",
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(16f),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4f7123),
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                )
            }
        }
    }
}

/**
 * Tarjeta visual para representar un producto en la tienda.
 *
 * Contiene icono, nombre, precio y un botón de compra.
 *
 * @param product Objeto del producto a mostrar.
 * @param fuenteprincipal Fuente para los textos.
 * @param darkGreen Color base del botón y textos destacados.
 * @param lightGreen Color del fondo de la tarjeta.
 * @param modifier Modificador opcional para personalizar el diseño exterior.
 */

@Composable
fun ProductCardComida(
    product: ProductComida,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color,
    modifier: Modifier = Modifier
) {
    val shopVM : ShopViewModel = viewModel()
    val descuento by shopVM.descuentoComida.observeAsState(0)

    Card(
        modifier = modifier
            .padding(horizontal = Dimensions.widthPercentage(1.25f))
            .aspectRatio(0.65f)
            .clickable { product.onClick() },
        colors = CardDefaults.cardColors(
            containerColor = lightGreen.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
    ) {
        // Usamos Box para posicionar el botón de forma fija en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.widthPercentage(1f))
        ) {
            // Contenido superior (icono, nombre, precio)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Icon(
                    imageVector = product.icon,
                    contentDescription = product.name,
                    tint = darkGreen,
                    modifier = Modifier
                        .padding(
                            bottom = Dimensions.heightPercentage(0.5f),
                            top = Dimensions.heightPercentage(2f)
                        )
                        .size(Dimensions.widthPercentage(10f))
                )

                Text(
                    text = product.name,
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(12f),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = Dimensions.responsiveSp(14f),
                    modifier = Modifier
                        .padding(
                            vertical = Dimensions.heightPercentage(0.5f),
                            horizontal = Dimensions.widthPercentage(1f)
                        )
                        .fillMaxWidth()
                )

                Text(
                    text = "${'$'}${shopVM.aplicarDescuento(product.price.toDouble(),descuento)}",
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(16f),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4f7123),
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                )
            }
        }
    }
}

@Composable
fun ProductCardTarjeta(
    product: ProductTarjeta,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color,
    modifier: Modifier = Modifier
) {
    val shopVM : ShopViewModel = viewModel()
    val descuento by shopVM.descuentoComida.observeAsState(0)

    Card(
        modifier = modifier
            .padding(horizontal = Dimensions.widthPercentage(1.25f))
            .aspectRatio(0.65f)
            .clickable { product.onClick() },
        colors = CardDefaults.cardColors(
            containerColor = lightGreen.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(Dimensions.widthPercentage(4f))
    ) {
        // Usamos Box para posicionar el botón de forma fija en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.widthPercentage(1f))
        ) {
            // Contenido superior (icono, nombre, precio)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Icon(
                    imageVector = product.icon,
                    contentDescription = product.name,
                    tint = darkGreen,
                    modifier = Modifier
                        .padding(
                            bottom = Dimensions.heightPercentage(0.5f),
                            top = Dimensions.heightPercentage(2f)
                        )
                        .size(Dimensions.widthPercentage(10f))
                )

                Text(
                    text = product.name,
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(12f),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = Dimensions.responsiveSp(14f),
                    modifier = Modifier
                        .padding(
                            vertical = Dimensions.heightPercentage(0.5f),
                            horizontal = Dimensions.widthPercentage(1f)
                        )
                        .fillMaxWidth()
                )

                Text(
                    text = "${'$'}${product.price}",
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(16f),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4f7123),
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                )
            }
        }
    }
}

/**
 * Componente que muestra un ítem de detalle para el modal de comida.
 *
 * @param icon Icono a mostrar junto al texto.
 * @param texto Etiqueta del detalle.
 * @param valor Valor del detalle.
 * @param fuentePrincipal Fuente a utilizar.
 */
@Composable
fun DetalleComidaItem(
    icon: ImageVector,
    texto: String,
    valor: String,
    fuentePrincipal: FontFamily
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(Dimensions.widthPercentage(6f))
        )
        Spacer(modifier = Modifier.width(Dimensions.widthPercentage(2f)))
        Text(
            text = texto,
            fontFamily = fuentePrincipal,
            fontSize = Dimensions.responsiveSp(16f),
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            fontFamily = fuentePrincipal,
            fontSize = Dimensions.responsiveSp(18f),
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

/**
 * Componente que muestra un ítem de detalle para el modal de tarjeta.
 *
 * @param icon Icono a mostrar junto al texto.
 * @param texto Etiqueta del detalle.
 * @param valor Valor del detalle.
 * @param fuentePrincipal Fuente a utilizar.
 */
@Composable
fun DetalleTarjetaItem(
    icon: ImageVector,
    texto: String,
    valor: String,
    fuentePrincipal: FontFamily
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(Dimensions.widthPercentage(6f))
        )
        Spacer(modifier = Modifier.width(Dimensions.widthPercentage(2f)))
        Text(
            text = texto,
            fontFamily = fuentePrincipal,
            fontSize = Dimensions.responsiveSp(16f),
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            fontFamily = fuentePrincipal,
            fontSize = Dimensions.responsiveSp(18f),
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}



@Composable
fun DiscountBanner(
    descuento: Int,
    fuenteprincipal: FontFamily,
    modifier: Modifier = Modifier
) {
    if (descuento > 0) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimensions.widthPercentage(2f),
                    vertical = Dimensions.heightPercentage(1f)
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF6B35) // Naranja vibrante para destacar
            ),
            shape = RoundedCornerShape(Dimensions.widthPercentage(4f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF355D34), // Naranja
                                Color(0xFF518D50), // Naranja más claro
                                Color(0xFF355D34)  // Naranja
                            )
                        )
                    )
                    .padding(Dimensions.widthPercentage(4f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Icono de descuento animado
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(Dimensions.widthPercentage(12f))
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = "Descuento",
                            tint = Color.White,
                            modifier = Modifier.size(Dimensions.widthPercentage(7f))
                        )
                    }

                    // Contenido del mensaje
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = Dimensions.widthPercentage(3f)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Texto principal del descuento
                        Text(
                            text = "¡OFERTA ESPECIAL!",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(16f),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(Dimensions.heightPercentage(0.5f)))

                        // Porcentaje de descuento destacado
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "-$descuento%",
                                fontFamily = fuenteprincipal,
                                fontSize = Dimensions.responsiveSp(24f),
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(Dimensions.widthPercentage(2f))
                                    )
                                    .padding(
                                        horizontal = Dimensions.widthPercentage(2f),
                                        vertical = Dimensions.heightPercentage(0.5f)
                                    )
                            )

                            Spacer(modifier = Modifier.width(Dimensions.widthPercentage(2f)))

                            Text(
                                text = "EN COMIDAS",
                                fontFamily = fuenteprincipal,
                                fontSize = Dimensions.responsiveSp(12f),
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    // Icono de comida
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(Dimensions.widthPercentage(12f))
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Comida",
                            tint = Color.White,
                            modifier = Modifier.size(Dimensions.widthPercentage(7f))
                        )
                    }
                }

                // Decoración de esquinas con pequeños iconos
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(4f))
                        .align(Alignment.TopStart)
                        .offset(
                            x = Dimensions.widthPercentage(1f),
                            y = Dimensions.heightPercentage(0.5f)
                        )
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(4f))
                        .align(Alignment.TopEnd)
                        .offset(
                            x = -Dimensions.widthPercentage(1f),
                            y = Dimensions.heightPercentage(0.5f)
                        )
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(4f))
                        .align(Alignment.BottomStart)
                        .offset(
                            x = Dimensions.widthPercentage(1f),
                            y = -Dimensions.heightPercentage(0.5f)
                        )
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(4f))
                        .align(Alignment.BottomEnd)
                        .offset(
                            x = -Dimensions.widthPercentage(1f),
                            y = -Dimensions.heightPercentage(0.5f)
                        )
                )
            }
        }
    }
}


// Estado para controlar el modal de compra
@Composable
fun PurchaseConfirmationModal(
    isVisible: Boolean,
    productName: String,
    productIcon: ImageVector,
    productPrice: String,
    onDismiss: () -> Unit,
    fuenteprincipal: FontFamily,
    primaryGreen: Color = Color(0xFF9CCD5C),
    darkGreen: Color = Color(0xFF6B9A2F),
    lightGreen: Color = Color(0xFFB5E878)
) {
    if (isVisible) {
        // Animaciones
        var showContent by remember { mutableStateOf(false) }
        var showCheckmark by remember { mutableStateOf(false) }
        var showConfetti by remember { mutableStateOf(false) }

        val scaleAnimation by animateFloatAsState(
            targetValue = if (showContent) 1f else 0.3f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "scale"
        )

        val rotationAnimation by animateFloatAsState(
            targetValue = if (showCheckmark) 0f else -180f,
            animationSpec = tween(800, easing = FastOutSlowInEasing),
            label = "rotation"
        )

        LaunchedEffect(isVisible) {
            if (isVisible) {
                showContent = true
                delay(300)
                showCheckmark = true
                delay(200)
                showConfetti = true
                delay(2500) // Modal se cierra automáticamente después de 2.5 segundos
                onDismiss()
            }
        }

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .scale(scaleAnimation)
                        .padding(Dimensions.widthPercentage(8f)),
                    colors = CardDefaults.cardColors(
                        containerColor = lightGreen.copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(Dimensions.widthPercentage(6f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(Dimensions.widthPercentage(6f))
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(Dimensions.heightPercentage(3f)))

                        // Texto principal
                        Text(
                            text = "¡COMPRA EXITOSA!",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(24f),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(Dimensions.heightPercentage(1f)))

                        // Detalles del producto
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = darkGreen.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(Dimensions.widthPercentage(3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(Dimensions.widthPercentage(4f))
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Icono del producto
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(Dimensions.widthPercentage(12f))
                                        .clip(CircleShape)
                                        .background(darkGreen.copy(alpha = 0.2f))
                                ) {
                                    Icon(
                                        imageVector = productIcon,
                                        contentDescription = productName,
                                        tint = darkGreen,
                                        modifier = Modifier.size(Dimensions.widthPercentage(7f))
                                    )
                                }

                                Spacer(modifier = Modifier.width(Dimensions.widthPercentage(3f)))

                                // Información del producto
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = productName,
                                        fontFamily = fuenteprincipal,
                                        fontSize = Dimensions.responsiveSp(16f),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = productPrice,
                                        fontFamily = fuenteprincipal,
                                        fontSize = Dimensions.responsiveSp(14f),
                                        color = Color(0xFF4f7123)
                                    )
                                }

                                // Icono de añadido al inventario
                                Icon(
                                    imageVector = Icons.Default.Inventory,
                                    contentDescription = "Añadido al inventario",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(Dimensions.widthPercentage(6f))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                        // Mensaje adicional
                        Text(
                            text = "El artículo se ha añadido a tu inventario",
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(14f),
                            color = Color(0xFF4f7123),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(Dimensions.heightPercentage(1f)))

                        // Indicador de cierre automático
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = Color.Gray,
                                modifier = Modifier.size(Dimensions.widthPercentage(4f))
                            )
                            Spacer(modifier = Modifier.width(Dimensions.widthPercentage(1f)))
                            Text(
                                text = "Se cerrará automáticamente",
                                fontFamily = fuenteprincipal,
                                fontSize = Dimensions.responsiveSp(12f),
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}




/**
 * Modelo de datos que representa un producto disponible en la tienda del juego.
 *
 * @param name Nombre del producto.
 * @param price Precio en dinero del juego.
 * @param icon Icono representativo del producto.
 */

data class Product(
    val name: String,
    val price: Double,
    val icon: ImageVector,
    val onClick : () -> Unit
)
data class ProductTarjeta(
    val name: String,
    val price: Int,
    val icon: ImageVector,
    val onClick: () -> Unit
)
data class ProductComida(
    val name: String,
    val price: Int,
    val icon: ImageVector,
    val onClick: () -> Unit
)
data class ComidaTienda(
    val nombre: String,
    val duracion: Int,
    val precio: Int,
    val efecto: Int,
    val icon: ImageVector
)
