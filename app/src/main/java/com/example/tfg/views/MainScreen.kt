package com.example.tfg.views

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.tfg.R
import java.time.format.TextStyle
import kotlin.random.Random


data class FallingIcon(var x: Float, val speed: Float, var y: Float)



@SuppressLint("RememberReturnType")
@Composable

fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    musicEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit
) {
    val configuracion = LocalConfiguration.current
    val totalHeight = configuracion.screenHeightDp
    val totalWidth = configuracion.screenWidthDp
    val density = LocalDensity.current
    val widthPx = with(density) { totalWidth.dp.toPx() }
    val heightPx = with(density) { totalHeight.dp.toPx() }
    val fuenteprincipal = FontFamily(
        Font(R.font.barriecito_regular)
    )
    val icons = remember {
        mutableStateOf(
            List(100) {
                FallingIcon(
                    x = Random.nextFloat() * widthPx,
                    speed = Random.nextFloat() * 3f + 1f,
                    y = Random.nextFloat() * -heightPx
                )
            }
        )
    }
    val showDialog = remember { mutableStateOf(false) }
    val botonModifier = Modifier
        .padding(horizontal = (totalWidth * 0.15f).dp, vertical = Dimensions.heightPercentage(2f))
        .fillMaxWidth()
        .height(Dimensions.heightPercentage(6f))
        .border(width = 2.dp, color = Color(0x00727272), shape = MaterialTheme.shapes.medium)
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFF9CCD5C))

    LaunchedEffect(Unit) {
        while (true) {
            icons.value = icons.value.map { icon ->
                val newY = icon.y + icon.speed
                if (newY > totalHeight * 2) {
                    icon.copy(
                        y = Random.nextFloat() * -heightPx,
                        x = Random.nextFloat() * widthPx
                    )
                } else {
                    icon.copy(y = newY)
                }
            }
            kotlinx.coroutines.delay(15L)
        }
    }


    Box(modifier = modifier.fillMaxSize().background(Color(0xFFc1ff72))){
            Image(
                painter = painterResource(id = R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        Canvas(modifier = Modifier.fillMaxSize()) {
            icons.value.forEach { icon ->
                drawContext.canvas.nativeCanvas.drawText(
                    "$",
                    icon.x,
                    icon.y,
                    android.graphics.Paint().apply {
                        textSize = 100f
                        color = android.graphics.Color.argb(100, 0, 100, 0)
                        isFakeBoldText = true
                    }
                )
            }
        }

        Column (modifier = Modifier.fillMaxSize()){
            Box(modifier = Modifier.fillMaxWidth().padding(top = Dimensions.heightPercentage(12f), start = Dimensions.widthPercentage(6f), end = Dimensions.widthPercentage(6f))){
                Row(modifier = Modifier.fillMaxWidth()
                        .padding(2.dp)
                        .border(width = 2.dp, color = Color(0x00727272), shape = MaterialTheme.shapes.medium)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF9CCD5C)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 10.dp).fillMaxWidth()){
                        Text("FAMILY BUDGET",
                            color = Color(0xFF000000),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontFamily = fuenteprincipal,
                            fontSize = Dimensions.responsiveSp(50f)
                        )

                    }

                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = (totalHeight * 0.15f).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = botonModifier
                        .clickable {navController.navigate("pantalla_juego")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Jugar",
                        color = Color(0xFF000000),
                        fontFamily = fuenteprincipal,
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showDialog.value) {
                    Dialog(onDismissRequest = { showDialog.value = false }) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF9CCD5C))
                                .padding(24.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Opciones",
                                    fontSize = 30.sp,
                                    fontFamily = fuenteprincipal,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                // 🔊 Switch para el sonido
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "🔊 Sonido:",
                                        fontSize = 20.sp,
                                        fontFamily = fuenteprincipal,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    androidx.compose.material3.Switch(
                                        checked = musicEnabled,
                                        onCheckedChange = { checked ->
                                            onMusicToggle(!musicEnabled)
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("🌐 Idioma: Español (Proximamente...)",
                                    fontSize = 20.sp,
                                    fontFamily = fuenteprincipal,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(30.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF759E73))
                                        .clickable {
                                            showDialog.value = false
                                        }
                                        .padding(horizontal = 20.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        "Cerrar",
                                        fontSize = 18.sp,
                                        fontFamily = fuenteprincipal,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                }



                Box(
                    modifier = botonModifier
                        .clickable {
                            showDialog.value = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Opciones",
                        color = Color(0xFF000000),
                        fontFamily = fuenteprincipal,
                        fontSize = 30.sp
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth().offset(y = (totalHeight*0.38f).dp).padding(vertical = 10.dp)){
                Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF759E73)).padding(vertical = 4.dp), horizontalArrangement = Arrangement.Center){
                    Box(modifier = Modifier
                        .size(60.dp)
    //                    .border(width = 2.dp, color = Color(0xFF000000), shape = MaterialTheme.shapes.medium)
                        .padding(6.dp)){
                        OutlinedIconButton(onClick = {},
                            ) {
                            Icon(Icons.Default.Home, contentDescription = "")
                        }
                    }
                }
            }
        }
    }
}

