package com.example.tfg.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.BikeScooter
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.RollerSkating
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.*

/**
 * Representa un mes dentro del juego.
 *
 * @property id ID autogenerado del mes.
 * @property numero Número del mes (1-12).
 * @property fkDia Día vinculado como representativo del mes (opcional).
 */

@Entity(tableName = "mes")
data class Mes(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val numero: Int,
    @ColumnInfo(name = "fk_dia") val fkDia: Long? = null
)

/**
 * Representa un día del calendario de juego.
 *
 * @property id ID único.
 * @property numeroDia Día del mes (1-31).
 * @property fkJugador ID del jugador activo ese día.
 * @property fkMes ID del mes al que pertenece el día.
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
 *
 * @property id Identificador único.
 * @property nombre Nombre del jugador.
 * @property dinero Cantidad actual de dinero.
 * @property ingresos Ingresos diarios totales.
 * @property gastos Gastos diarios totales.
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
 *
 * @property id ID de la comida.
 * @property nombre Nombre de la comida.
 * @property duracion Días que dura el efecto de la comida.
 * @property precio Precio en dinero del juego.
 */

@Entity(tableName = "comida")
data class Comida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val duracion: Int,
    val precio: Double
)

/**
 * Tarjeta con efectos especiales que alteran el juego.
 *
 * @property nombre Nombre de la tarjeta.
 * @property nombreEfecto Nombre del efecto visible al jugador.
 * @property tipoEfecto Tipo de efecto (positivo, negativo, etc).
 * @property dirigidoA Objetivo del efecto (jugador, todos, etc).
 * @property queHace Descripción del efecto.
 */

@Entity(tableName = "tarjeta")
data class Tarjeta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    @ColumnInfo(name = "nombre_efecto") val nombreEfecto: String,
    @ColumnInfo(name = "tipo_efecto") val tipoEfecto: String,
    @ColumnInfo(name = "dirigido_a") val dirigidoA: String,
    @ColumnInfo(name = "que_hace") val queHace: String
)

/**
 * Negocio que el jugador puede comprar para obtener ingresos pasivos.
 *
 * @property nombre Nombre del negocio.
 * @property ingresos Ingresos generados por turno.
 * @property costeTienda Precio de compra.
 * @property costeMantenimiento Coste diario por mantenerlo activo.
 * @property categoria Nivel del negocio (Baja, Media, Alta).
 * @property icon Nombre del icono a usar en la interfaz.
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


@Entity(
    tableName = "tienda",
    foreignKeys = [
        ForeignKey(Jugador::class, parentColumns = ["id"], childColumns = ["fk_jugador"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Dia::class, parentColumns = ["id"], childColumns = ["fk_dia"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Negocio::class, parentColumns = ["id"], childColumns = ["fk_negocios"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Comida::class, parentColumns = ["id"], childColumns = ["fk_comida"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Tarjeta::class, parentColumns = ["id"], childColumns = ["fk_tarjetas"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_jugador"), Index("fk_dia"), Index("fk_negocios"), Index("fk_comida"), Index("fk_tarjetas")]
)
data class Tienda(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_jugador") val fkJugador: Long,
    @ColumnInfo(name = "fk_dia") val fkDia: Long,
    @ColumnInfo(name = "fk_negocios") val fkNegocios: Long,
    @ColumnInfo(name = "fk_comida") val fkComida: Long,
    @ColumnInfo(name = "fk_tarjetas") val fkTarjetas: Long
)

@Entity(
    tableName = "inventario",
    foreignKeys = [
        ForeignKey(Jugador::class, parentColumns = ["id"], childColumns = ["fk_jugador"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Comida::class, parentColumns = ["id"], childColumns = ["fk_inventario_comida"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Tarjeta::class, parentColumns = ["id"], childColumns = ["fk_inventario_tarjetas"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Negocio::class, parentColumns = ["id"], childColumns = ["fk_negocios"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_jugador"), Index("fk_inventario_comida"), Index("fk_inventario_tarjetas"), Index("fk_negocios")]
)
data class Inventario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_jugador") val fkJugador: Long,
    @ColumnInfo(name = "fk_inventario_comida") val fkInventarioComida: Long,
    @ColumnInfo(name = "fk_inventario_tarjetas") val fkInventarioTarjetas: Long,
    @ColumnInfo(name = "fk_negocios") val fkNegocios: Long
)

@Entity(
    tableName = "inventario_comida",
    foreignKeys = [
        ForeignKey(Inventario::class, parentColumns = ["id"], childColumns = ["fk_inventario"], onDelete = ForeignKey.CASCADE),
        ForeignKey(Comida::class, parentColumns = ["id"], childColumns = ["fk_comida"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("fk_inventario"), Index("fk_comida")]
)
data class InventarioComida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fk_inventario") val fkInventario: Long,
    @ColumnInfo(name = "fk_comida") val fkComida: Long
)

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
    @ColumnInfo(name = "fk_tarjeta") val fkTarjeta: Long
)

@Entity(tableName = "partida")
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ganador: String
)

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
