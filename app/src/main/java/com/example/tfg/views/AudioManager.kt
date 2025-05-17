package com.example.tfg.views

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

/**
 * Objeto singleton responsable de la gestión de audio en la aplicación.
 *
 * Permite reproducir archivos de audio en bucle y detenerlos cuando sea necesario.
 * Solo se reproduce una pista a la vez; si se intenta reproducir la misma pista que ya está sonando, no hace nada.
 */
object AudioManager {
    private var currentResId: Int? = null
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Reproduce una pista de audio en bucle.
     *
     * Si ya está sonando el mismo recurso, no se vuelve a reproducir.
     * Si hay otra pista en reproducción, se detiene antes de iniciar la nueva.
     *
     * @param context Contexto de la aplicación necesario para inicializar el [MediaPlayer].
     * @param resId ID del recurso de audio a reproducir (debe estar en la carpeta `res/raw`).
     */
    fun play(context: Context, @RawRes resId: Int) {
        // Si ya está sonando la misma pista, no hacer nada
        if (currentResId == resId && mediaPlayer?.isPlaying == true) return

        // Si hay otro MediaPlayer activo, pararlo y liberarlo
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }

        // Crear y arrancar el nuevo
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            start()
        }
        currentResId = resId
    }

    /**
     * Detiene la reproducción de audio y libera los recursos del [MediaPlayer].
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

