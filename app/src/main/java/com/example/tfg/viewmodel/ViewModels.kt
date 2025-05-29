package com.example.tfg.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.withTransaction
import com.example.tfg.AppDatabase
import com.example.tfg.dao.ComidaDao
import com.example.tfg.dao.InventarioComidaDao
import com.example.tfg.dao.InventarioComidaWithComida
import com.example.tfg.dao.InventarioNegocioDao
import com.example.tfg.dao.InventarioNegocioWithNegocio
import com.example.tfg.dao.InventarioTarjetaDao
import com.example.tfg.dao.InventarioTarjetaWithTarjeta
import com.example.tfg.dao.JugadorDao
import com.example.tfg.dao.JugadorEfectoDao
import com.example.tfg.dao.NegocioDao
import com.example.tfg.dao.PartidaJugadorDao
import com.example.tfg.dao.ResumenDiaDao
import com.example.tfg.dao.ResumenDiaDao_Impl
import com.example.tfg.dao.TarjetaDao
import com.example.tfg.entity.*
import com.example.tfg.viewmodel.EstadoTurno.costes
import com.example.tfg.viewmodel.EstadoTurno.costesOriginal
import com.example.tfg.viewmodel.EstadoTurno.dinero
import com.example.tfg.viewmodel.EstadoTurno.idJugador
import com.example.tfg.viewmodel.EstadoTurno.ingresos
import com.example.tfg.viewmodel.EstadoTurno.ingresosOriginal
import com.example.tfg.viewmodel.EstadoTurno.jugador
import com.example.tfg.viewmodel.PartidaDatos.listaJugadores
import com.example.tfg.viewmodel.PartidaDatos.partidaId
import com.example.tfg.viewmodel.TurnoManager.applyEffectTo
import com.example.tfg.viewmodel.TurnoManager.players
import com.example.tfg.views.Resumen
import com.example.tfg.views.Resumen.numDia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.cos

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
    var ingresosOriginal by mutableIntStateOf(0)
    var costes        by mutableIntStateOf(0)
    var costesOriginal by mutableIntStateOf(0)
    var diaId         by mutableLongStateOf(0L)
    var diaNum        by mutableIntStateOf(0)
    var inventarioId  by mutableLongStateOf(0L)
    var jugador       by mutableStateOf<Jugador>(Jugador(idJugador,nombre,dinero.toDouble(),ingresos.toDouble(),ingresosOriginal,costes.toDouble(),costesOriginal))

    /** Actualiza todas las propiedades a partir de un Jugador y sus datos relacionados */
    fun loadFrom(
        jugador : Jugador,
        dia: Dia,
        inventario: Inventario
    ) {
        idJugador    = jugador.id
        nombre       = jugador.nombre
        dinero       = jugador.dinero.toInt()
        ingresos     = jugador.ingresos.toInt()
        ingresosOriginal = jugador.ingresosOriginal
        costes       = jugador.gastos.toInt()
        costesOriginal = jugador.gastosOriginal
        diaId        = dia.id
        inventarioId = inventario.id
        diaNum       = dia.numeroDia
    }
    fun updateJugador(){
        jugador = Jugador(idJugador,nombre,dinero.toDouble(),ingresos.toDouble(),ingresosOriginal,costes.toDouble(),costesOriginal)
    }
}

// 3) Un singleton que orquesta la rotación de turnos:
object TurnoManager {
    var players: MutableList<Jugador> = mutableListOf()
    // Ahora almacenamos, para cada jugador, la lista completa de sus días del mes
    private var diasPorJugador: List<List<Dia>> = emptyList()
    private var invsPorJugador: List<Inventario> = emptyList()
    private lateinit var invComidaDao : InventarioComidaDao
    private lateinit var db_turno : AppDatabase
    private lateinit var invTarjetaDao : InventarioTarjetaDao
    private lateinit var jugadorEfectosDao : JugadorEfectoDao
    private lateinit var jugadorDao : JugadorDao
    private lateinit var resumenDiaDao : ResumenDiaDao
    private lateinit var invNegDao : InventarioNegocioDao
    private lateinit var partidaJugador : PartidaJugadorDao

    // Índice de jugador actual (0 .. players.size-1)
    private var index = 0

    // Contador de turno total (número de veces que se ha llamado next())
    var turno = 0

    var ultimoTurnoGenerado = turno -1

    // Número de día actual (1 .. max días)
    var diaNum = 1
        private set

    // IDs expuestos para la UI / Base de datos
    var playerId: Long = 0L
        private set
    var diaId: Long = 0L
        private set


    // Luego, en JugadorEfectoViewModel:
    suspend fun applyEffectTo(playerId: Long, effect: JugadorEfecto) {
        // 1) Cargo el jugador
        val jugador = db_turno.jugadorDao().getById(playerId)
        val updated = when (effect.campo_afectado) {
            "dinero" ->
                if (effect.tipo == "positivo")
                    jugador.copy(dinero = jugador.dinero + effect.ingresos)
                else
                    jugador.copy(dinero = jugador.dinero - effect.ingresos)

            "negocio" -> {
                when {
                    effect.tipo == "positivo" && effect.gastos == 0.0 ->
                        jugador.copy(ingresos = jugador.ingresos * (1 + effect.ingresos / 100))
                    effect.tipo == "negativo" && effect.gastos == 0.0 ->
                        jugador.copy(ingresos = jugador.ingresos * (1 - effect.ingresos / 100))
                    effect.tipo == "positivo" && effect.ingresos == 0.0 ->
                        jugador.copy(gastos = jugador.gastos * (1 - effect.gastos / 100))
                    effect.tipo == "negativo" && effect.ingresos == 0.0 ->
                        jugador.copy(gastos = jugador.gastos * (1 + effect.gastos / 100))
                    else ->
                        jugador
                }
            }
            else -> jugador
        }

        // 4) Persisto el jugador actualizado
        db_turno.jugadorDao().update(updated)

        // 5) Si es el jugador activo, recargo EstadoTurno
        if (playerId == EstadoTurno.idJugador) {
            EstadoTurno.updateJugador()
        }
    }

