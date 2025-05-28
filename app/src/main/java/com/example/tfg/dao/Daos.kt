package com.example.tfg.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfg.entity.*
import com.example.tfg.views.ItemCount
import kotlinx.coroutines.flow.Flow

@Dao
interface MesDao {
    @Query("SELECT * FROM mes") fun getAll(): LiveData<List<Mes>>
    @Insert suspend fun insert(m: Mes) : Long
    @Update suspend fun update(m: Mes)
    @Delete suspend fun delete(m: Mes)
    @Query("SELECT EXISTS(SELECT 1 FROM mes WHERE numero = :numero)")
    suspend fun existsByNumero(numero: Int): Boolean
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

    /**
     * Obtiene la lista de días de un jugador para un mes determinado, ordenados por número de día.
     * @param jugadorId ID del jugador.
     * @param mesId ID del mes.
     */
    @Query("""
        SELECT *
        FROM dia
        WHERE fk_jugador = :jugadorId
          AND fk_mes = :mesId
        ORDER BY numero_dia
    """ )
    suspend fun getDiasByJugadorAndMes(jugadorId: Long, mesId: Long): List<Dia>

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
    // Primero, en JugadorDao (si no existe), para poder cargar un jugador por ID:
    @Query("SELECT * FROM jugador WHERE id = :id")
    suspend fun getById(id: Long): Jugador
}

@Dao
interface ComidaDao {
    @Query("SELECT * FROM comida") fun getAll(): LiveData<List<Comida>>
    @Insert suspend fun insert(c: Comida)
    @Update suspend fun update(c: Comida)
    @Delete suspend fun delete(c: Comida)
    @Query("SELECT precio FROM comida WHERE nombre = :nombre") suspend fun getPrecioByName(nombre : String) : Int
}

@Dao
interface TarjetaDao {
    @Query("SELECT * FROM tarjeta") fun getAll(): LiveData<List<Tarjeta>>
    @Insert suspend fun insert(t: Tarjeta)
    @Update suspend fun update(t: Tarjeta)
    @Delete suspend fun delete(t: Tarjeta)
    @Query("SELECT * FROM tarjeta ORDER BY id ASC LIMIT 3")
    fun getFirst3(): LiveData<List<Tarjeta>>
    /** Todas las tarjetas excepto las 3 primeras */
    @Query("""
      SELECT * 
        FROM tarjeta 
       WHERE id NOT IN (
         SELECT id 
           FROM tarjeta 
          ORDER BY id ASC 
          LIMIT 3
       )
    """)
    suspend fun getExceptFirst3(): List<Tarjeta>
    @Query("SELECT efecto_valor FROM tarjeta WHERE nombre = :nombre") suspend fun getPrecioByName(nombre : String) : Int

}

@Dao
interface NegocioDao {
    @Query("SELECT * FROM negocio") fun getAll(): LiveData<List<Negocio>>
    @Insert suspend fun insert(n: Negocio)
    @Update suspend fun update(n: Negocio)
    @Delete suspend fun delete(n: Negocio)
    @Query("SELECT * FROM negocio WHERE categoria = :categoria")
    suspend fun getByCategoria(categoria: String): List<Negocio>
    @Query("SELECT * FROM negocio WHERE id = :id")
    suspend fun getById(id: Long): Negocio?
    @Query("SELECT coste_tienda FROM negocio WHERE nombre = :nombre") suspend fun getPrecioByName(nombre : String) : Double

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

    /**
     * Obtiene el inventario principal de un jugador.
     * @param jugadorId ID del jugador.
     */
    @Query("""
        SELECT *
        FROM inventario
        WHERE fk_partida = :partidaId
    """ )
    suspend fun getInventarioByJugador(partidaId: Long): List<Inventario>
}

