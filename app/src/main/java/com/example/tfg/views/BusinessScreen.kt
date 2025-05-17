package com.example.tfg.views

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.R
import com.example.tfg.viewmodel.NegocioViewModel
import com.example.tfg.entity.Negocio

/**
 * Pantalla de visualización de negocios.
 *
 * Muestra un listado en forma de cuadrícula de los negocios disponibles, con un diseño personalizado
 * y filtrado por categorías. Los datos se obtienen del [NegocioViewModel] usando LiveData.
 *
 * @param onNavigateToHome Acción al pulsar "Inicio" en la barra de navegación.
 * @param onNavigateToBusiness Acción al pulsar "Negocios".
 * @param onNavigateToCalendar Acción al pulsar "Calendario".
 * @param onNavigateToShop Acción al pulsar "Tienda".
 * @param onNavigateToSettings Acción al pulsar "Opciones".
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val lightGreen = Color(0xFFB5E878)
    val fuentePrincipal = FontFamily(Font(R.font.barriecito_regular))

    // Obtener ViewModel y lista de Negocios desde la base de datos
    val viewModel: NegocioViewModel = viewModel()
    val negocios by viewModel.allNegocios.observeAsState(emptyList())

    // Categorías de filtro (basadas en el campo "categoria" de cada Negocio)
    val categories = listOf("Todos", "Negocios", "Servicios", "Comercios", "Otros")
    var selectedCategory by remember { mutableStateOf("Todos") }

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
                    imageVector = Icons.Default.Business,
                    contentDescription = "Negocios",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(Dimensions.widthPercentage(10f))
                        .padding(end = Dimensions.widthPercentage(2f))
                )
                Text(
                    text = "NEGOCIOS",
                    color = Color.Black,
                    fontSize = Dimensions.responsiveSp(28f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fuentePrincipal
                )
            }

            // Grid de Negocios
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = Dimensions.heightPercentage(10f)),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = negocios.filter { selectedCategory == "Todos" || it.categoria == selectedCategory },
                    key = { negocio -> negocio.id }
                ) { negocio ->
                    NegocioCard(
                        negocio = negocio,
                        fuentePrincipal = fuentePrincipal,
                        darkGreen = darkGreen,
                        lightGreen = lightGreen
                    )
                }
            }
        }
    }
}

/**
 * Composable que representa una tarjeta individual para mostrar un [Negocio].
 *
 * Incluye un icono, el nombre del negocio y los ingresos diarios.
 *
 * @param negocio Objeto [Negocio] a mostrar.
 * @param fuentePrincipal Fuente usada para el texto.
 * @param darkGreen Color verde oscuro del tema.
 * @param lightGreen Color verde claro del tema.
 */

@Composable
fun NegocioCard(
    negocio: Negocio,
    fuentePrincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimensions.widthPercentage(2f))
            .fillMaxWidth()
    ) {
        val icon = iconFromString(negocio.icon)
        // Icono circular del negocio
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(Dimensions.widthPercentage(20f))
                .clip(CircleShape)
                .background(lightGreen.copy(alpha = 0.7f))
                .clickable { /* Lógica para ver detalles */ }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = negocio.nombre,
                tint = darkGreen,
                modifier = Modifier.size(Dimensions.widthPercentage(10f))
            )
        }

        // Nombre del negocio
        Text(
            text = negocio.nombre,
            fontFamily = fuentePrincipal,
            fontSize = Dimensions.responsiveSp(14f),
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = Dimensions.heightPercentage(1f))
        )

        // Ingresos diarios
        Text(
            text = "${'$'}${negocio.ingresos.toInt()}/día",
            fontFamily = fuentePrincipal,
            fontSize = Dimensions.responsiveSp(12f),
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}



/**
 * Mapea un nombre de icono (guardado como texto en la base de datos) a su correspondiente [ImageVector].
 *
 * @param name Nombre del icono como string (por ejemplo: `"LocalCafe"`).
 * @return [ImageVector] correspondiente al nombre. Si no coincide, devuelve `Icons.Default.Storefront`.
 */

