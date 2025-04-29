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
import androidx.compose.ui.unit.sp
import com.example.tfg.R

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
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                icon = Icons.Default.Settings,
                label = "Opciones",
                onClick = onNavigateToSettings,
                isSelected = currentScreen == "Opciones"
            )
        }
    }
}

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
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            color = itemColor,
            fontSize = 12.sp,
            fontFamily = fuenteprincipal,
            textAlign = TextAlign.Center
        )
    }
}