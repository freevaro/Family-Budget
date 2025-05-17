package com.example.tfg.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Objeto que proporciona funciones utilitarias para calcular dimensiones relativas
 * a la pantalla del dispositivo de forma responsiva.
 *
 * Las funciones se basan en [LocalConfiguration] para adaptarse dinámicamente
 * a la resolución y densidad del dispositivo.
 */

object Dimensions {
    /**
     * Devuelve el ancho de la pantalla en dp.
     *
     * @return Ancho de la pantalla actual como [Dp].
     */
    @Composable
    fun screenWidth() = LocalConfiguration.current.screenWidthDp.dp


    /**
     * Devuelve el alto de la pantalla en dp.
     *
     * @return Alto de la pantalla actual como [Dp].
     */

    @Composable
    fun screenHeight() = LocalConfiguration.current.screenHeightDp.dp

    /**
     * Calcula un tamaño de fuente responsivo a partir de un valor base.
     *
     * Tiene en cuenta la escala de fuente del sistema para mejorar la accesibilidad.
     *
     * @param baseSp Tamaño base en sp.
     * @return Tamaño escalado en sp.
     */

    @Composable
    fun responsiveSp(baseSp: Float) = (baseSp * LocalConfiguration.current.fontScale).sp

    /**
     * Calcula un valor en dp que representa un porcentaje del ancho de pantalla.
     *
     * @param percentage Porcentaje del ancho (por ejemplo, 50f para la mitad).
     * @return Valor correspondiente en [Dp].
     */

    @Composable
    fun widthPercentage(percentage: Float) = (LocalConfiguration.current.screenWidthDp * percentage / 100).dp


    /**
     * Calcula un valor en dp que representa un porcentaje del alto de pantalla.
     *
     * @param percentage Porcentaje del alto (por ejemplo, 25f para un cuarto).
     * @return Valor correspondiente en [Dp].
     */

    @Composable
    fun heightPercentage(percentage: Float) = (LocalConfiguration.current.screenHeightDp * percentage / 100).dp
}