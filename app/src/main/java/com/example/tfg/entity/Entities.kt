package com.example.tfg.entity

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.NO_ACTION

/**
 * Representa un mes dentro del juego.
 */
@Entity(tableName = "mes")
data class Mes(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val numero: Int,
    @ColumnInfo(name = "fk_dia") val fkDia: Long? = null
)

/**
 * Representa un día del calendario de juego.
 */
@Entity(
    tableName = "dia",
    foreignKeys = [
        ForeignKey(Mes::class, parentColumns = ["id"], childColumns = ["fk_mes"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Jugador::class, parentColumns = ["id"], childColumns = ["fk_jugador"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_mes"), Index("fk_jugador")]
)
data class Dia(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "numero_dia") val numeroDia: Int,
    @ColumnInfo(name = "fk_jugador") val fkJugador: Long,
    @ColumnInfo(name = "fk_mes") val fkMes: Long
)

/**
 * Jugador del juego, con sus datos financieros.
 */
@Entity(tableName = "jugador")
data class Jugador(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val dinero: Double,
    val ingresos: Double,
    val gastos: Double
)

/**
 * Tipo de comida disponible en la tienda.
 */
@Entity(tableName = "comida")
data class Comida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var nombre: String,
    val duracion: Int,
    val precio: Int,
    @ColumnInfo(name = "efecto_valor") val efecto: Int
)

/**
 * Tarjeta con efectos especiales que alteran el juego.
 */
@Entity(tableName = "tarjeta")
data class Tarjeta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    @ColumnInfo(name = "nombre_efecto") val nombreEfecto: String,
    @ColumnInfo(name = "tipo_efecto") val tipoEfecto: String,
    @ColumnInfo(name = "tipo_tarjeta") val tipoTarjeta: String,
    @ColumnInfo(name = "dirigido_a") val dirigidoA: String,
    @ColumnInfo(name = "que_modifica") val queModifica: String,
    @ColumnInfo(name = "efecto_valor") val efectoValor: Int,
    @ColumnInfo(name = "que_hace") val queHace: String
)

/**
 * Negocio que el jugador puede comprar para obtener ingresos pasivos.
 */
@Entity(tableName = "negocio")
data class Negocio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val ingresos: Double,
    @ColumnInfo(name = "coste_tienda") val costeTienda: Double,
    @ColumnInfo(name = "coste_mantenimiento") val costeMantenimiento: Double,
    val categoria: String,
    val icon: String
)

/**
 * Registro de ítems disponibles en tienda.
 */
@Entity(
    tableName = "tienda",
    foreignKeys = [
        ForeignKey(Jugador::class, parentColumns = ["id"], childColumns = ["fk_jugador"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Dia::class, parentColumns = ["id"], childColumns = ["fk_dia"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("fk_jugador"), Index("fk_dia")]
)
data class Tienda(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_jugador") val fkJugador: Long,
    @ColumnInfo(name = "fk_dia") val fkDia: Long
)

/**
 * Inventario asociado a un único jugador y partida.
 */
@Entity(
    tableName = "inventario",
    foreignKeys = [
        ForeignKey(Jugador::class, parentColumns = ["id"], childColumns = ["fk_jugador"], onDelete = CASCADE),
        ForeignKey(Partida::class, parentColumns = ["id"], childColumns = ["fk_partida"], onDelete = CASCADE)
    ],
    indices = [Index("fk_jugador"), Index("fk_partida")]
)
data class Inventario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_jugador") val fkJugador: Long,
    @ColumnInfo(name = "fk_partida") val fkPartida: Long
)

@Entity(
    tableName = "inventario_negocio",
    foreignKeys = [
        ForeignKey(
            entity = Inventario::class,
            parentColumns = ["id"],
            childColumns = ["fk_inventario"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Negocio::class,
            parentColumns = ["id"],
            childColumns = ["fk_negocio"],
            onDelete = NO_ACTION
        )
    ],
    indices = [
        Index("fk_inventario"),
        Index("fk_negocio")
    ]
)
data class InventarioNegocio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_inventario") val fkInventario: Long,
    @ColumnInfo(name = "fk_negocio")    val fkNegocio: Long,
    @ColumnInfo(name = "cantidad")      val cantidad: Int = 1
)


/**
 * Relación entre Inventario y Comida con duración de efecto.
 */
@Entity(
    tableName = "inventario_comida",
    foreignKeys = [
        ForeignKey(
            Inventario::class,
            parentColumns = ["id"],
            childColumns = ["fk_inventario"],
            onDelete = CASCADE),
        ForeignKey(
            Comida::class,
            parentColumns = ["id"],
            childColumns = ["fk_comida"],
            onDelete = CASCADE)
    ],
    indices = [Index("fk_inventario"), Index("fk_comida")]
)
data class InventarioComida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_inventario") val fkInventario: Long,
    @ColumnInfo(name = "fk_comida") val fkComida: Long,
    @ColumnInfo(name = "duracion") val duracion: Int
)

/**
 * Relación entre Inventario y Tarjeta con duración de efecto.
 */
@Entity(
    tableName = "inventario_tarjeta",
    foreignKeys = [
        ForeignKey(Inventario::class, parentColumns = ["id"], childColumns = ["fk_inventario"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Tarjeta::class, parentColumns = ["id"], childColumns = ["fk_tarjeta"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_inventario"), Index("fk_tarjeta")]
)
data class InventarioTarjeta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_inventario") val fkInventario: Long,
    @ColumnInfo(name = "fk_tarjeta") val fkTarjeta: Long,
    @ColumnInfo(name = "duracion") val duracion: Int
)

/**
 * Partida principal del juego.
 */
@Entity(tableName = "partida")
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ganador: String
)

/**
 * Relación entre Partida y Jugador.
 */
@Entity(
    tableName = "partida_jugador",
    foreignKeys = [
        ForeignKey(Partida::class, parentColumns = ["id"], childColumns = ["fk_partida"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Jugador::class, parentColumns = ["id"], childColumns = ["fk_jugador"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_partida"), Index("fk_jugador")]
)
data class PartidaJugador(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_partida") val fkPartida: Long,
    @ColumnInfo(name = "fk_jugador") val fkJugador: Long
)

/**
 * Relación entre Partida y Día.
 */
@Entity(
    tableName = "partida_dia",
    foreignKeys = [
        ForeignKey(Partida::class, parentColumns = ["id"], childColumns = ["fk_partida"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Dia::class, parentColumns = ["id"], childColumns = ["fk_dia"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_partida"), Index("fk_dia")]
)
data class PartidaDia(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_partida") val fkPartida: Long,
    @ColumnInfo(name = "fk_dia") val fkDia: Long
)

/**
 * Relación entre Tienda y Negocio: un inventario de tienda puede contener múltiples negocios.
 */
@Entity(
    tableName = "tienda_negocio",
    foreignKeys = [
        ForeignKey(
            entity = Tienda::class,
            parentColumns = ["id"],
            childColumns = ["fk_tienda"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Negocio::class,
            parentColumns = ["id"],
            childColumns = ["fk_negocio"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index("fk_tienda"), Index("fk_negocio")]
)
data class TiendaNegocio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_tienda") val fkTienda: Long,
    @ColumnInfo(name = "fk_negocio") val fkNegocio: Long
)



