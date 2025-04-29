package com.example.tfg.views

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.tfg.R

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
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))

    // Categorías de negocios
    val categories = listOf("Todos", "Negocios", "Servicios", "Comercios", "Otros")
    var selectedCategory by remember { mutableStateOf("Todos") }

    // Negocios de ejemplo
    val businesses = remember {
        listOf(
            Business("Cafetería", 50, Icons.Default.LocalCafe),
            Business("Restaurante", 80, Icons.Default.Restaurant),
            Business("Tienda", 60, Icons.Default.Store),
            Business("Gimnasio", 70, Icons.Default.FitnessCenter),
            Business("Librería", 40, Icons.Default.MenuBook),
            Business("Farmacia", 90, Icons.Default.LocalPharmacy),
            Business("Cine", 100, Icons.Default.LocalMovies),
            Business("Hotel", 120, Icons.Default.Hotel),
            Business("Peluquería", 30, Icons.Default.ContentCut),
            Business("Supermercado", 110, Icons.Default.ShoppingCart),
            Business("Taller", 65, Icons.Default.Build),
            Business("Panadería", 45, Icons.Default.Cake)
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
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = "NEGOCIOS",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteprincipal,
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 75.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Negocios
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(businesses) { business ->
                    BusinessCard(
                        business = business,
                        fuenteprincipal = fuenteprincipal,
                        darkGreen = darkGreen,
                        lightGreen = lightGreen
                    )
                }
            }
        }

        // Barra de navegación inferior
    }
}

@Composable
fun BusinessCard(
    business: Business,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // Icono circular del negocio
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(lightGreen.copy(alpha = 0.7f))
                .clickable { /* Lógica para ver detalles */ }
        ) {
            Icon(
                imageVector = business.icon,
                contentDescription = business.name,
                tint = darkGreen,
                modifier = Modifier.size(40.dp)
            )
        }

        // Nombre del negocio
        Text(
            text = business.name,
            fontFamily = fuenteprincipal,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Ingresos diarios
        Text(
            text = "$${business.dailyIncome}/día",
            fontFamily = fuenteprincipal,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

data class Business(
    val name: String,
    val dailyIncome: Int,
    val icon: ImageVector
)