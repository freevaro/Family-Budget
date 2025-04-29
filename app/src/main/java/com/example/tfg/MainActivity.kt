package com.example.tfg

import BottomNavigationBar
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.views.*
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    companion object{
        var comprobanteMusica : Boolean = false
    }

//    fun getComprobante() : Boolean{
//        return comprobanteMusica
//    }
//
//    fun setComprobante(s : Boolean) {
//        comprobanteMusica = s
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var current = "loading"


        // Configura la status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = 0xFF6B9A2F.toInt()
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false

        setContent {
            TFGTheme {
                val navController: NavHostController = rememberNavController()
                val screenOrder = listOf(
                    "pantalla_tienda",
                    "pantalla_negocios",
                    "pantalla_juego",
                    "pantalla_calendario",
                    "pantalla_ajustes",
                    "pantalla_principal",
                    "loading"
                )

                var currentScreen by rememberSaveable { mutableStateOf("loading") }


                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    containerColor = Color(0xFF6B9A2F),
                    bottomBar = {
                        if (currentScreen != "pantalla_principal" && currentScreen != "loading"){
                                BottomNavigationBar(
                                    onNavigateToHome = { navController.navigate("pantalla_juego") },
                                    onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                    onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                    onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                    onNavigateToSettings = { navController.navigate("pantalla_ajustes") },
                                    currentScreen = currentScreen
                                )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "loading",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable("loading"){
                            LoadingScreen(modifier = Modifier,navController)
                        }
                        composable("pantalla_principal"){
                            MainScreen(modifier = Modifier,navController)
                            currentScreen = "pantalla_principal"
                        }
                        composable("pantalla_juego",
                            enterTransition = {
                                slideEnterTransition(currentScreen, "pantalla_juego", screenOrder)
                            },
                            exitTransition = {
                                slideExitTransition("pantalla_juego", currentScreen, screenOrder)
                            }
                        ) {
                            currentScreen = "pantalla_juego"
                            current = "dentro"
                            GameHomeScreen(
                                onEndTurnClick = {},
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_tienda",
                            enterTransition = {
                                slideEnterTransition(currentScreen, "pantalla_tienda", screenOrder)
                            },
                            exitTransition = {
                                slideExitTransition("pantalla_tienda", currentScreen, screenOrder)
                            }
                        ) {
                            currentScreen = "pantalla_tienda"
                            ShopScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = {},
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_negocios",
                            enterTransition = {
                                slideEnterTransition(currentScreen, "pantalla_negocios", screenOrder)
                            },
                            exitTransition = {
                                slideExitTransition("pantalla_negocios", currentScreen, screenOrder)
                            }
                        ) {
                            currentScreen = "pantalla_negocios"
                            BusinessScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = {},
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_calendario",
                            enterTransition = {
                                slideEnterTransition(currentScreen, "pantalla_calendario", screenOrder)
                            },
                            exitTransition = {
                                slideExitTransition("pantalla_calendario", currentScreen, screenOrder)
                            }
                        ) {
                            currentScreen = "pantalla_calendario"
                            CalendarScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = {},
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_ajustes",
                            enterTransition = {
                                slideEnterTransition(currentScreen, "pantalla_ajustes", screenOrder)
                            },
                            exitTransition = {
                                slideExitTransition("pantalla_ajustes", currentScreen, screenOrder)
                            }
                        ) {
                            currentScreen = "pantalla_ajustes"
                            SettingsScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = {},
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideEnterTransition(
        from: String?,
        to: String?,
        screenOrder: List<String>
    ): EnterTransition {
        val fromIndex = screenOrder.indexOf(from)
        val toIndex = screenOrder.indexOf(to)
        val direction = if (fromIndex < toIndex) AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right
        return slideIntoContainer(direction, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideExitTransition(
        from: String?,
        to: String?,
        screenOrder: List<String>
    ): ExitTransition {
        val fromIndex = screenOrder.indexOf(from)
        val toIndex = screenOrder.indexOf(to)
        val direction = if (fromIndex < toIndex) AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right
        return slideOutOfContainer(direction, animationSpec = tween(300))
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}
