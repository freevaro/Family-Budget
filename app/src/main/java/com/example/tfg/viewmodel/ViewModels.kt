package com.example.tfg.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.tfg.AppDatabase
import com.example.tfg.entity.*
import kotlinx.coroutines.launch

/**
 * ViewModel de [Mes] que permite acceder y modificar los datos desde la interfaz de usuario.
 *
 * Solo pongo el del mes porque los demas son iguales.
 *
 * @param application Aplicación que proporciona el contexto para obtener la instancia de la base de datos.
 */

class MesViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).mesDao()
    val allMeses: LiveData<List<Mes>> = dao.getAll()
    fun insert(m: Mes)        = viewModelScope.launch { dao.insert(m) }
    fun update(m: Mes)        = viewModelScope.launch { dao.update(m) }
    fun delete(m: Mes)        = viewModelScope.launch { dao.delete(m) }
}

class DiaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).diaDao()
    val allDias: LiveData<List<Dia>> = dao.getAll()
    fun insert(d: Dia)        = viewModelScope.launch { dao.insert(d) }
    fun update(d: Dia)        = viewModelScope.launch { dao.update(d) }
    fun delete(d: Dia)        = viewModelScope.launch { dao.delete(d) }
}

class JugadorViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).jugadorDao()
    val allJugadores: LiveData<List<Jugador>> = dao.getAll()
    fun insert(j: Jugador)   = viewModelScope.launch { dao.insert(j) }
    fun update(j: Jugador)   = viewModelScope.launch { dao.update(j) }
    fun delete(j: Jugador)   = viewModelScope.launch { dao.delete(j) }
}

class ComidaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).comidaDao()
    val allComidas: LiveData<List<Comida>> = dao.getAll()
    fun insert(c: Comida)     = viewModelScope.launch { dao.insert(c) }
    fun update(c: Comida)     = viewModelScope.launch { dao.update(c) }
    fun delete(c: Comida)     = viewModelScope.launch { dao.delete(c) }
}

class TarjetaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).tarjetaDao()
    val allTarjetas: LiveData<List<Tarjeta>> = dao.getAll()
    fun insert(t: Tarjeta)    = viewModelScope.launch { dao.insert(t) }
    fun update(t: Tarjeta)    = viewModelScope.launch { dao.update(t) }
    fun delete(t: Tarjeta)    = viewModelScope.launch { dao.delete(t) }
}

class NegocioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).negocioDao()
    val allNegocios: LiveData<List<Negocio>> = dao.getAll()
    fun insert(n: Negocio)    = viewModelScope.launch { dao.insert(n) }
    fun update(n: Negocio)    = viewModelScope.launch { dao.update(n) }
    fun delete(n: Negocio)    = viewModelScope.launch { dao.delete(n) }
}

class TiendaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).tiendaDao()
    val allTiendas: LiveData<List<Tienda>> = dao.getAll()
    fun insert(t: Tienda)     = viewModelScope.launch { dao.insert(t) }
    fun update(t: Tienda)     = viewModelScope.launch { dao.update(t) }
    fun delete(t: Tienda)     = viewModelScope.launch { dao.delete(t) }
}

class InventarioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).inventarioDao()
    val allInventarios: LiveData<List<Inventario>> = dao.getAll()
    fun insert(i: Inventario) = viewModelScope.launch { dao.insert(i) }
    fun update(i: Inventario) = viewModelScope.launch { dao.update(i) }
    fun delete(i: Inventario) = viewModelScope.launch { dao.delete(i) }
}

class InventarioComidaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).inventarioComidaDao()
    val allItems: LiveData<List<InventarioComida>> = dao.getAll()
    fun insert(ic: InventarioComida) = viewModelScope.launch { dao.insert(ic) }
    fun update(ic: InventarioComida) = viewModelScope.launch { dao.update(ic) }
    fun delete(ic: InventarioComida) = viewModelScope.launch { dao.delete(ic) }
}

class InventarioTarjetaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).inventarioTarjetaDao()
    val allItems: LiveData<List<InventarioTarjeta>> = dao.getAll()
    fun insert(it: InventarioTarjeta) = viewModelScope.launch { dao.insert(it) }
    fun update(it: InventarioTarjeta) = viewModelScope.launch { dao.update(it) }
    fun delete(it: InventarioTarjeta) = viewModelScope.launch { dao.delete(it) }
}

class PartidaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).partidaDao()
    val allPartidas: LiveData<List<Partida>> = dao.getAll()
    fun insert(p: Partida) = viewModelScope.launch { dao.insert(p) }
    fun update(p: Partida) = viewModelScope.launch { dao.update(p) }
    fun delete(p: Partida) = viewModelScope.launch { dao.delete(p) }
}

class PartidaJugadorViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).partidaJugadorDao()
    val allLinks: LiveData<List<PartidaJugador>> = dao.getAll()
    fun insert(pj: PartidaJugador) = viewModelScope.launch { dao.insert(pj) }
    fun update(pj: PartidaJugador) = viewModelScope.launch { dao.update(pj) }
    fun delete(pj: PartidaJugador) = viewModelScope.launch { dao.delete(pj) }
}

class PartidaDiaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).partidaDiaDao()
    val allLinks: LiveData<List<PartidaDia>> = dao.getAll()
    fun insert(pd: PartidaDia) = viewModelScope.launch { dao.insert(pd) }
    fun update(pd: PartidaDia) = viewModelScope.launch { dao.update(pd) }
    fun delete(pd: PartidaDia) = viewModelScope.launch { dao.delete(pd) }
}
