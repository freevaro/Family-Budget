package com.example.tfg.views

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onEndGame: () -> Unit = {},
    navController : NavHostController
) {
    val primaryGreen = Color(0xFF9CCD5C)
    val darkGreen = Color(0xFF6B9A2F)
    val fuenteprincipal = FontFamily(Font(R.font.barriecito_regular))

    var isMusicOn by remember { mutableStateOf(true) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(darkGreen, primaryGreen)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "AJUSTES",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteprincipal,
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Opción de música
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Música",
                    tint = if (isMusicOn) darkGreen else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Música",
                    fontSize = 18.sp,
                    fontFamily = fuenteprincipal,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isMusicOn,
                    onCheckedChange = { value ->
                        isMusicOn = value
                        // Aquí puedes pausar o reproducir música usando callback o mediaPlayer
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para finalizar partida
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "FINALIZAR PARTIDA",
                    fontSize = 16.sp,
                    fontFamily = fuenteprincipal,
                    color = Color.White
                )
            }
        }

        // Modal de confirmación
        if (showConfirmDialog) {
            AlertDialog(containerColor = primaryGreen,
                onDismissRequest = { showConfirmDialog = false },
                title = {
                    Text("Confirmar", fontFamily = fuenteprincipal)
                },
                text = {
                    Text("¿Seguro que quieres finalizar la partida?", fontFamily = fuenteprincipal)
                },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmDialog = false
                        navController.navigate("pantalla_principal")
                    }) {
                        Text("Sí", fontFamily = fuenteprincipal)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Cancelar", fontFamily = fuenteprincipal)
                    }
                }
            )
        }
    }
}