    /** Inicializa los jugadores y carga todos los Días e Inventarios para el mes */
    suspend fun init(partidaId: Long, db: AppDatabase) {
        val daoJ = db.jugadorDao()
        val daoD = db.diaDao()
        val daoI = db.inventarioDao()
        invComidaDao = db.inventarioComidaDao()
        db_turno = db
        invTarjetaDao = db.inventarioTarjetaDao()
        jugadorEfectosDao = db.jugadorEfectoDao()
        jugadorDao = db.jugadorDao()
        resumenDiaDao = db.resumenDiaDao()
        invNegDao = db.inventarioNegocioDao()
        partidaJugador = db.partidaJugadorDao()

        players = daoJ.getPlayersForPartida(partidaId).toMutableList()  // <-- mutableListOf
        // Para cada jugador, cargar la lista de 31 días del mes
        diasPorJugador = players.map { p ->
            daoD.getDiasByJugadorAndMes(p.id, /* mesId */ 1L)
        }
        // Para cada jugador y cada día, cargar su inventario
        invsPorJugador = daoI.getInventarioByJugador(partidaId)

        // Empezamos en el primer jugador del día 1
        index = 0
        diaNum = 1
        actualizarEstado()
    }


    /**
     * Calcula los ingresos y costes de TODOS los negocios del jugador activo,
     * los suma al jugador en la BD y actualiza EstadoTurno y memoria interna.
     */
    suspend fun procesarIngresosYCostesDeNegocios() {
        // 1) Leemos los detalles de los negocios
        val invId = EstadoTurno.inventarioId
        val detalles: List<InventarioNegocioWithNegocio> =
            db_turno.inventarioNegocioDao()
                .getConDetalle(invId)
                .first()  // Flow → List

        // 2) Acumulamos ingresos y costes
        var ingresosTotal = 0.0
        var costesTotal   = 0.0
        detalles.forEach { item ->
            ingresosTotal += item.negocio.ingresos * item.invNegocio.cantidad
            costesTotal   += item.negocio.costeMantenimiento * item.invNegocio.cantidad
        }

        // 3) Recuperamos el jugador actual de la BD
        val jugador = db_turno.jugadorDao().getById(EstadoTurno.idJugador)

        // 4) Creamos una copia actualizada y la persistimos
        val actualizado = jugador.copy(
            ingresos = jugador.ingresos + ingresosTotal,
            gastos   = jugador.gastos   + costesTotal,
            ingresosOriginal = ingresosTotal.toInt(),
            gastosOriginal = costesTotal.toInt()
        )
        ingresosOriginal = ingresosTotal.toInt()
        costesOriginal = costesTotal.toInt()
        db_turno.jugadorDao().update(actualizado)  // ← persiste en BD

        // 5) Actualizamos EstadoTurno para reflejar los nuevos valores
        EstadoTurno.dinero   = actualizado.dinero.toInt()
        EstadoTurno.ingresos = actualizado.ingresos.toInt()
        EstadoTurno.costes   = actualizado.gastos.toInt()
        EstadoTurno.updateJugador()

        // 6) Refresca también la lista interna de TurnoManager
        refreshCurrentPlayerInMemory()
    }


    /** Llama a esto cuando hayas persistido un cambio en el jugador */
    fun refreshCurrentPlayerInMemory() {
        players[index] = EstadoTurno.jugador
    }

    suspend fun cargarJugadoresLista(){
        players = jugadorDao.getPlayersForPartida(partidaId)
    }

    /**
     * Aplica sobre los ingresos y costes almacenados en EstadoTurno
     * todos los efectos activos (duracion > 0) cuyo campo_afectado = "negocio".
     */
    suspend fun aplicarEfectosNegocioActivos() {
        // 1) Leemos todos los efectos del jugador actual
        val efectos = db_turno.jugadorEfectoDao()
            .getByJugador(EstadoTurno.idJugador)
            .first()
            .filter { it.duracion > 0 && it.campo_afectado == "negocio" }

        // 2) Iteramos y ajustamos EstadoTurno.ingresos y EstadoTurno.costes
        efectos.forEach { effect ->
            var modificado = 0
            if (effect.tipo == "positivo" && effect.gastos == 0.0) {
                modificado = (EstadoTurno.ingresos * effect.ingresos / 100).toInt()
                // Incremento porcentual
                EstadoTurno.ingresos += modificado
            } else if (effect.tipo == "negativo" && effect.gastos == 0.0) {
                modificado = (EstadoTurno.ingresos * effect.ingresos / 100).toInt()
                // Decremento porcentual
                EstadoTurno.ingresos -= modificado
            }else if(effect.tipo == "positivo" && effect.ingresos == 0.0){
                modificado = (EstadoTurno.costes * effect.gastos / 100).toInt()
                EstadoTurno.costes   -= modificado
            }else if (effect.tipo == "negativo" && effect.ingresos == 0.0){
                modificado = (EstadoTurno.costes * effect.gastos / 100).toInt()
                EstadoTurno.costes   += modificado
            }
        }
        // Solo refrescamos si había algún efecto
        if (efectos.isNotEmpty() && players.isNotEmpty()) {
            refreshCurrentPlayerInMemory()
        }
    }