@Dao
interface InventarioComidaDao {
    @Query("SELECT id, fk_inventario, fk_comida, duracion, cantidad FROM inventario_comida")
    fun getAll(): Flow<List<InventarioComida>>
    @Insert suspend fun insert(ic: InventarioComida)
    @Update suspend fun update(ic: InventarioComida)
    @Delete suspend fun delete(ic: InventarioComida)
    @Query("""
    SELECT fk_comida AS itemId, COUNT(*) AS count
      FROM inventario_comida
     WHERE fk_inventario = :inventarioId
     GROUP BY fk_comida
  """)
    suspend fun countAllByInventario(inventarioId: Long): List<ItemCount>

    /**
     * Resta 1 al contador de días (duracion) de cada comida
     * y, si ese nuevo duracion coincide con un múltiplo exacto
     * de la duracion base de la comida, reduce cantidad en 1.
     */
    @Query("""
    UPDATE inventario_comida
       SET 
         duracion = duracion - 1,
         cantidad = CASE
           WHEN ( (duracion - 1) 
                  % (SELECT duracion 
                       FROM comida 
                      WHERE comida.id = inventario_comida.fk_comida)
                ) = 0 
           THEN cantidad - 1
           ELSE cantidad
         END
     WHERE fk_inventario IN (
       SELECT id 
         FROM inventario 
        WHERE fk_partida = :partidaId
     )
       AND cantidad > 0
  """)
    suspend fun decrementarDuracionYCantidadEnPartida(partidaId: Long)

    /**
     * Borra todas las comidas de la partida cuya duración o cantidad ha llegado a cero
     */
    @Query("""
      DELETE FROM inventario_comida
       WHERE fk_inventario IN (
         SELECT id 
           FROM inventario 
          WHERE fk_partida = :partidaId
       )
         AND (duracion <= 0 OR cantidad <= 0)
    """)
    suspend fun deleteExpiredInPartida(partidaId: Long)


    // JOIN entre inventario_negocio y negocio:
    @Transaction
    @Query("SELECT * FROM inventario_comida WHERE fk_inventario = :inventarioId")
    fun getConDetalle(inventarioId: Long): Flow<List<InventarioComidaWithComida>>

    /** Devuelve el número de filas que coinciden (0 o más) */
    @Query("""
    SELECT COUNT(*) 
      FROM inventario_comida ic
           JOIN comida c
             ON ic.fk_comida = c.id
     WHERE ic.fk_inventario = :inventarioId
       AND c.nombre         = :nombre
  """)
    suspend fun countByNombre(
        inventarioId: Long,
        nombre: String
    ): Int

    @Query("SELECT cantidad FROM inventario_comida WHERE fk_comida = :fk_comida")
    suspend fun selectCantidad(fk_comida : Long)  : Int

    /** Devuelve la fila si ya existe esa relación inventario–negocio */
    @Query("""
    SELECT * 
      FROM inventario_comida
     WHERE fk_inventario = :inventarioId 
       AND fk_comida    = :fk_comida
     LIMIT 1
  """)
    suspend fun getByInventarioAndComida(
        inventarioId: Long,
        fk_comida: Long
    ): InventarioComida?

}

@Dao
interface InventarioTarjetaDao {
    /** Inserta o reemplaza un registro y devuelve su ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventarioTarjeta: InventarioTarjeta): Long

    /** Actualiza un registro existente */
    @Update
    suspend fun update(inventarioTarjeta: InventarioTarjeta)

    /** Borra un registro concreto */
    @Delete
    suspend fun delete(inventarioTarjeta: InventarioTarjeta)

    /** Obtiene todos los negocios de un inventario específico como LiveData */
    @Query("SELECT * FROM inventario_tarjeta WHERE fk_inventario = :inventarioId")
    fun getByInventario(inventarioId: Long): LiveData<List<InventarioTarjeta>>

    @Query("SELECT id, fk_inventario, fk_tarjeta, cantidad, duracion FROM inventario_tarjeta")
    fun getAll(): Flow<List<InventarioTarjeta>>



