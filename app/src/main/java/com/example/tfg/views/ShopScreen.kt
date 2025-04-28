package com.example.tfg.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun ShopScreen(
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

    // Categorías de productos
    val categories = listOf("Todos", "Negocios", "Hogar", "Alimentos", "Tarjetas Bonus")
    var selectedCategory by remember { mutableStateOf("Todos") }

    // Productos de ejemplo
    val products = remember {
        listOf(
            Product("Negocio Básico", 500, Icons.Default.Business),
            Product("Negocio Premium", 1200, Icons.Default.BusinessCenter),
            Product("Muebles Hogar", 300, Icons.Default.Chair),
            Product("Electrodoméstico", 450, Icons.Default.Kitchen),
            Product("Comida Semanal", 100, Icons.Default.Restaurant),
            Product("Comida Premium", 200, Icons.Default.RestaurantMenu),
            Product("Tarjeta Negocio", 150, Icons.Default.CardGiftcard),
            Product("Tarjeta Dinero", 150, Icons.Default.CreditCard),
            Product("Tarjeta Aleatorio", 100, Icons.Default.Casino),
            Product("Negocio Avanzado", 800, Icons.Default.Store),
            Product("Comida Familiar", 150, Icons.Default.Fastfood),
            Product("Mueble Lujo", 600, Icons.Default.Weekend)
        )
    }

    // Filtrar productos según la categoría seleccionada
    val filteredProducts = when (selectedCategory) {
        "Negocios" -> products.filter { it.name.contains("Negocio") }
        "Hogar" -> products.filter { it.name.contains("Mueble") || it.name.contains("Electrodoméstico") }
        "Alimentos" -> products.filter { it.name.contains("Comida") }
        "Tarjetas Bonus" -> products.filter { it.name.contains("Tarjeta") }
        else -> products
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
                text = "TIENDA",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteprincipal,
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Barra de búsqueda
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar productos", fontFamily = fuenteprincipal) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = lightGreen.copy(alpha = 0.7f),
                    unfocusedBorderColor = darkGreen
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Categorías
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        fuenteprincipal = fuenteprincipal,
                        darkGreen = darkGreen,
                        lightGreen = lightGreen
                    )
                }
            }

            // Productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        fuenteprincipal = fuenteprincipal,
                        darkGreen = darkGreen,
                        lightGreen = lightGreen
                    )
                }
            }
        }

        // Barra de navegación inferior
        BottomNavigationBar(
            onNavigateToHome = onNavigateToHome,
            onNavigateToBusiness = onNavigateToBusiness,
            onNavigateToCalendar = onNavigateToCalendar,
            onNavigateToShop = onNavigateToShop,
            onNavigateToSettings = onNavigateToSettings,
            currentScreen = "Tienda",
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) darkGreen else lightGreen.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = category,
            color = if (isSelected) Color.White else darkGreen,
            fontFamily = fuenteprincipal,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    fuenteprincipal: FontFamily,
    darkGreen: Color,
    lightGreen: Color
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Icono del producto
            Icon(
                imageVector = product.icon,
                contentDescription = product.name,
                tint = darkGreen,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 8.dp)
            )

            // Nombre del producto
            Text(
                text = product.name,
                fontFamily = fuenteprincipal,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Precio
            Text(
                text = "$${product.price}",
                fontFamily = fuenteprincipal,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = darkGreen
            )

            // Botón comprar
            Button(
                onClick = { /* Lógica de compra */ },
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "COMPRAR",
                    fontFamily = fuenteprincipal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

data class Product(
    val name: String,
    val price: Int,
    val icon: ImageVector
)
