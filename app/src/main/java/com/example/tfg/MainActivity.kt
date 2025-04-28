package com.example.tfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.views.GameHomeScreen
import com.example.tfg.views.LoadingScreen
import com.example.tfg.views.MainScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.example.tfg.views.BusinessScreen
import com.example.tfg.views.CalendarScreen
import com.example.tfg.views.ShopScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TFGTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController: NavHostController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "pantalla_inicio",
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300))
                        }
                    ) {
                        composable("pantalla_inicio") {
                            LoadingScreen(Modifier.fillMaxSize(), navController)
                        }
                        composable("pantalla_principal") {
                            MainScreen(Modifier.fillMaxSize(), navController)
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
                                    navController.navigate("pantalla_juego")
                                },
                                onNavigateToBusiness = {
                                    navController.navigate("pantalla_negocios")
                                },
                                onNavigateToCalendar = {
                                    navController.navigate("pantalla_calendario")
                                },
                                onNavigateToShop = {
                                    navController.navigate("pantalla_tienda")
                                },
                                onNavigateToSettings = {
                                    // Navegación o lógica correspondiente
                                }
                            )
                        }
                        composable("pantalla_tienda") {
                            ShopScreen(
                                onNavigateToHome = {
                                    navController.navigate("pantalla_juego")
                                },
                                onNavigateToBusiness = {
                                    navController.navigate("pantalla_negocios")
                                },
                                onNavigateToCalendar = {
                                    navController.navigate("pantalla_calendario")
                                },
                                onNavigateToShop = {
                                    navController.navigate("pantalla_tienda")
                                },
                                onNavigateToSettings = {
                                    // Navegación a ajustes cuando la tengas
                                }
                            )
                        }
                        composable("pantalla_negocios") {
                            BusinessScreen(
                                onNavigateToHome = {
                                    navController.navigate("pantalla_juego")
                                },
                                onNavigateToBusiness = {
                                    navController.navigate("pantalla_negocios")
                                },
                                onNavigateToCalendar = {
                                    navController.navigate("pantalla_calendario")
                                },
                                onNavigateToShop = {
                                    navController.navigate("pantalla_tienda")
                                },
                                onNavigateToSettings = {
                                    // Navegación a ajustes cuando la tengas
                                }
                            )
                        }
                        composable("pantalla_calendario") {
                            CalendarScreen(
                                onNavigateToHome = {
                                    navController.navigate("pantalla_juego")
                                },
                                onNavigateToBusiness = {
                                    navController.navigate("pantalla_negocios")
                                },
                                onNavigateToCalendar = {
                                    navController.navigate("pantalla_calendario")
                                },
                                onNavigateToShop = {
                                    navController.navigate("pantalla_tienda")
                                },
                                onNavigateToSettings = {
                                    // Navegación a ajustes cuando la tengas
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
