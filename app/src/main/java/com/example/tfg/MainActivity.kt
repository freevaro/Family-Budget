package com.example.tfg

import androidx.annotation.RawRes
import BottomNavigationBar
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.views.*

// Singleton para gestionar el audio de la app

/**
 * Representa el estado actual del audio (activado o desactivado).
 *
 * @property enabled Indica si el audio está habilitado.
 */
data class AudioManagerState(var enabled: Boolean = true)


object AudioManager {
    private var currentResId: Int? = null
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Reproduce un archivo de audio dado si está habilitado y no se está reproduciendo ya.
     *
     * @param context Contexto de la aplicación necesario para crear el [MediaPlayer].
     * @param resId ID del recurso de audio a reproducir.
     * @param enabled Indica si el audio debe reproducirse.
     */
    fun play(context: Context, @RawRes resId: Int, enabled: Boolean) {
        if (!enabled) return stop()
        if (currentResId == resId && mediaPlayer?.isPlaying == true) return
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            start()
        }
        currentResId = resId
    }

    /**
     * Detiene y libera el recurso de audio si está siendo reproducido.
     */
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
        currentResId = null
    }
}

/**
 * Actividad principal de la aplicación.
 * Configura el tema, el controlador de navegación y gestiona el estado global de la música,
 * así como las transiciones entre pantallas.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = 0xFF6B9A2F.toInt()
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false

        setContent {
            TFGTheme {
                val navController: NavHostController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val context = LocalContext.current
                val lifecycle = LocalLifecycleOwner.current.lifecycle

                // Estado global de música
                var musicEnabled by rememberSaveable { mutableStateOf(true) }

                // Reproduce o detiene la música según la pantalla actual y el estado del audio
                LaunchedEffect(currentRoute, musicEnabled) {
                    when (currentRoute) {
                        "loading" -> AudioManager.stop()
                        "pantalla_principal" -> AudioManager.play(context, R.raw.mainscreen, musicEnabled)
                        "pantalla_juego",
                        "pantalla_tienda",
                        "pantalla_negocios",
                        "pantalla_calendario",
                        "pantalla_ajustes" -> AudioManager.play(context, R.raw.background, musicEnabled)
                        else -> AudioManager.stop()
                    }
                }

                // Detiene el audio cuando la actividad pasa al background
                DisposableEffect(lifecycle) {
                    val observer = LifecycleEventObserver { _: LifecycleOwner, event ->
                        if (event == Lifecycle.Event.ON_STOP) AudioManager.stop()
                    }
                    lifecycle.addObserver(observer)
                    onDispose { lifecycle.removeObserver(observer) }
                }

                var currentScreen by rememberSaveable { mutableStateOf("loading") }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    containerColor = Color(0xFF6B9A2F),
                    bottomBar = {
                        if (currentScreen !in listOf("pantalla_principal", "loading")) {
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
                    // Controla la navegación entre pantallas
                    NavHost(
                        navController = navController,
                        startDestination = "loading",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Aquí se declaran todas las pantallas disponibles
                        // Se actualiza el valor de currentScreen en cada composable
                        composable("loading") {
                            currentScreen = "loading"
                            LoadingScreen(Modifier, navController)
                        }
                        composable("pantalla_principal") {
                            currentScreen = "pantalla_principal"
                            MainScreen(
                                modifier = Modifier,
                                navController = navController,
                                musicEnabled = musicEnabled,
                                onMusicToggle = { musicEnabled = it }
                            )
                        }
                        composable(
                            "pantalla_juego",
                            enterTransition = { slideEnterTransition(currentScreen, "pantalla_juego") },
                            exitTransition = { slideExitTransition("pantalla_juego", currentScreen) }
                        ) {
                            currentScreen = "pantalla_juego"
                            GameHomeScreen(
                                onEndTurnClick = {},
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") },
                                navController = navController,
                                musicEnabled = musicEnabled,
                                onMusicToggle = { musicEnabled = it }
                            )
                        }
                        composable("pantalla_tienda") {
                            currentScreen = "pantalla_tienda"
                            ShopScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = {},
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_negocios") {
                            currentScreen = "pantalla_negocios"
                            BusinessScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = {},
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_calendario") {
                            currentScreen = "pantalla_calendario"
                            CalendarScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = {},
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = { navController.navigate("pantalla_ajustes") }
                            )
                        }
                        composable("pantalla_ajustes") {
                            currentScreen = "pantalla_ajustes"
                            SettingsScreen(
                                onNavigateToHome = { navController.navigate("pantalla_juego") },
                                onNavigateToBusiness = { navController.navigate("pantalla_negocios") },
                                onNavigateToCalendar = { navController.navigate("pantalla_calendario") },
                                onNavigateToShop = { navController.navigate("pantalla_tienda") },
                                onNavigateToSettings = {},
                                musicEnabled = musicEnabled,
                                onMusicToggle = { musicEnabled = it },
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Define las transiciones de entrada y salida entre pantallas con deslizamiento y fundido.
     *
     * @param from Pantalla de origen.
     * @param to Pantalla de destino.
     * @return Transición de entrada compuesta.
     */
    @OptIn(ExperimentalAnimationApi::class)
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideEnterTransition(
        from: String?, to: String?
    ): EnterTransition =
        slideIntoContainer(
            if (from == null) AnimatedContentTransitionScope.SlideDirection.Left else if (from < (to ?: ""))
                AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))

    @OptIn(ExperimentalAnimationApi::class)
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideExitTransition(
        from: String?, to: String?
    ): ExitTransition =
        slideOutOfContainer(
            if (from == null) AnimatedContentTransitionScope.SlideDirection.Left else if (from < (to ?: ""))
                AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(300)
        )
}
