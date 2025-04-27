package com.example.tfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.views.GameHomeScreen
import com.example.tfg.views.LoadingScreen
import com.example.tfg.views.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TFGTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

//                    NavHost(navController = navController, startDestination = "pantalla_inicio") {
//                        composable("pantalla_inicio") {
//                            MainScreen(Modifier.padding(innerPadding), navController)
//                        }
//                    }



                    NavHost(navController = navController, startDestination = "pantalla_inicio") {
                        composable("pantalla_inicio") {
                            LoadingScreen(Modifier.padding(innerPadding), navController)
                        }
                        composable("pantalla_principal") {
                            MainScreen(Modifier.padding(innerPadding), navController)
                        }
                        composable("pantalla_juego") {
                            GameHomeScreen(
                                playerName = "David",
                                currentDay = 7,
                                totalDays = 30,
                                cash = 600,
                                passiveIncome = 80,
                                dailyExpenses = 50,
                                onEndTurnClick = {
                                    navController.navigate("pantalla_evento")
                                },
                                onNavigateToHome = {
                                    navController.navigate("pantalla_principal")
                                },
                                onNavigateToBusiness = {
                                    // Navegación o lógica correspondiente
                                },
                                onNavigateToCalendar = {
                                    // Navegación o lógica correspondiente
                                },
                                onNavigateToShop = {
                                    // Navegación o lógica correspondiente
                                },
                                onNavigateToSettings = {
                                    // Navegación o lógica correspondiente
                                }
                            )
                        }


                    }


                }
            }
        }
    }
}