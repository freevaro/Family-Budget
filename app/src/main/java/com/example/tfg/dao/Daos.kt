package com.example.tfg.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfg.entity.*

@Dao
interface MesDao {
    @Query("SELECT * FROM mes") fun getAll(): LiveData<List<Mes>>
    @Insert suspend fun insert(m: Mes)
    @Update suspend fun update(m: Mes)
    @Delete suspend fun delete(m: Mes)
}

@Dao
interface DiaDao {
    @Query("SELECT * FROM dia") fun getAll(): LiveData<List<Dia>>
    @Insert suspend fun insert(d: Dia)
    @Update suspend fun update(d: Dia)
    @Delete suspend fun delete(d: Dia)
}

@Dao
interface JugadorDao {
    @Query("SELECT * FROM jugador") fun getAll(): LiveData<List<Jugador>>
    @Insert suspend fun insert(j: Jugador)
    @Update suspend fun update(j: Jugador)
    @Delete suspend fun delete(j: Jugador)
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
}

@Dao
interface NegocioDao {
    @Query("SELECT * FROM negocio") fun getAll(): LiveData<List<Negocio>>
    @Insert suspend fun insert(n: Negocio)
    @Update suspend fun update(n: Negocio)
    @Delete suspend fun delete(n: Negocio)
}

@Dao
interface TiendaDao {
    @Query("SELECT * FROM tienda") fun getAll(): LiveData<List<Tienda>>
    @Insert suspend fun insert(t: Tienda)
    @Update suspend fun update(t: Tienda)
    @Delete suspend fun delete(t: Tienda)
}

@Dao
interface InventarioDao {
    @Query("SELECT * FROM inventario") fun getAll(): LiveData<List<Inventario>>
    @Insert suspend fun insert(i: Inventario)
    @Update suspend fun update(i: Inventario)
    @Delete suspend fun delete(i: Inventario)
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
interface PartidaDao {
    @Query("SELECT * FROM partida") fun getAll(): LiveData<List<Partida>>
    @Insert suspend fun insert(p: Partida)
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
