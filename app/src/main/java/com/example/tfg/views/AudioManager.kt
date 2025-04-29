package com.example.tfg.views

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

object AudioManager {
    private var currentResId: Int? = null
    private var mediaPlayer: MediaPlayer? = null

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

    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
        currentResId = null
    }
}
