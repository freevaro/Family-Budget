import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tfg.R
import com.example.tfg.views.Dimensions

/**
 * Barra de navegación inferior personalizada.
 *
 * Muestra iconos para navegar entre las distintas pantallas de la app.
 * La pantalla actual se resalta mediante un mayor contraste en el icono y el texto.
 *
 * @param onNavigateToHome Callback para navegar a la pantalla de inicio.
 * @param onNavigateToBusiness Callback para navegar a la pantalla de negocios.
 * @param onNavigateToCalendar Callback para navegar a la pantalla de calendario.
 * @param onNavigateToShop Callback para navegar a la pantalla de la tienda.
 * @param onNavigateToSettings Callback para navegar a la pantalla de opciones.
 * @param currentScreen Nombre de la pantalla actual, utilizado para resaltar el ítem seleccionado.
 * @param modifier Modificador opcional para personalizar la barra.
 */
@Composable
fun BottomNavigationBar(
    onNavigateToHome: () -> Unit,
    onNavigateToBusiness: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToSettings: () -> Unit,
    currentScreen: String = "Inicio",
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF6B9A2F),
        shadowElevation = Dimensions.heightPercentage(1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.heightPercentage(1f)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Default.ShoppingCart,
                label = "Tienda",
                onClick = onNavigateToShop,
                isSelected = currentScreen == "Tienda"
            )

            BottomNavItem(
                icon = Icons.Default.Business,
                label = "Negocios",
                onClick = onNavigateToBusiness,
                isSelected = currentScreen == "Negocios"
            )

            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                onClick = onNavigateToHome,
                isSelected = currentScreen == "Inicio"
            )

            BottomNavItem(
                icon = Icons.Default.CalendarMonth,
                label = "Calendario",
                onClick = onNavigateToCalendar,
                isSelected = currentScreen == "Calendario"
            )

            BottomNavItem(
                icon = Icons.Default.Inventory,
                label = "Inventario",
                onClick = onNavigateToSettings,
                isSelected = currentScreen == "Opciones"
            )
        }
    }
}

/**
 * Elemento individual de la barra de navegación inferior.
 *
 * Muestra un icono y un texto debajo. Cambia su apariencia si está seleccionado.
 *
 * @param icon Icono a mostrar.
 * @param label Texto descriptivo del ítem.
 * @param onClick Acción a ejecutar al pulsar el ítem.
 * @param isSelected Indica si este ítem está actualmente seleccionado.
 */
@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    val itemColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
    val fuenteprincipal = FontFamily(
        Font(R.font.barriecito_regular)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(Dimensions.widthPercentage(2f))
            .clip(RoundedCornerShape(Dimensions.widthPercentage(3f)))
            .clickable(onClick = onClick)
            .padding(Dimensions.widthPercentage(1f))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = itemColor,
            modifier = Modifier.size(Dimensions.widthPercentage(6f))
        )

        Text(
            text = label,
            color = itemColor,
            fontSize = Dimensions.responsiveSp(12f),
            fontFamily = fuenteprincipal,
            textAlign = TextAlign.Center
        )
    }
}