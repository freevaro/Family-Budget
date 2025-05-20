package com.example.tfg

import com.example.tfg.entity.Comida
import com.example.tfg.dao.ComidaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Inserta una lista de comidas predefinidas en la base de datos al crearla.
 */
suspend fun prepopulateComidas(comidaDao: ComidaDao) = withContext(Dispatchers.IO) {
    val comidas = listOf(
        Comida(nombre = "Comida Diaria", duracion = 1, precio = 4000, efecto = 10),
        Comida(nombre = "Comida Semanal", duracion = 7, precio = 20000, efecto = 10),
        Comida(nombre = "Comida Premium", duracion = 1, precio = 15000, efecto = 25)
    )
    comidas.forEach { comidaDao.insert(it) }
}
