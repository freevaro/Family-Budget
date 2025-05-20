package com.example.tfg

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tfg.dao.*
import com.example.tfg.entity.*
import com.example.tfg.util.prepopulateNegocios
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clase principal de la base de datos Room de la aplicación.
 *
 * Define las entidades utilizadas y proporciona acceso a los DAOs correspondientes.
 * También incluye una función `getInstance` para obtener la instancia singleton de la base de datos,
 * con una `Callback` para insertar datos iniciales al crearla por primera vez.
 */
@Database(
    entities = [
        Mes::class,
        Dia::class,
        Jugador::class,
        Comida::class,
        Tarjeta::class,
        Negocio::class,
        Tienda::class,
        Inventario::class,
        InventarioNegocio::class,
        InventarioComida::class,
        InventarioTarjeta::class,
        Partida::class,
        PartidaJugador::class,
        PartidaDia::class,
        TiendaNegocio::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * DAO para acceder a la tabla `Mes`.
     */
    abstract fun mesDao(): MesDao

    /**
     * DAO para acceder a la tabla `Dia`.
     */
    abstract fun diaDao(): DiaDao

    /**
     * DAO para acceder a la tabla `Jugador`.
     */
    abstract fun jugadorDao(): JugadorDao

    /**
     * DAO para acceder a la tabla `Comida`.
     */
    abstract fun comidaDao(): ComidaDao

    /**
     * DAO para acceder a la tabla `Tarjeta`.
     */
    abstract fun tarjetaDao(): TarjetaDao


    /**
     * DAO para acceder a la tabla de relacion `InventarioNegocio`
     */
    abstract fun inventarioNegocioDao(): InventarioNegocioDao

    /**
     * DAO para acceder a la tabla `Negocio`.
     */
    abstract fun negocioDao(): NegocioDao

    /**
     * DAO para acceder a la tabla `Tienda`.
     */
    abstract fun tiendaDao(): TiendaDao

    /**
     * DAO para acceder a la tabla `Inventario`.
     */
    abstract fun inventarioDao(): InventarioDao

    /**
     * DAO para acceder a la tabla de relación `InventarioComida`.
     */
    abstract fun inventarioComidaDao(): InventarioComidaDao

    /**
     * DAO para acceder a la tabla de relación `InventarioTarjeta`.
     */
    abstract fun inventarioTarjetaDao(): InventarioTarjetaDao

    /**
     * DAO para acceder a la tabla `Partida`.
     */
    abstract fun partidaDao(): PartidaDao

    /**
     * DAO para acceder a la tabla de relación `PartidaJugador`.
     */
    abstract fun partidaJugadorDao(): PartidaJugadorDao

    /**
     * DAO para acceder a la tabla de relación `PartidaDia`.
     */
    abstract fun partidaDiaDao(): PartidaDiaDao

    /**
     * DAO para acceder a la tabla de relación `TiendaNegocio`.
     */
    abstract fun tiendaNegocioDao(): TiendaNegocioDao



    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia singleton de la base de datos.
         *
         * Si la instancia aún no existe, la crea con `Room.databaseBuilder`.
         * Además, incluye una `Callback` para prepoblar la base de datos con negocios iniciales.
         *
         * @param context Contexto de la aplicación necesario para construir la base de datos.
         * @return Instancia única de [AppDatabase].
         */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TFG_BBDD"
                )
                    .addCallback(object : RoomDatabase.Callback() {

                        /**
                         * Callback que se ejecuta una vez al crear la base de datos.
                         * Se utiliza para poblar la base de datos con datos iniciales,
                         * como los negocios definidos en [prepopulateNegocios].
                         */
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val scope = CoroutineScope(Dispatchers.IO)
                            scope.launch {
                                prepopulateNegocios(getInstance(context).negocioDao())
                                prepopulateTarjetas(getInstance(context).tarjetaDao())
                                prepopulateComidas(getInstance(context).comidaDao())
                            }
                        }
                    })
                    .build().also { INSTANCE = it }
            }
    }
}