    /** Lógica de avance de día: al completar un ciclo completo de jugadores, sumamos 1 */
    private suspend fun gestionDia() {
        // Si acabamos de envolver al primer jugador (antes estábamos en el último)
        if (index == 0) {
            diaNum++


            var cont = 0
            players.forEach { jugador ->
                var dinerojugador = jugador.dinero + jugador.ingresos - jugador.gastos
                val actualizado = jugador.copy(dinero = dinerojugador)
                jugadorDao.update(actualizado)
                players[cont] = actualizado
                cont++
            }

            invComidaDao.decrementarDuracionYCantidadEnPartida(partidaId)
            invComidaDao.deleteExpiredInPartida(partidaId)

            // 2) Ahora procesamos TODOS los efectos de tarjetas UNA VEZ por día:
            //    (a) leemos todos en BD, (b) aplicamos, (c) reducimos duración o borramos.
            val efectos = db_turno.jugadorEfectoDao().getByJugador(EstadoTurno.idJugador).first()
            for (effect in efectos) {
                if (effect.duracion > 0) {
                    applyEffectTo(effect.fkJugador, effect)
                    val nuevoDur = effect.duracion - 1
                    if (nuevoDur > 0) {
                        db_turno.jugadorEfectoDao().update(effect.copy(duracion = nuevoDur))
                    } else {
                        db_turno.jugadorEfectoDao().delete(effect)
                        // 3) Además, RESTAR esa tarjeta de tu inventario:
                        invTarjetaDao.getByInventarioAndTarjeta(
                            EstadoTurno.inventarioId, effect.fkTarjeta
                        )?.let { reg ->
                            if (reg.cantidad > 1) {
                                invTarjetaDao.update(reg.copy(cantidad = reg.cantidad - 1))
                            } else {
                                invTarjetaDao.delete(reg)
                            }
                        }
                    }
                }
            }
            gestionarDuracionEfectos(invTarjetaDao,jugadorEfectosDao, jugadorDao)
        }
    }

    private suspend fun gestionarDuracionEfectos(invTarjetaDao : InventarioTarjetaDao, jugadorEfectosDao : JugadorEfectoDao, jugadorDao : JugadorDao){
        invTarjetaDao.decrementarDuracionYCantidadEnPartida(partidaId)
        invTarjetaDao.deleteExpiredInPartida(partidaId)

        jugadorEfectosDao.decrementarDuracionDeTodosEfectos()

        var cont = 0

        players.forEach { jugador ->
            val efectos = db_turno.jugadorEfectoDao()
                .getByJugador(jugador.id)
                .first()
                .filter { it.campo_afectado == "negocio" }
            var actualizado = jugador.copy()
            efectos.forEach { effect ->
                if (effect.fkJugador == jugador.id){
                    if (effect.duracion == 0){
                        if (effect.tipo == "positivo" && effect.gastos == 0.0) {
                            actualizado = jugador.copy(ingresos = jugador.ingresos - effect.valor_modificado.toDouble())
                        } else if (effect.tipo == "negativo" && effect.gastos == 0.0) {
                            actualizado = jugador.copy(ingresos = jugador.ingresos + effect.valor_modificado.toDouble())
                        }else if(effect.tipo == "positivo" && effect.ingresos == 0.0){
                            actualizado = jugador.copy(gastos = jugador.gastos + effect.valor_modificado.toDouble())
                        }else if (effect.tipo == "negativo" && effect.ingresos == 0.0){
                            actualizado = jugador.copy(gastos = jugador.gastos - effect.valor_modificado.toDouble())
                        }
                    }
                }

            }
            jugadorDao.update(actualizado)
            players[cont] = actualizado
            cont++
        }
        jugadorEfectosDao.eliminarEfectosExpirados()
        aplicarEfectosNegocioActivos()
    }

    /** Avanza al siguiente jugador y recarga EstadoTurno */
    suspend fun next() {
        if (players.isEmpty()) return
        players[index]
        cargarJugadoresLista()
        saveResumenTurnoActual()
        // Avanzamos índice de jugador (y ciclo)
        index = (index + 1) % players.size

        // Lleva la cuenta de los turnos totales
        turno++

        // Si acabamos de completar un ciclo completo, avanzamos el día
        gestionDia()

        // Recargamos el estado con el jugador/día/inventario actuales
        actualizarEstado()

    }

    /** Carga los IDs y el EstadoTurno desde los arrays según index y diaNum */
    private fun actualizarEstado() {
        val jugador : Jugador  = players[index]
        // diasPorJugador[index] tiene la lista de días: diaNum-1 es el índice
        val dia       = diasPorJugador[index].getOrNull(diaNum - 1)
            ?: error("No existe el día $diaNum para el jugador ${jugador.id}")
        val inventario = invsPorJugador[index]
        EstadoTurno.loadFrom(jugador, dia, inventario)
        playerId = jugador.id
        diaId    = dia.id
    }

