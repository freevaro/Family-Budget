package com.example.tfg.views

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Definir los productos por categoría
    val negociosBajo = listOf(
        Product("Negocio Básico", 500, Icons.Default.Business),
        Product("Tienda Local", 550, Icons.Default.Store),
        Product("Puesto Mercado", 450, Icons.Default.Storefront)
    )

    val negociosMedio = listOf(
        Product("Negocio Medio", 800, Icons.Default.BusinessCenter),
        Product("Cafetería", 850, Icons.Default.LocalCafe),
        Product("Tienda Ropa", 750, Icons.Default.ShoppingBag)
    )

    val negociosAlto = listOf(
        Product("Negocio Premium", 1200, Icons.Default.Apartment),
        Product("Restaurante", 1300, Icons.Default.Restaurant),
        Product("Centro Comercial", 1500, Icons.Default.ShoppingCart)
    )

    val comidas = listOf(
        Product("Comida Semanal", 100, Icons.Default.Restaurant),
        Product("Comida Premium", 200, Icons.Default.RestaurantMenu),
        Product("Comida Familiar", 150, Icons.Default.Fastfood)
    )

    val tarjetasBonus = listOf(
        Product("Tarjeta Negocio", 150, Icons.Default.CardGiftcard),
        Product("Tarjeta Dinero", 150, Icons.Default.CreditCard),
        Product("Tarjeta Aleatorio", 100, Icons.Default.Casino)
    )

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
                .padding(Dimensions.widthPercentage(4f)) // 4% del ancho
                .verticalScroll(rememberScrollState())
        ) {
            // Título
            Text(
                text = "TIENDA",
                color = Color.Black,
                fontSize = Dimensions.responsiveSp(29f), // 5% del tamaño base
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteprincipal,
                modifier = Modifier
                    .padding(top = Dimensions.heightPercentage(8f), // 8% del alto
                        bottom = Dimensions.heightPercentage(3f))
                    .align(Alignment.CenterHorizontally)
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
                products = negociosBajo,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Subsección Medio
            SubcategorySection(
                title = "Medio",
                products = negociosMedio,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Subsección Alto
            SubcategorySection(
                title = "Alto",
                products = negociosAlto,
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
            ProductRow(
                products = comidas,
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
            ProductRow(
                products = tarjetasBonus,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen
            )

            // Espacio para la barra de navegación
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

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
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkGreen
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}

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
            .padding(vertical = 8.dp)
    ) {
        // Título de la subcategoría
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fuenteprincipal,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 16.dp)
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
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        products.forEach { product ->
            ProductCard(
                product = product,
                fuenteprincipal = fuenteprincipal,
                darkGreen = darkGreen,
                lightGreen = lightGreen,
                modifier = Modifier.width(Dimensions.widthPercentage(28f)) // 28% del ancho
            )
        }
    }
}

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
            .padding(horizontal = 5.dp)
            .aspectRatio(0.55f),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        // Usamos Box para posicionar el botón de forma fija en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
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
                        .padding(bottom = 8.dp, top = 25.dp)
                        .size(Dimensions.widthPercentage(12f))
                )

                Text(
                    text = product.name,
                    fontFamily = fuenteprincipal,
                    fontSize = Dimensions.responsiveSp(3.5f), // 3.5% del tamaño base                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "${'$'}${product.price}",
                    fontFamily = fuenteprincipal,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGreen,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Botón "COMPRAR" siempre en la parte inferior
            Button(
                onClick = { /* Lógica de compra */ },
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp,vertical = 0.dp)
            ) {
                Text(
                    text = "COMPRAR",
                    fontFamily = fuenteprincipal,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth()
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
