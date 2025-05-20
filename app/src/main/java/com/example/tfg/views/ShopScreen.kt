package com.example.tfg.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.R
import com.example.tfg.viewmodel.ShopViewModel
import com.example.tfg.viewmodel.TurnoManager.playerId
import com.example.tfg.viewmodel.TurnoManager.diaId



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
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))


    val activity = LocalContext.current as ComponentActivity
    val shopVM: ShopViewModel = viewModel(
        viewModelStoreOwner = activity
    )

    // Generar tienda una sola vez si no existe
    LaunchedEffect(shopVM.tiendaId.value) {
        if (shopVM.tiendaId.value == null) {
            shopVM.generarTiendaNueva(playerId, diaId)
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

//    // Definir los productos por categoría
//    val negociosBajo = listOf(
//        Product(negocios[0].nombre, negocios[0].costeTienda, iconFromString(negocios[0].icon)),
//        Product(negocios[1].nombre, negocios[1].costeTienda, iconFromString(negocios[1].icon)),
//        Product(negocios[2].nombre, negocios[2].costeTienda, iconFromString(negocios[2].icon))
//    )
//
//    val negociosMedio = listOf(
//        Product(negocios[3].nombre, negocios[3].costeTienda, iconFromString(negocios[3].icon)),
//        Product(negocios[4].nombre, negocios[4].costeTienda, iconFromString(negocios[4].icon)),
//        Product(negocios[5].nombre, negocios[5].costeTienda, iconFromString(negocios[5].icon))
//    )
//
//    val negociosAlto = listOf(
//        Product(negocios[6].nombre, negocios[6].costeTienda, iconFromString(negocios[6].icon)),
//        Product(negocios[7].nombre, negocios[7].costeTienda, iconFromString(negocios[7].icon)),
//        Product(negocios[8].nombre, negocios[8].costeTienda, iconFromString(negocios[8].icon))
//    )

    val tarjetas by shopVM.tarjetas.observeAsState(emptyList())

    val primerasTarjetas = remember(tarjetas) { tarjetas.take(3) }
    val tarjetasBonus = primerasTarjetas.map {
        ProductTarjeta(it.nombre, it.efectoValor, Icons.Default.CardGiftcard)
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

    val primerasComidas = remember(comidas) { comidas.take(3) }
    val tarjetasComida = primerasComidas.map { comida ->
        // Elige el icono según el nombre, como antes
        val icon = when (comida.nombre) {
            "Comida Diaria"  -> Icons.Default.Fastfood
            "Comida Semanal" -> Icons.Default.Restaurant
            "Comida Premium" -> Icons.Default.RestaurantMenu
            else              -> Icons.Default.BuildCircle
        }
        ProductComida(comida.nombre, comida.precio, icon)
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
                products = negociosBajo.map { Product(it.nombre, it.costeTienda, iconFromString(it.icon)) },
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Subsección Medio
            SubcategorySection(
                title = "Medio",
                products = negociosMedio.map { Product(it.nombre, it.costeTienda, iconFromString(it.icon)) },
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Subsección Alto
            SubcategorySection(
                title = "Alto",
                products = negociosAlto.map { Product(it.nombre, it.costeTienda, iconFromString(it.icon)) },
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Sección Comidas
            CategorySection(
                title = "COMIDAS",
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
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
            .padding(vertical = Dimensions.heightPercentage(1f)),
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
            .aspectRatio(0.55f),
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
                    color = darkGreen,
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                )
            }

            // Botón "COMPRAR" siempre en la parte inferior
            Button(
                onClick = { /* Lógica de compra */ },
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(Dimensions.widthPercentage(2f)),
                contentPadding = PaddingValues(horizontal = Dimensions.widthPercentage(1f), vertical = Dimensions.heightPercentage(0.5f)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "COMPRAR",
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(13f),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
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
    Card(
        modifier = modifier
            .padding(horizontal = Dimensions.widthPercentage(1.25f))
            .aspectRatio(0.55f),
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
                    color = darkGreen,
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                )
            }

            // Botón "COMPRAR" siempre en la parte inferior
            Button(
                onClick = { /* Lógica de compra */ },
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(Dimensions.widthPercentage(2f)),
                contentPadding = PaddingValues(horizontal = Dimensions.widthPercentage(1f), vertical = Dimensions.heightPercentage(0.5f)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "COMPRAR",
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(13f),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
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
    Card(
        modifier = modifier
            .padding(horizontal = Dimensions.widthPercentage(1.25f))
            .aspectRatio(0.55f),
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
                    color = darkGreen,
                    modifier = Modifier.padding(bottom = Dimensions.heightPercentage(0.5f))
                )
            }

            // Botón "COMPRAR" siempre en la parte inferior
            Button(
                onClick = { /* Lógica de compra */ },
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(Dimensions.widthPercentage(2f)),
                contentPadding = PaddingValues(horizontal = Dimensions.widthPercentage(1f), vertical = Dimensions.heightPercentage(0.5f)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "COMPRAR",
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(13f),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
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
    val icon: ImageVector
)
data class ProductTarjeta(
    val name: String,
    val price: Int,
    val icon: ImageVector
)
data class ProductComida(
    val name: String,
    val price: Int,
    val icon: ImageVector
)
data class ComidaTienda(
    val nombre: String,
    val duracion: Int,
    val precio: Int,
    val efecto: Int,
    val icon: ImageVector
)
