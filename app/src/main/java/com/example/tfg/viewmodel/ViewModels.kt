package com.example.tfg.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import androidx.room.withTransaction
import com.example.tfg.AppDatabase
import com.example.tfg.entity.*
import com.example.tfg.viewmodel.PartidaDatos.listaJugadores
import com.example.tfg.viewmodel.PartidaDatos.partidaId
import kotlinx.coroutines.launch

/**
 * ViewModel de [Mes] que permite acceder y modificar los datos desde la interfaz de usuario.
 *
 * Solo pongo el del mes porque los demas son iguales.
 *
 * @param application Aplicación que proporciona el contexto para obtener la instancia de la base de datos.
 */
// 2) Define tu singleton de estado de turno:

object EstadoTurno {
    // Propiedades reactivas (importa getValue/setValue y mutableStateOf...)
    var idJugador     by mutableLongStateOf(0L)
    var nombre        by mutableStateOf("")
    var dinero        by mutableIntStateOf(0)
    var ingresos      by mutableIntStateOf(0)
    var costes        by mutableIntStateOf(0)
    var diaId         by mutableLongStateOf(0L)
    var diaNum        by mutableIntStateOf(0)
    var inventarioId  by mutableLongStateOf(0L)

    /** Actualiza todas las propiedades a partir de un Jugador y sus datos relacionados */
    fun loadFrom(
        jugador: Jugador,
        dia: Dia,
        inventario: Inventario
    ) {
        idJugador    = jugador.id
        nombre       = jugador.nombre
        dinero       = jugador.dinero.toInt()
        ingresos     = jugador.ingresos.toInt()
        costes       = jugador.gastos.toInt()
        diaId        = dia.id
        inventarioId = inventario.id
        diaNum       = dia.numeroDia
    }
}

// 3) Un singleton que orquesta la rotación de turnos:

object TurnoManager {
    private var players: List<Jugador> = emptyList()
    private var dias:    List<Dia>     = emptyList()
    private var invs:    List<Inventario> = emptyList()
    private var index   = 0
    var playerId : Long = 0L
    var diaId : Long = 0L

    /** Inicializa los jugadores (y su Día/Inventario) para la partida */
    suspend fun init(partidaId: Long, db: AppDatabase) {
        val daoJ = db.jugadorDao()
        val daoD = db.diaDao()
        val daoI = db.inventarioDao()

        players = daoJ.getPlayersForPartida(partidaId)
        // Asumimos que ya existe un Día y un Inventario por jugador
        dias  = players.map { p -> daoD.getDiaByJugadorAndMes(p.id, /*mesId*/1L) }
        invs  = players.map { p -> daoI.getByPlayerSync(p.id) }
        diaId = dias[0].id
        playerId = players[0].id

        // Carga el primer turno
        index = 0
        EstadoTurno.loadFrom(players[0], dias[0], invs[0])
    }

    /** Avanza al siguiente jugador y recarga EstadoTurno */
    fun next() {
        if (players.isEmpty()) return
        index = (index + 1) % players.size
        EstadoTurno.loadFrom(players[index], dias[index], invs[index])
        playerId = players[index].id
        diaId = dias[index].id

    }
}



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

object PartidaDatos{
    var partidaId : Long = 0L
    var listaJugadores : MutableList<Jugador> = mutableListOf()
    var jugador1Id : Long = 0L
    var jugador2Id : Long = 0L
    var jugador3Id : Long = 0L
    var jugador4Id : Long = 0L



    fun aplicarid(id : Long){
        if(jugador1Id == 0L){
            jugador1Id = id
        }else if(jugador2Id == 0L){
            jugador2Id = id
        }else if(jugador3Id == 0L){
            jugador3Id = id
        }else if(jugador4Id == 0L){
            jugador4Id = id
        }
    }

}

class PartidaStartViewModel(application: Application) : AndroidViewModel(application) {
    private val db                = AppDatabase.getInstance(application)
    private val partidaDao        = db.partidaDao()
    private val jugadorDao        = db.jugadorDao()
    private val partidaJugadorDao = db.partidaJugadorDao()
    private val inventarioDao     = db.inventarioDao()
    private val mesDao            = db.mesDao()
    private val diaDao            = db.diaDao()
    private val partidaDiaDao     = db.partidaDiaDao()
    // DAOs de inventarios hijos, listos para operaciones posteriores
    private val invNegocioDao     = db.inventarioNegocioDao()

    /**
     * Inicia una nueva partida:
     * 1) Crea Partida (ganador = "")
     * 2) Crea Mes inicial (número = 1)
     * 3) Para cada jugador:
     *    a) Inserta Jugador (dinero/ingresos/gastos = 0)
     *    b) Crea vínculo Partida–Jugador
     *    c) Crea Inventario vacío (solo fkJugador)
     *    d) Genera 31 días y enlaces Partida–Día
     *
     * No se insertan aún filas en inventario_negocio, inventario_comida o inventario_tarjeta:
     * esos hijos quedan vacíos hasta que el jugador adquiera algo.
     */
    fun empezarPartida(playerNames: List<String>) = viewModelScope.launch {
        db.withTransaction {
            // 1) Partida
            partidaId = partidaDao.insert(Partida(ganador = ""))

            // 2) Mes inicial
            val mesId = mesDao.insert(Mes(numero = 1))

            // 3) Por cada jugador, creamos todo lo necesario
            playerNames.forEach { nombre ->
                // a) Jugador
                val jugadorId = jugadorDao.insert(
                    Jugador(nombre = nombre, dinero = 0.0, ingresos = 0.0, gastos = 0.0)
                )
                // Se aplica el id a los datos
                PartidaDatos.aplicarid(jugadorId)
                // b) Relación partida–jugador
                partidaJugadorDao.insert(
                    PartidaJugador(fkPartida = partidaId, fkJugador = jugadorId)
                )
                // c) Inventario vacío para este jugador
                val inventarioId = inventarioDao.insert(
                    Inventario(fkJugador = jugadorId)
                )

                // d) Generar 31 días y relacionarlos con la partida
                (1..31).forEach { diaNum ->
                    val diaId = diaDao.insert(
                        Dia(numeroDia = diaNum, fkJugador = jugadorId, fkMes = mesId)
                    )
                    partidaDiaDao.insert(
                        PartidaDia(fkPartida = partidaId, fkDia = diaId)
                    )
                }
            }
            listaJugadores = jugadorDao.getPlayersForPartida(partidaId)
            TurnoManager.init(partidaId, db)
        }

    }
}

class GameHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val jugadorDao = AppDatabase
        .getInstance(application)
        .jugadorDao()

    /** LiveData con la lista de jugadores */
    val playersLiveData: LiveData<List<Jugador>> = jugadorDao.getAll()
}

class TiendaNegocioViewModel(application: Application) : AndroidViewModel(application) {
    private val db                 = AppDatabase.getInstance(application)
    private val tiendaNegocioDao   = db.tiendaNegocioDao()

    /** LiveData con todos los registros de negocio para una tienda dada */
    private val _tiendaId = MutableLiveData<Long>()
    val negociosEnTienda: LiveData<List<TiendaNegocio>> =
        _tiendaId.switchMap { tiendaId ->
            tiendaNegocioDao.getByTienda(tiendaId)
        }

    /** Marca qué tienda queremos consultar */
    fun setTiendaId(tiendaId: Long) {
        _tiendaId.value = tiendaId
    }

    /** Inserta una nueva relación tienda–negocio */
    fun insertarNegocioEnTienda(fkTienda: Long, fkNegocio: Long) = viewModelScope.launch {
        tiendaNegocioDao.insert(
            TiendaNegocio(fkTienda = fkTienda, fkNegocio = fkNegocio)
        )
    }

    /** Elimina una relación existente */
    fun eliminarNegocioDeTienda(item: TiendaNegocio) = viewModelScope.launch {
        tiendaNegocioDao.delete(item)
    }
}

/**
 * ViewModel para gestionar la lógica de la tienda del jugador.
 */
class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val jugadorDao = db.jugadorDao()
    private val negocioDao = db.negocioDao()
    private val tiendaDao = db.tiendaDao()
    private val tiendaNegocioDao = db.tiendaNegocioDao()
    private val comidaDao = db.comidaDao()
    private val tarjetaDao = db.tarjetaDao()

    private val _tiendaId = MutableLiveData<Long>()
    /** ID de la tienda generada para el jugador actual */
    val tiendaId: LiveData<Long> = _tiendaId

    /** Negocios asignados a la tienda */
    val negociosEnTienda: LiveData<List<Negocio>> = _tiendaId.switchMap { id ->
        tiendaNegocioDao.getNegociosForTienda(id)
    }

    /** Todas las comidas disponibles */
    val comidas: LiveData<List<Comida>> = comidaDao.getAll()

    /** Todas las tarjetas disponibles */
    val tarjetas: LiveData<List<Tarjeta>> = tarjetaDao.getFirst3()

    /**
     * Genera una nueva tienda para el jugador indicado y día dado.
     * Selecciona 3 negocios aleatorios de cada categoría: baja, media, alta.
     * Inserta las relaciones en tienda_negocio y expone la tiendaId.
     */

    fun generarTiendaNueva(jugadorId: Long, diaId: Long) = viewModelScope.launch {
        Log.d("ShopVM", "➔ jugadorId=$jugadorId , diaId=$diaId")
        if (diaId <= 0 || jugadorId <= 0) return@launch

        db.withTransaction {
            val existe = tiendaDao.existeTiendaParaDia(diaId)
            Log.d("ShopVM", "¿Ya existe tienda para día $diaId? $existe")
            if (existe) {
                Log.d("ShopVM", "❌ Omite inserción, ya había tienda")
                return@withTransaction
            }
            Log.d("ShopVM", "✅ No existía, procedo a insertar tienda")
            val newTiendaId = tiendaDao.insert(Tienda(fkJugador = jugadorId, fkDia = diaId))
            Log.d("ShopVM", "   → Insertada tienda con id $newTiendaId")
            _tiendaId.postValue(newTiendaId)

            // 2) Obtener y mezclar negocios por categoría
            val bajos = negocioDao.getByCategoria("Baja").shuffled().take(3)
            val medios = negocioDao.getByCategoria("Media").shuffled().take(3)
            val altos = negocioDao.getByCategoria("Alta").shuffled().take(3)

            // 3) Insertar cada relación tienda–negocio en transacción
            db.withTransaction {
                (bajos + medios + altos).forEach { negocio ->
                    val insertedId = tiendaNegocioDao.insert(
                        TiendaNegocio(fkTienda = newTiendaId, fkNegocio = negocio.id)
                    )
                    Log.d("ShopViewModel", "Inserted TiendaNegocio id=$insertedId for negocio=${negocio.id}")
                }
            }
        }
        val total = tiendaDao.countAll()
        Log.d("ShopVM", "Total de tiendas en DB tras generarTiendaNueva: $total")
    }

}





