package com.example.tfg.views

import androidx.annotation.Dimension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tfg.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable

fun LoadingScreen(modifier: Modifier = Modifier, navController: NavController){
    val configuracion = LocalConfiguration.current
    val totalWidth = configuracion.screenWidthDp
    val totalHeight = configuracion.screenHeightDp
    val fuenteprincipal = FontFamily(
        Font(R.font.barriecito_regular)
    )
    var listaScreens by remember { mutableStateOf<ListaScreens?>(null) }


    // LINEA DE CARGA
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()



    Box(modifier = modifier.fillMaxSize().background(Color(0xFFc1ff72))){
        Column (modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy( // espacio fijo entre hijos
                16.dp,
                Alignment.CenterVertically
            )){
            Box(modifier = Modifier.offset(x = (totalWidth * 0f).dp, y = (totalHeight*-0.07f).dp).padding(start = Dimensions.widthPercentage(15f), top = Dimensions.heightPercentage(26f), end = Dimensions.widthPercentage(20f))){
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        text = "Family Budget",
                        fontFamily = fuenteprincipal,
                        fontSize = 30.sp,
                        modifier = Modifier.fillMaxWidth().padding(start = Dimensions.widthPercentage(12f), top = Dimensions.heightPercentage(10f)).align(Alignment.CenterHorizontally)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.dollar),
                        contentDescription = "Dollar",
                        modifier = Modifier
                            .padding(start = Dimensions.widthPercentage(3f), top = Dimensions.heightPercentage(6f), bottom = Dimensions.heightPercentage(8f))
                            .fillMaxWidth(0.4f)              // 40% del ancho para la imagen
                            .aspectRatio(1f)
                    )
                    LaunchedEffect(Unit) {
                        loading = true
                        scope.launch {
                            loadProgress { progress ->
                                currentProgress = progress
                            }
                            navController.navigate("pantalla_principal")
                        }
                    }
                    if (loading) {
                        LinearProgressIndicator(
                            progress = { currentProgress },
                            modifier = Modifier
                                .padding(start = Dimensions.widthPercentage(5f))
                                .fillMaxWidth()                  // llena esa columna (80% pantalla)
                                .height(4.dp),
                            color = Color(0xFF38A37F)
                        )
                    }
                }
                }
            }
        }
    }
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(15)
    }
}