    private suspend fun saveResumenTurnoActual(){

        val diaId      = EstadoTurno.diaNum
        val jugadorId = EstadoTurno.idJugador
        val dinero     = jugadorDao.getDineroById(idJugador)
        val ingresos   = jugadorDao.getIngresosById(idJugador)
        val gastos     = jugadorDao.getGastosById(idJugador)
        val inventarioId = EstadoTurno.inventarioId
        val turno = TurnoManager.turno


        // 1) Cuenta totales de negocios en este inventario
        val counts = invNegDao.countAllByInventario(inventarioId)      // List<ItemCount>
        val totalNegocios = counts.sumOf { it.count }

        // 2) ¿Ya existe un resumen para este día/jugador?
        val existente = resumenDiaDao.getResumen(diaId, jugadorId)

        // 3) Crea la entidad con o sin id (auto-generate cuando id=0)
        val resumen = ResumenDia(
            id         = existente?.id ?: 0,
            numDia      = diaId,
            fkJugador  = jugadorId,
            dinero     = dinero,
            negocios   = totalNegocios,
            ingresos   = ingresos,
            gastos     = gastos,
            turno      = turno + 1
        )

        // 4) Inserta o actualiza
        if (existente != null) {
            resumenDiaDao.update(resumen)
        } else {
            resumenDiaDao.insert(resumen)
        }
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
    fun update()   = viewModelScope.launch { dao.update(EstadoTurno.jugador) }
    fun delete(j: Jugador)   = viewModelScope.launch { dao.delete(j) }
}

class ComidaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).comidaDao()
    val allComidas: LiveData<List<Comida>> = dao.getAll()
    fun insert(c: Comida)     = viewModelScope.launch { dao.insert(c) }
    fun update(c: Comida)     = viewModelScope.launch { dao.update(c) }
    fun delete(c: Comida)     = viewModelScope.launch { dao.delete(c) }
    suspend fun getPrecioByName(name : String) = dao.getPrecioByName(name)
}

class TarjetaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).tarjetaDao()
    val allTarjetas: LiveData<List<Tarjeta>> = dao.getAll()
    fun insert(t: Tarjeta)    = viewModelScope.launch { dao.insert(t) }
    fun update(t: Tarjeta)    = viewModelScope.launch { dao.update(t) }
    fun delete(t: Tarjeta)    = viewModelScope.launch { dao.delete(t) }
    suspend fun getPrecioByName(name : String) = dao.getPrecioByName(name)

}

class NegocioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).negocioDao()
    val allNegocios: LiveData<List<Negocio>> = dao.getAll()
    fun insert(n: Negocio)    = viewModelScope.launch { dao.insert(n) }
    fun update(n: Negocio)    = viewModelScope.launch { dao.update(n) }
    fun delete(n: Negocio)    = viewModelScope.launch { dao.delete(n) }
    suspend fun getPrecioByName(name : String) = dao.getPrecioByName(name)
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
    private val db = AppDatabase.getInstance(application)
    private val dao = db.inventarioComidaDao()
    private val jugadorDao = db.jugadorDao()                 // nuevo

    /** Compra una unidad de comida, añade duración y descuenta precio */
    fun comprarComida(comida: Comida, precio: Int) = viewModelScope.launch {
        val invId = EstadoTurno.inventarioId                   // importe de EstadoTurno
        // 1) ¿ya existe?
        val existente = dao.getByInventarioAndComida(invId, comida.id)
        if (existente != null) {
            // 2a) actualizar cantidad
            val nuevaCantidad = existente.cantidad + 1
            dao.update(existente.copy(cantidad = existente.cantidad + 1,duracion = comida.duracion * nuevaCantidad))
        } else {
            // 2b) insertar nuevo
            dao.insert(
                InventarioComida(
                    fkInventario = invId,
                    fkComida = comida.id,
                    duracion = comida.duracion * 1,
                    cantidad = 1,
                )
            )
        }
        // Restar dinero y actualizar jugador
        dinero = dinero - comida.precio                         // EstadoTurno.dinero
        EstadoTurno.updateJugador()
        jugadorDao.update(EstadoTurno.jugador)

        // Refrescar jugador en memoria
        TurnoManager.refreshCurrentPlayerInMemory()
        val comidaDao : ComidaDao = db.comidaDao()
        comida.precio = comidaDao.getPrecioByName(comida.nombre)
    }

    // Exponer un StateFlow parametrizado por inventoryId:
    fun itemsFor(inventarioId: Long): StateFlow<List<InventarioComidaWithComida>> =
        dao.getConDetalle(inventarioId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val allItems: StateFlow<List<InventarioComida>> = dao
        .getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun upsert(item: InventarioComida) = viewModelScope.launch {
        dao.insert(item)
    }

    fun remove(item: InventarioComida) = viewModelScope.launch {
        dao.delete(item)
    }
    fun insert(ic: InventarioComida) = viewModelScope.launch { dao.insert(ic) }
    fun update(ic: InventarioComida) = viewModelScope.launch { dao.update(ic) }
    fun delete(ic: InventarioComida) = viewModelScope.launch { dao.delete(ic) }

    private val _counts = MutableStateFlow<Map<Long,Int>>(emptyMap())
    val comidaCounts: StateFlow<Map<Long,Int>> = _counts

    fun refreshAll(inventarioId: Long) = viewModelScope.launch {
        val list = dao.countAllByInventario(inventarioId)
        _counts.value = list.associate { it.itemId to it.count }
    }
}

class InventarioTarjetaViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val dao = db.inventarioTarjetaDao()                // :contentReference[oaicite:1]{index=1}
    private val jugadorDao = db.jugadorDao()                // nuevo



    fun duracionInventarioTarjeta(invTarjetaId: Long): Flow<Int> =
        dao
            .getDuracionById(invTarjetaId)
            .map { it ?: 0 }
            .flowOn(Dispatchers.IO)


    // Exponer un StateFlow parametrizado por inventoryId:
    fun itemsFor(inventarioId: Long): StateFlow<List<InventarioTarjetaWithTarjeta>> =
        dao.getConDetalle(inventarioId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Compra una tarjeta, la añade y descuenta su precio */
    fun comprarTarjeta(tarjeta: Tarjeta) = viewModelScope.launch {
        val invId = EstadoTurno.inventarioId
        // 1) ¿ya existe?
        val existente = dao.getByInventarioAndTarjeta(invId, tarjeta.id)
        if (existente != null) {
            // 2a) actualizar cantidad
            dao.update(existente.copy(cantidad = existente.cantidad + 1))
        } else {

            // 2b) insertar nuevo
            dao.insert(
                InventarioTarjeta(
                    fkInventario = invId,
                    fkTarjeta = tarjeta.id,
                    cantidad = 1,
                    duracion = 0
                )
            )
        }
        // Restar dinero según el precio mostrado (efectoValor)
        dinero = dinero - tarjeta.efectoValor
        EstadoTurno.updateJugador()
        jugadorDao.update(EstadoTurno.jugador)

        val tarjetaDao : TarjetaDao = db.tarjetaDao()
        tarjeta.efectoValor = tarjetaDao.getPrecioByName(tarjeta.nombre)
    }
    val allItems: StateFlow<List<InventarioTarjeta>> = dao
        .getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun upsert(item: InventarioTarjeta) = viewModelScope.launch {
        dao.insert(item)
    }

    fun remove(item: InventarioTarjeta) = viewModelScope.launch {
        dao.delete(item)
    }
    fun insert(inv: InventarioTarjeta) = viewModelScope.launch { dao.insert(inv) }
    fun update(inv: InventarioTarjeta) = viewModelScope.launch { dao.update(inv) }
    fun delete(inv: InventarioTarjeta) = viewModelScope.launch { dao.delete(inv) }

    private val _counts = MutableStateFlow<Map<Long,Int>>(emptyMap())
    val tarjetaCounts: StateFlow<Map<Long,Int>> = _counts

    fun refreshAll(inventarioId: Long) = viewModelScope.launch {
        val list = dao.countAllByInventario(inventarioId)
        _counts.value = list.associate { it.itemId to it.count }
    }
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
    fun getAllById(id : Long) : LiveData<List<PartidaJugador>> = dao.getByPartida(id)
}

class PositionsViewModel(application: Application) : AndroidViewModel(application) {
    private val db                 = AppDatabase.getInstance(application)
    private val partidaJugadorDao  = db.partidaJugadorDao()

    /** Debes fijar esta propiedad antes de observar `playersInGame` */
    private val _partidaId = MutableLiveData<Long>()
    fun setPartidaId(id: Long) { _partidaId.value = id }

    /** LiveData con solo los jugadores de esa partida */
    val playersInGame: LiveData<List<Jugador>> =
        _partidaId.switchMap { pid ->
            partidaJugadorDao.getJugadoresForPartida(pid)
        }
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

            val existMes : Boolean = mesDao.existsByNumero(1)
            var mesId = 1L

            if (!existMes){
                mesId = mesDao.insert(Mes(numero = 1))
            }