// Función para mapear el nombre del icono guardado en la base de datos a un ImageVector
fun iconFromString(name: String): ImageVector = when (name) {
    "LocalCafe" -> Icons.Default.LocalCafe
    "Restaurant" -> Icons.Default.Restaurant
    "Store" -> Icons.Default.Store
    "FitnessCenter" -> Icons.Default.FitnessCenter
    "MenuBook" -> Icons.Default.MenuBook
    "LocalPharmacy" -> Icons.Default.LocalPharmacy
    "LocalMovies" -> Icons.Default.LocalMovies
    "Hotel" -> Icons.Default.Hotel
    "ContentCut" -> Icons.Default.ContentCut
    "ShoppingCart" -> Icons.Default.ShoppingCart
    "Build" -> Icons.Default.Build
    "Cake" -> Icons.Default.Cake
    "LocalDrink" -> Icons.Default.LocalDrink
    "Newspaper" -> Icons.Default.Newspaper
    "Icecream" -> Icons.Default.Icecream
    "PhoneIphone" -> Icons.Default.PhoneIphone
    "LocalCarWash" -> Icons.Default.LocalCarWash
    "Fastfood" -> Icons.Default.Fastfood
    "LocalGroceryStore" -> Icons.Default.LocalGroceryStore
    "CardGiftcard" -> Icons.Default.CardGiftcard
    "PedalBike" -> Icons.Default.PedalBike
    "Checkroom" -> Icons.Default.Checkroom
    "PhotoCamera" -> Icons.Default.PhotoCamera
    "Pets" -> Icons.Default.Pets
    "LocalPostOffice" -> Icons.Default.LocalPostOffice
    "WbSunny" -> Icons.Default.WbSunny
    "Toys" -> Icons.Default.Toys
    "RollerSkating" -> Icons.Default.RollerSkating
    "Coffee" -> Icons.Default.Coffee
    "Computer" -> Icons.Default.Computer
    "Draw" -> Icons.Default.Draw
    "ChildCare" -> Icons.Default.ChildCare
    "TravelExplore" -> Icons.Default.TravelExplore
    "Storefront" -> Icons.Default.Storefront
    "Language" -> Icons.Default.Language
    "CarRepair" -> Icons.Default.CarRepair
    "BikeScooter" -> Icons.Default.BikeScooter
    "DesignServices" -> Icons.Default.DesignServices
    "BakeryDining" -> Icons.Default.BakeryDining
    "Spa" -> Icons.Default.Spa
    "Book" -> Icons.Default.Book
    "ShoppingBasket" -> Icons.Default.ShoppingBasket
    "FoodBank" -> Icons.Default.FoodBank
    "Shop" -> Icons.Default.Shop
    "Workspaces" -> Icons.Default.Workspaces
    "RestaurantMenu" -> Icons.Default.RestaurantMenu
    "FaceRetouchingNatural" -> Icons.Default.FaceRetouchingNatural
    "Code" -> Icons.Default.Code
    "PrecisionManufacturing" -> Icons.Default.PrecisionManufacturing
    "Movie" -> Icons.Default.Movie
    "RocketLaunch" -> Icons.Default.RocketLaunch
    "OndemandVideo" -> Icons.Default.OndemandVideo
    "Recycling" -> Icons.Default.Recycling
    "SmartToy" -> Icons.Default.SmartToy
    "Sailing" -> Icons.Default.Sailing
    "VideogameAsset" -> Icons.Default.VideogameAsset
    "Science" -> Icons.Default.Science
    "LocalHospital" -> Icons.Default.LocalHospital
    "ElectricCar" -> Icons.Default.ElectricCar
    "Tv" -> Icons.Default.Tv
    "SatelliteAlt" -> Icons.Default.SatelliteAlt
    "ShoppingBag" -> Icons.Default.ShoppingBag
    else -> Icons.Default.Storefront
}
