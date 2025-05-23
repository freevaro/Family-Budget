package com.example.tfg.views

import BottomNavigationBar
import android.R.attr.onClick
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

// Estado de selección de categoría y detalle
    var selectedNegocio by remember { mutableStateOf<Negocio?>(null) }


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
                    items = negocios,
                    key = { negocio -> negocio.id }
                ) { negocio ->
                    NegocioCard(
                        negocio = negocio,
                        fuentePrincipal = fuentePrincipal,
                        darkGreen = darkGreen,
                        lightGreen = lightGreen,
                        onClick = { selectedNegocio = negocio}
                    )
                }
            }
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
                        fontFamily = fuentePrincipal,
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
                        fuentePrincipal = fuentePrincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleNegocioItem(
                        icon = Icons.Default.Store,
                        texto = "Coste tienda:",
                        valor = "$${negocio.costeTienda.toInt()}",
                        fuentePrincipal = fuentePrincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleNegocioItem(
                        icon = Icons.Default.Build,
                        texto = "Coste mantenimiento:",
                        valor = "$${negocio.costeMantenimiento.toInt()}",
                        fuentePrincipal = fuentePrincipal
                    )

                    Spacer(modifier = Modifier.height(Dimensions.heightPercentage(2f)))

                    DetalleNegocioItem(
                        icon = Icons.Default.Category,
                        texto = "Categoría:",
                        valor = negocio.categoria,
                        fuentePrincipal = fuentePrincipal
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
                        .padding(horizontal = Dimensions.widthPercentage(4f))
                ) {
                    Text(
                        "CERRAR",
                        fontFamily = fuentePrincipal,
                        fontSize = Dimensions.responsiveSp(16f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
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
    lightGreen: Color,
    onClick : () -> Unit
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
                .clickable { onClick() }
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



/**
 * Composable que muestra un ítem de detalle para el modal de negocio.
 *
 * @param icon Icono a mostrar junto al texto.
 * @param texto Etiqueta del detalle.
 * @param valor Valor del detalle.
 * @param fuentePrincipal Fuente a utilizar.
 */
@Composable
fun DetalleNegocioItem(
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