            // 3) Por cada jugador, creamos todo lo necesario
            playerNames.forEach { nombre ->
                // a) Jugador
                val jugadorId = jugadorDao.insert(
                    Jugador(nombre = nombre, dinero = 999999.0, ingresos = 0.0, gastos = 0.0, ingresosOriginal = 0, gastosOriginal = 0)
                )
                // Se aplica el id a los datos
                PartidaDatos.aplicarid(jugadorId)
                // b) Relación partida–jugador
                partidaJugadorDao.insert(
                    PartidaJugador(fkPartida = partidaId, fkJugador = jugadorId)
                )
                // c) Inventario vacío para este jugador
                val inventarioId = inventarioDao.insert(
                    Inventario(fkJugador = jugadorId, fkPartida = partidaId)
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


class JugadorEfectoViewModel(application: Application) : AndroidViewModel(application) {
    private val db                = AppDatabase.getInstance(application)
    private val dao        = db.jugadorEfectoDao()
    private val tarjetaDao = db.tarjetaDao()
    private val invTarjetaDao = db.inventarioTarjetaDao()
    private var lastExchangedCard: Tarjeta? = null

    fun getLastExchangedCard(): Tarjeta? = lastExchangedCard
    val allEfectos: LiveData<List<JugadorEfecto>> = dao.getAll()




    fun insert(je: JugadorEfecto) = viewModelScope.launch { dao.insert(je) }
    fun update(je: JugadorEfecto) = viewModelScope.launch { dao.update(je) }
    fun delete(je: JugadorEfecto) = viewModelScope.launch { dao.delete(je) }
    fun efectosPorJugador(id: Long) = dao.getByJugador(id)


    /**
     * Reemplaza la tarjeta indicada por una nueva elegida aleatoriamente
     * entre todas las de la tabla `tarjeta` (excluyendo las 3 primeras).
     * Borra la entrada antigua de `inventario_tarjeta`, añade (o actualiza)
     * la del nuevo id de tarjeta, y registra un efecto con todos los campos a cero.
     */
    fun reemplazarTarjeta(oldTarjeta: Tarjeta) = viewModelScope.launch {
        val invId     = EstadoTurno.inventarioId
        val jugadorId = EstadoTurno.idJugador

        // 1) Ajustamos la cantidad de la tarjeta antigua en el inventario:
        invTarjetaDao.getByInventarioAndTarjeta(invId, oldTarjeta.id)?.let { registro ->
            if (registro.cantidad > 1) {
                // Si había más de 1, restamos uno
                invTarjetaDao.update(
                    registro.copy(cantidad = registro.cantidad - 1)
                )
            } else {
                // Si era la última, la borramos
                invTarjetaDao.delete(registro)
            }

        }



        // 2) Todas las tarjetas “jugables” (excluye las 3 primeras)
        val todasDisponibles = tarjetaDao.getExceptFirst3()
        if (todasDisponibles.isEmpty()) return@launch

        // 3) IDs de tarjetas que ya tienes
        val ownedIds = invTarjetaDao
            .getConDetalle(invId)              // Flow<List<InventarioTarjetaWithTarjeta>>
            .first()                           // primera emisión
            .map { it.tarjeta.id }             // lista de fk_tarjeta actuales

        // 4) Filtrar las que ya tienes
        val filtradas = todasDisponibles
            .filterNot { it.id in ownedIds }
        if (filtradas.isEmpty()) return@launch

        // 3) Determinamos el modo según el nombre de la tarjeta antigua
        val modo = when {
            oldTarjeta.nombre.contains("Negocio", ignoreCase = true)  -> "negocio"
            oldTarjeta.nombre.contains("Dinero",   ignoreCase = true)  -> "dinero"
            else                                                       -> "aleatoria"
        }

        val pool = when (modo) {
            "negocio"   -> filtradas.filter { it.tipoTarjeta.equals("negocio", true) }
            "dinero"    -> filtradas.filter { it.tipoTarjeta.equals("dinero",   true) }
            else        -> filtradas
        }
        if (pool.isEmpty()) return@launch

// 6) Escoger al azar y añadir al inventario
        val nueva = pool.random()
        lastExchangedCard = nueva
        if (nueva.tipoTarjeta == "negocio"){
            invTarjetaDao.insert(
                InventarioTarjeta(fkInventario = invId, fkTarjeta = nueva.id, cantidad = 1, duracion = 2)
            )
        }else{
            invTarjetaDao.insert(
                InventarioTarjeta(fkInventario = invId, fkTarjeta = nueva.id, cantidad = 1, duracion = 0)
            )
        }


        // 7) Preparamos los campos del efecto
        val tipoEfecto   = if (nueva.tipoEfecto.equals("Positivo", true)) "positivo" else "negativo"
        val campo        = if (nueva.tipoTarjeta.equals("negocio", true)) "negocio" else "dinero"
        val valor        = nueva.efectoValor.toDouble()
        val cantidad     = 1

        // Duración basada en el tipo de la tarjeta NUEVA:
        // - dinero: cero (efecto inmediato)
        // - negocio y demás: 2 turnos
        val duracion = if (nueva.tipoTarjeta.equals("dinero", true)) 0 else 2

        // 8) Función auxiliar para crear el efecto
        fun crearEfectoPara(jugadorDestino: Long) : JugadorEfecto{


            val jugadorefecto = JugadorEfecto(
                fkJugador      = jugadorDestino,
                fkTarjeta      = nueva.id,
                tipo           = tipoEfecto,
                campo_afectado = campo,
                ingresos       = when {
                    nueva.tipoTarjeta.equals("dinero", true)             -> valor
                    nueva.queModifica.equals("ingresos", true)           -> valor
                    else                                                  -> 0.0
                },
                gastos         = if (
                    nueva.tipoTarjeta.equals("negocio", true)
                    && nueva.queModifica.equals("costes", true)
                ) valor else 0.0,
                cantidad       = cantidad,
                duracion       = duracion,
                valor_modificado = 0
            )
            comprobarEfectos(jugadorefecto)
            return jugadorefecto

        }



        if (nueva.dirigidoA.equals("Propio", true)) {
            val jugador = db.jugadorDao().getById(jugadorId)
            val effect = crearEfectoPara(jugadorId)

            // Calcula aquí el valor_modificado según el campo y tipo
            val valorModificado = when (campo) {
                "dinero" -> {
                    // si fuese una cantidad fija:
                    effect.ingresos.toInt()  // o gastos.toInt() si es gasto
                }
                "negocio" -> {
                    if (effect.tipo == "positivo" && effect.gastos == 0.0) {
                        // % de ingresos
                        ((jugador.ingresos * effect.ingresos/100)).toInt()
                    } else if (tipoEfecto == "negativo" && effect.gastos == 0.0) {
                        ((jugador.ingresos * effect.ingresos/100)).toInt()
                    } else if (tipoEfecto == "positivo" && effect.ingresos == 0.0) {
                        ((jugador.gastos    * effect.gastos/100)).toInt()
                    } else /* negativo coste */ {
                        ((jugador.gastos    * effect.gastos/100)).toInt()
                    }
                }
                else -> 0
            }

            val effect_con_valor = effect.copy(valor_modificado = valorModificado)
            dao.insert(effect_con_valor)                                  // persisto el efecto             // si es inmediato
            applyEffectTo(jugadorId, effect_con_valor)                 // aplico y persisto
        } else {
            TurnoManager.players
                .map { it.id }
                .filter { it != jugadorId }
                .forEach { otherId ->
                    if (otherId != jugadorId){
                        val jugador = db.jugadorDao().getById(otherId)
                        val effect = crearEfectoPara(otherId)

                        // Calcula aquí el valor_modificado según el campo y tipo
                        val valorModificado = when (campo) {
                            "dinero" -> {
                                // si fuese una cantidad fija:
                                effect.ingresos.toInt()  // o gastos.toInt() si es gasto
                            }
                            "negocio" -> {
                                if (effect.tipo == "positivo" && effect.gastos == 0.0) {
                                    // % de ingresos
                                    ((jugador.ingresos * effect.ingresos/100)).toInt()
                                } else if (tipoEfecto == "negativo" && effect.gastos == 0.0) {
                                    ((jugador.ingresos * effect.ingresos/100)).toInt()
                                } else if (tipoEfecto == "positivo" && effect.ingresos == 0.0) {
                                    ((jugador.gastos    * effect.gastos/100)).toInt()
                                } else /* negativo coste */ {
                                    ((jugador.gastos    * effect.gastos/100)).toInt()
                                }
                            }
                            else -> 0
                        }

                        val effect_con_valor = effect.copy(valor_modificado = valorModificado)
                        dao.insert(effect_con_valor)                                  // persisto el efecto             // si es inmediato
                        applyEffectTo(otherId, effect_con_valor)                 // aplico y persisto
                    }
                }
        }
    }



    fun comprobarEfectos(nueva : JugadorEfecto){
        if (nueva.tipo == "positivo" && nueva.campo_afectado == "negocio"){
            EstadoTurno.ingresos = EstadoTurno.ingresos + (EstadoTurno.ingresos*nueva.ingresos/100).toInt()
            EstadoTurno.costes = EstadoTurno.costes + (EstadoTurno.costes*nueva.gastos/100).toInt()
        }else if(nueva.tipo == "negativo" && nueva.campo_afectado == "negocio"){
            EstadoTurno.ingresos = EstadoTurno.ingresos - (EstadoTurno.ingresos*nueva.ingresos/100).toInt()
            EstadoTurno.costes = EstadoTurno.costes - (EstadoTurno.costes*nueva.gastos/100).toInt()
        }else if (nueva.tipo == "positivo" && nueva.campo_afectado == "dinero"){
            EstadoTurno.dinero = EstadoTurno.dinero + nueva.ingresos.toInt()
        }else if (nueva.tipo == "negativo" && nueva.campo_afectado == "dinero"){
            EstadoTurno.dinero = EstadoTurno.dinero - nueva.ingresos.toInt()
        }
    }
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

class InventarioNegocioViewModel(application: Application) : AndroidViewModel(application) {
    private val db                 = AppDatabase.getInstance(application)
    private val dao                = db.inventarioNegocioDao()
    private val jugadorDao = db.jugadorDao()            // 📌

    /** StateFlow con todos los negocios en inventario */
    val allItems: StateFlow<List<InventarioNegocio>> = dao
        .getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Inserta o actualiza un registro */
    fun upsert(item: InventarioNegocio) = viewModelScope.launch {
        dao.insert(item)
    }

    /** Elimina un registro */
    fun remove(item: InventarioNegocio) = viewModelScope.launch {
        dao.delete(item)
    }



    /** Operaciones básicas */
    fun insert(inv: InventarioNegocio)   = viewModelScope.launch { dao.insert(inv) }
    fun update(inv: InventarioNegocio)   = viewModelScope.launch { dao.update(inv) }
    fun delete(inv: InventarioNegocio)   = viewModelScope.launch { dao.delete(inv) }

    // Exponer un StateFlow parametrizado por inventoryId:
    fun itemsFor(inventarioId: Long): StateFlow<List<InventarioNegocioWithNegocio>> =
        dao.getConDetalle(inventarioId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Compra un negocio: si ya está en inventario incrementa cantidad, si no lo inserta */
    fun comprarNegocio(negocio: Negocio) = viewModelScope.launch {
        val invId = EstadoTurno.inventarioId
        // 1) ¿ya existe?
        val existente = dao.getByInventarioAndNegocio(invId, negocio.id)
        if (existente != null) {
            // 2a) actualizar cantidad
            dao.update(existente.copy(cantidad = existente.cantidad + 1))
        } else {
            // 2b) insertar nuevo
            dao.insert(
                InventarioNegocio(
                    fkInventario = invId,
                    fkNegocio    = negocio.id,
                    cantidad     = 1
                )
            )
        }
        dinero = dinero - negocio.costeTienda.toInt()
        EstadoTurno.updateJugador()

        // 3) Graba el Jugador en BD *con los nuevos valores*
        jugadorDao.update(EstadoTurno.jugador)
        TurnoManager.refreshCurrentPlayerInMemory()               // <-- nuevo
        val negocioDao : NegocioDao = db.negocioDao()
        negocio.costeTienda = negocioDao.getPrecioByName(negocio.nombre)
    }

    private val _counts = MutableStateFlow<Map<Long,Int>>(emptyMap())
    val negocioCounts: StateFlow<Map<Long,Int>> = _counts

    /** Recarga el mapa (negocioId -> cantidad) para todo el inventario */
    fun refreshAll(inventarioId: Long) = viewModelScope.launch {
        val list = dao.countAllByInventario(inventarioId)
        _counts.value = list.associate { it.itemId to it.count }
    }

}

class ResumenDiaViewModel(application: Application) : AndroidViewModel(application) {
    private val db                 = AppDatabase.getInstance(application)

    private val dao = db.resumenDiaDao()
    private val invNegDao  = db.inventarioNegocioDao()


    fun applyResumen(dia : Int){
        var resumen = dao.getResumenLive(dia, idJugador)
        if (resumen.isInitialized){
            Resumen.id = resumen.value?.id!!
            Resumen.fk_jugador = resumen.value?.fkJugador!!
            numDia = resumen.value?.numDia!!
            Resumen.dinero = resumen.value?.dinero!!.toInt()
            Resumen.negocios = resumen.value?.negocios!!
            Resumen.ingresos = resumen.value?.ingresos!!.toInt()
            Resumen.gastos = resumen.value?.gastos!!.toInt()
            Resumen.turno = resumen.value?.turno!!
        }else{
            Resumen.id = 0
            Resumen.fk_jugador = 0
            numDia = 0
            Resumen.dinero = 0
            Resumen.negocios = 0
            Resumen.ingresos = 0
            Resumen.gastos = 0
            Resumen.turno = 0
        }
    }

    /** LiveData con el resumen del día actual para el jugador */
    fun getResumen(numDia : Int, jugadorId: Long): LiveData<ResumenDia?> =
        dao.getResumenLive(numDia, jugadorId)

    /** Inserta un nuevo resumen */
    fun insert(resumen: ResumenDia) = viewModelScope.launch {
        dao.insert(resumen)
    }

    /** Actualiza un resumen existente */
    fun update(resumen: ResumenDia) = viewModelScope.launch {
        dao.update(resumen)
    }

    /** Elimina un resumen */
    fun delete(resumen: ResumenDia) = viewModelScope.launch {
        dao.delete(resumen)
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
    private val inventarioDao      = db.inventarioDao()
    private val invComidaDao       = db.inventarioComidaDao()

    // Nuevo LiveData con el % de descuento de comida
    private val _descuentoComida = MutableLiveData(0)
    val descuentoComida: LiveData<Int> = _descuentoComida


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


    /** Para recuperar síncrono el inventario en una coroutine */
    suspend fun getInventarioSync(jugadorId: Long) =
        inventarioDao.getByPlayerSync(jugadorId)

    /** Encapsula el countByNombre del DAO */
    suspend fun countComidaEnInventario(inventarioId: Long, nombre: String): Int =
        invComidaDao.countByNombre(inventarioId, nombre)

    /**
     * Genera una nueva tienda para el jugador indicado y día dado.
     * Selecciona 3 negocios aleatorios de cada categoría: baja, media, alta.
     * Inserta las relaciones en tienda_negocio y expone la tiendaId.
     */



    fun generarTiendaNueva(jugadorId: Long, diaId: Long) = viewModelScope.launch {
        if (diaId <= 0 || jugadorId <= 0) return@launch

        // 1) Calculamos descuento por comida
        val inventario = inventarioDao.getByPlayerSync(jugadorId)
        val detalles   = invComidaDao
            .getConDetalle(inventario.id)
            .first()  // primera emisión del Flow
            .filter { it.invComida.duracion > 0 }
        val efectoTotal = detalles.sumOf { it.comida.efecto }  // porcentaje acumulado
        _descuentoComida.postValue(efectoTotal)

        // 2) Factor de ajuste
        val factor = 1 - (efectoTotal.toDouble() / 100)

        db.withTransaction {
            // 3) Creamos la tienda
            val newTiendaId = tiendaDao.insert(Tienda(fkJugador=jugadorId, fkDia=diaId))
            _tiendaId.postValue(newTiendaId)

            // 4) Obtenemos negocios y ajustamos precio
            val bajos  = negocioDao.getByCategoria("Baja")
                .shuffled()
                .map { it.copy(costeTienda = it.costeTienda * factor) }
                .take(3)
            val medios = negocioDao.getByCategoria("Media")
                .shuffled()
                .map { it.copy(costeTienda = it.costeTienda * factor) }
                .take(3)
            val altos  = negocioDao.getByCategoria("Alta")
                .shuffled()
                .map { it.copy(costeTienda = it.costeTienda * factor) }
                .take(3)

            // 5) Insertamos relaciones
            (bajos + medios + altos).forEach { negocio ->
                tiendaNegocioDao.insert(
                    TiendaNegocio(fkTienda=newTiendaId, fkNegocio=negocio.id)
                )
            }
        }
    }
    /** Recalcula siempre todo el descuento (sin necesitar el objeto Comida) */
    fun actualizarDescuento(jugadorId: Long) = viewModelScope.launch {
        val inv = inventarioDao.getByPlayerSync(jugadorId)
        val detalles = invComidaDao.getConDetalle(inv.id)
            .first()
            .filter { it.invComida.duracion > 0 }
        val efectoTotal = detalles.sumOf { it.comida.efecto }
        _descuentoComida.postValue(efectoTotal)
    }
    fun aplicarDescuento(precioOriginal : Double, descuento : Int) : Double{
        var precioAplicado : Double = precioOriginal - (precioOriginal*descuento/100)
        return precioAplicado
    }
}