    @Query("""
    SELECT fk_tarjeta AS itemId, COUNT(*) AS count
      FROM inventario_tarjeta
     WHERE fk_inventario = :inventarioId
     GROUP BY fk_tarjeta
  """)
    suspend fun countAllByInventario(inventarioId: Long): List<ItemCount>

    // JOIN entre inventario_negocio y negocio:
    @Transaction
    @Query("SELECT * FROM inventario_tarjeta WHERE fk_inventario = :inventarioId")
    fun getConDetalle(inventarioId: Long): Flow<List<InventarioTarjetaWithTarjeta>>

    @Query("SELECT cantidad FROM inventario_tarjeta WHERE fk_tarjeta = :fk_tarjeta")
    suspend fun selectCantidad(fk_tarjeta : Long) : Int

    /** Devuelve la fila si ya existe esa relación inventario–negocio */
    @Query("""
    SELECT * 
      FROM inventario_tarjeta
     WHERE fk_inventario = :inventarioId 
       AND fk_tarjeta    = :fk_tarjeta
     LIMIT 1
  """)
    suspend fun getByInventarioAndTarjeta(
        inventarioId: Long,
        fk_tarjeta: Long
    ): InventarioTarjeta?

    /**
     * Resta 1 al contador de días (duracion) de cada tarjeta
     * y, si llega a cero, reduce cantidad en 1.
     */
    @Query("""
    UPDATE inventario_tarjeta
       SET
         duracion = duracion - 1,
         cantidad = CASE
           WHEN (duracion - 1) = 0 THEN cantidad - 1
           ELSE cantidad
         END
     WHERE fk_inventario IN (
       SELECT id
         FROM inventario
        WHERE fk_partida = :partidaId
     )
       AND duracion > 0
  """)
    suspend fun decrementarDuracionYCantidadEnPartida(partidaId: Long)

    /**
     * Elimina tarjetas caducadas (duracion o cantidad <= 0),
     * excepto las de tipo fijo (IDs 1,2,3) y las de efecto Dinero.
     */
    @Query("""
    DELETE FROM inventario_tarjeta
     WHERE fk_inventario IN (
       SELECT id
         FROM inventario
        WHERE fk_partida = :partidaId
     )
       AND (duracion <= 0 OR cantidad <= 0)
       AND fk_tarjeta NOT IN (1,2,3)
       AND fk_tarjeta NOT IN (
         SELECT id
           FROM tarjeta
          WHERE tipo_efecto = 'Dinero'
       )
  """)
    suspend fun deleteExpiredInPartida(partidaId: Long)

    /** Devuelve la duración de un inventario_tarjeta concreto */
    @Query("SELECT duracion FROM inventario_tarjeta WHERE id = :inventarioTarjetaId")
    fun getDuracionById(inventarioTarjetaId: Long): Flow<Int?>

}


@Dao
interface JugadorEfectoDao {
    @Query("SELECT * FROM jugador_efectos")
    fun getAll(): LiveData<List<JugadorEfecto>>

    @Query("SELECT * FROM jugador_efectos WHERE fk_jugador = :jugadorId")
    fun getByJugador(jugadorId: Long): Flow<List<JugadorEfecto>>

    /**
     * Resta 1 a la duración de todos los efectos activos de todos los jugadores.
     */
    @Query("""
    UPDATE jugador_efectos
       SET duracion = duracion - 1
     WHERE duracion > 0
  """)
    suspend fun decrementarDuracionDeTodosEfectos()

    /**
     * Elimina todos los efectos cuya duración haya llegado a cero.
     */
    @Query("""
    DELETE FROM jugador_efectos
     WHERE duracion <= 0
  """)
    suspend fun eliminarEfectosExpirados()


@Insert suspend fun insert(je: JugadorEfecto): Long
    @Update suspend fun update(je: JugadorEfecto)
    @Delete suspend fun delete(je: JugadorEfecto)
}



data class InventarioNegocioWithNegocio(
    @Embedded val invNegocio: InventarioNegocio,
    @Relation(
        parentColumn = "fk_negocio",
        entityColumn = "id"
    )
    val negocio: Negocio
)

