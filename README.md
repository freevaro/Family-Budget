# Family Budget Game

**Family Budget** es una aplicación desarrollada con **Jetpack Compose** para Android. Se trata de un videojuego con un diseño inspirado en juegos antiguos de movil, con el objetivo de ganar el maximo dinero posible de entre los jugadores de la partida.


---

## Índice

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Uso](#uso)
- [Contribución](#contribución)
- [Licencia](#licencia)

---

## Características

- **Pantalla de carga** con barra de progreso.
- **Navegación inferior** personalizada para acceder a secciones: Tienda, Negocios, Juego, Calendario y Ajustes.
- **Pantalla principal** con animación de iconos cayendo y opciones para Jugar u Opciones.
- **Pantalla de juego** que muestra estadísticas de turno (dinero, ingresos, gastos) y permite finalizar turno.
- **Pantalla de negocios**: listado de negocios de ejemplo, filtrado por categoría.
- **Pantalla de tienda**: catálogo de productos de ejemplo, con búsqueda y filtros por categoría.
- **Pantalla de calendario**: vista mensual con día actual seleccionado y resumen de estadísticas diarias.
- **Pantalla de ajustes**: control de música y opción de finalizar partida.
- **Interoperabilidad** con `NavHostController` y transiciones animadas entre pantallas.

---

## Tecnologías

- **Lenguaje:** Kotlin
- **Toolkit UI:** Jetpack Compose (versión estable más reciente)
- **Arquitectura:** Single-Activity + Navigation Compose
- **Temas y estilos:** Material3 (Material You)
- **Animaciones:** `AnimatedVisibility`, transiciones de entrada/salida con `slideIntoContainer` y `fadeIn`/`fadeOut`
- **Música y multimedia:** `MediaPlayer` para audio de fondo

---

## Instalación y Ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/family-budget-game.git
   ```
2. Abre el proyecto en **Android Studio** (recomendado Arctic Fox o superior).
3. Asegúrate de tener instalado el **SDK de Android** para API nivel 21 o superior.
4. Sincroniza las dependencias con Gradle.
5. Ejecuta el proyecto en un emulador o dispositivo físico con Android 6.0+.

---

## Uso

- Tras la pantalla de carga, se accede a la pantalla principal.
- Desde la pantalla principal, pulsa **Jugar** para entrar en el juego o **Opciones** para configurar música.
- La **barra inferior** permite navegar entre Tienda, Negocios, Juego, Calendario y Ajustes (disponible tras la carga).

---

## Contribución

1. Haz un fork de este repositorio.
2. Crea una rama (`git checkout -b feature/mi-mejora`).
3. Realiza tus cambios y commitea (`git commit -m 'Añadir nueva característica'`).
4. Haz push a la rama (`git push origin feature/mi-mejora`).
5. Abre un Pull Request.

---

## Licencia

Este proyecto está bajo la [MIT License](LICENSE).

---

*¡Gracias por visitar el repositorio!*

