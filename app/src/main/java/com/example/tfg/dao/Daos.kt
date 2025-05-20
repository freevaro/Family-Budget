package com.example.tfg.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfg.entity.*
import androidx.room.withTransaction

@Dao
interface MesDao {
    @Query("SELECT * FROM mes") fun getAll(): LiveData<List<Mes>>
    @Insert suspend fun insert(m: Mes) : Long
    @Update suspend fun update(m: Mes)
    @Delete suspend fun delete(m: Mes)
}

@Dao
interface DiaDao {
    @Query("SELECT * FROM dia") fun getAll(): LiveData<List<Dia>>
    @Insert suspend fun insert(d: Dia) : Long
    @Update suspend fun update(d: Dia)
    @Delete suspend fun delete(d: Dia)
    /**
     * Obtiene el día de un jugador en un mes concreto.
     * Asume que ya existe solo uno por jugador/mes.
     */
    @Query("""
      SELECT * 
        FROM dia 
       WHERE fk_jugador = :jugadorId 
         AND fk_mes     = :mesId
       LIMIT 1
    """)
    suspend fun getDiaByJugadorAndMes(
        jugadorId: Long,
        mesId: Long
    ): Dia
}

@Dao
interface JugadorDao {
    @Query("SELECT * FROM jugador") fun getAll(): LiveData<List<Jugador>>
    @Insert suspend fun insert(j: Jugador) : Long
    @Update suspend fun update(j: Jugador)
    @Delete suspend fun delete(j: Jugador)
    /** Devuelve la lista de jugadores que participan en la partida dada */
    @Query("""
    SELECT j.* 
      FROM jugador j 
      INNER JOIN partida_jugador pj 
        ON j.id = pj.fk_jugador 
     WHERE pj.fk_partida = :partidaId
  """)
    suspend fun getPlayersForPartida(partidaId: Long): MutableList<Jugador>
}

@Dao
interface ComidaDao {
    @Query("SELECT * FROM comida") fun getAll(): LiveData<List<Comida>>
    @Insert suspend fun insert(c: Comida)
    @Update suspend fun update(c: Comida)
    @Delete suspend fun delete(c: Comida)
}

@Dao
interface TarjetaDao {
    @Query("SELECT * FROM tarjeta") fun getAll(): LiveData<List<Tarjeta>>
    @Insert suspend fun insert(t: Tarjeta)
    @Update suspend fun update(t: Tarjeta)
    @Delete suspend fun delete(t: Tarjeta)
    @Query("SELECT * FROM tarjeta ORDER BY id ASC LIMIT 3")
    fun getFirst3(): LiveData<List<Tarjeta>>
}

@Dao
interface NegocioDao {
    @Query("SELECT * FROM negocio") fun getAll(): LiveData<List<Negocio>>
    @Insert suspend fun insert(n: Negocio)
    @Update suspend fun update(n: Negocio)
    @Delete suspend fun delete(n: Negocio)
    @Query("SELECT * FROM negocio WHERE categoria = :categoria")
    suspend fun getByCategoria(categoria: String): List<Negocio>
}

@Dao
interface TiendaDao {
    @Query("SELECT * FROM tienda") fun getAll(): LiveData<List<Tienda>>
    @Insert suspend fun insert(t: Tienda) : Long
    @Update suspend fun update(t: Tienda)
    @Delete suspend fun delete(t: Tienda)
    @Query("SELECT EXISTS(SELECT 1 FROM tienda WHERE fk_dia = :diaId)")
    suspend fun existeTiendaParaDia(diaId: Long): Boolean
    @Query("SELECT COUNT(*) FROM tienda")
    suspend fun countAll(): Int


}

@Dao
interface InventarioDao {
    @Query("SELECT * FROM inventario") fun getAll(): LiveData<List<Inventario>>
    @Insert suspend fun insert(i: Inventario)
    @Update suspend fun update(i: Inventario)
    @Delete suspend fun delete(i: Inventario)
    /**
     * Obtiene el inventario asociado a un jugador.
     * Asume que hay uno solo por jugador.
     */
    @Query("""
      SELECT * 
        FROM inventario 
       WHERE fk_jugador = :jugadorId
       LIMIT 1
    """)
    suspend fun getByPlayerSync(jugadorId: Long): Inventario
}

@Dao
interface InventarioComidaDao {
    @Query("SELECT * FROM inventario_comida") fun getAll(): LiveData<List<InventarioComida>>
    @Insert suspend fun insert(ic: InventarioComida)
    @Update suspend fun update(ic: InventarioComida)
    @Delete suspend fun delete(ic: InventarioComida)
}

@Dao
interface InventarioTarjetaDao {
    @Query("SELECT * FROM inventario_tarjeta") fun getAll(): LiveData<List<InventarioTarjeta>>
    @Insert suspend fun insert(it: InventarioTarjeta)
    @Update suspend fun update(it: InventarioTarjeta)
    @Delete suspend fun delete(it: InventarioTarjeta)
}

@Dao
interface InventarioNegocioDao {

    /** Inserta un registro y devuelve su ID generado. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventarioNegocio: InventarioNegocio): Long

    /** Actualiza un registro existente. */
    @Update
    suspend fun update(inventarioNegocio: InventarioNegocio)

    /** Borra un registro concreto. */
    @Delete
    suspend fun delete(inventarioNegocio: InventarioNegocio)

    /** Obtiene todos los negocios de un inventario específico. */
    @Query("SELECT * FROM inventario_negocio WHERE fk_inventario = :inventarioId")
    suspend fun getByInventario(inventarioId: Long): List<InventarioNegocio>

    /** Obtiene todos los registros de negocio de todos los inventarios. */
    @Query("SELECT * FROM inventario_negocio")
    suspend fun getAll(): List<InventarioNegocio>
}

@Dao
interface PartidaDao {
    @Query("SELECT * FROM partida") fun getAll(): LiveData<List<Partida>>
    @Insert suspend fun insert(p: Partida) : Long
    @Update suspend fun update(p: Partida)
    @Delete suspend fun delete(p: Partida)
}

@Dao
interface PartidaJugadorDao {
    @Query("SELECT * FROM partida_jugador") fun getAll(): LiveData<List<PartidaJugador>>
    @Insert suspend fun insert(pj: PartidaJugador)
    @Update suspend fun update(pj: PartidaJugador)
    @Delete suspend fun delete(pj: PartidaJugador)
}

@Dao
interface PartidaDiaDao {
    @Query("SELECT * FROM partida_dia") fun getAll(): LiveData<List<PartidaDia>>
    @Insert suspend fun insert(pd: PartidaDia)
    @Update suspend fun update(pd: PartidaDia)
    @Delete suspend fun delete(pd: PartidaDia)
}

@Dao
interface TiendaNegocioDao {

    /** Inserta un nuevo registro tienda–negocio y devuelve su ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TiendaNegocio): Long

    /** Actualiza un registro existente */
    @Update
    suspend fun update(item: TiendaNegocio)

    /** Borra un registro */
    @Delete
    suspend fun delete(item: TiendaNegocio)

    /** Devuelve la lista de relaciones para una tienda concreta */
    @Query("SELECT * FROM tienda_negocio WHERE fk_tienda = :tiendaId")
    fun getByTienda(tiendaId: Long): LiveData<List<TiendaNegocio>>

    /** Devuelve directamente los objetos Negocio para esa tienda */
    @Query("SELECT n.* FROM negocio n INNER JOIN tienda_negocio tn ON n.id = tn.fk_negocio WHERE tn.fk_tienda = :tiendaId")
    fun getNegociosForTienda(tiendaId: Long): LiveData<List<Negocio>>
}