data class InventarioComidaWithComida(
    @Embedded val invComida: InventarioComida,
    @Relation(
        parentColumn = "fk_comida",
        entityColumn = "id"
    )
    val comida: Comida
)

data class InventarioTarjetaWithTarjeta(
    @Embedded val invTarjeta: InventarioTarjeta,
    @Relation(
        parentColumn = "fk_tarjeta",
        entityColumn = "id"
    )
    val tarjeta: Tarjeta
)


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

    /** Flujo de todos los registros de inventario_negocio */
    @Query("SELECT id, fk_inventario, fk_negocio, cantidad FROM inventario_negocio")
    fun getAll(): Flow<List<InventarioNegocio>>

    // JOIN entre inventario_negocio y negocio:
    @Transaction
    @Query("SELECT * FROM inventario_negocio WHERE fk_inventario = :inventarioId")
    fun getConDetalle(inventarioId: Long): Flow<List<InventarioNegocioWithNegocio>>


    @Query("SELECT * FROM inventario_negocio WHERE fk_inventario = :inventarioId")
    fun getNegociosForInventario(inventarioId: Long): Flow<List<InventarioNegocio>>

    /** Devuelve la fila si ya existe esa relación inventario–negocio */
    @Query("""
    SELECT * 
      FROM inventario_negocio 
     WHERE fk_inventario = :inventarioId 
       AND fk_negocio    = :negocioId
     LIMIT 1
  """)
    suspend fun getByInventarioAndNegocio(
        inventarioId: Long,
        negocioId: Long
    ): InventarioNegocio?

    /**
     * Devuelve un mapa negocioId → cantidad de filas en ese inventario
     * (cada fila representa 1 unidad)
     */
    @Query("""
    SELECT fk_negocio AS itemId, COUNT(*) AS count
      FROM inventario_negocio
     WHERE fk_inventario = :inventarioId
     GROUP BY fk_negocio
  """)
    suspend fun countAllByInventario(inventarioId: Long): List<ItemCount>

    @Query("SELECT cantidad FROM inventario_negocio WHERE fk_negocio = :fk_negocio")
    suspend fun selectCantidad(fk_negocio : Long) : Int

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
    /**
     * Recupera todas las filas de partida_jugador para la partida indicada.
     */
    @Query("SELECT * FROM partida_jugador WHERE fk_partida = :partidaId")
    fun getByPartida(partidaId: Long): LiveData<List<PartidaJugador>>
    /** Recupera directamente las entidades Jugador que están en la partida indicada */
    @Query("""
    SELECT j.* 
      FROM jugador j
      JOIN partida_jugador pj ON j.id = pj.fk_jugador
     WHERE pj.fk_partida = :partidaId
  """)
    fun getJugadoresForPartida(partidaId: Long): LiveData<List<Jugador>>
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

@Dao
interface ResumenDiaDao {
    @Insert suspend fun insert(resumen: ResumenDia): Long

    @Update suspend fun update(resumen: ResumenDia)

    @Delete suspend fun delete(resumen: ResumenDia)

    /** Obtiene el resumen de un jugador en un día concreto */
    @Query("""
      SELECT * 
        FROM resumen_dia 
       WHERE numDia = :numDia
         AND fk_jugador = :jugadorId
       LIMIT 1
    """)
    fun getResumenLive(numDia: Int, jugadorId: Long): LiveData<ResumenDia?>

    /** (Opcional) acceso síncrono desde coroutine */
    @Query("""
      SELECT * 
        FROM resumen_dia 
       WHERE numDia = :numDia
         AND fk_jugador = :jugadorId
       LIMIT 1
    """)
    suspend fun getResumen(numDia: Int, jugadorId: Long): ResumenDia?

    /** Lista todos los resúmenes de un jugador */
    @Query("SELECT * FROM resumen_dia WHERE fk_jugador = :jugadorId")
    fun getAllByJugador(jugadorId: Long): LiveData<List<ResumenDia>>
}




