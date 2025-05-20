package com.example.tfg

import com.example.tfg.entity.Tarjeta
import com.example.tfg.dao.TarjetaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Inserta una lista de tarjetas predefinidas en la base de datos al crearla.
 */
suspend fun prepopulateTarjetas(tarjetaDao: TarjetaDao) = withContext(Dispatchers.IO) {
    val tarjetas = listOf(
        Tarjeta(nombre = "Tarjeta Negocio", nombreEfecto = "", tipoEfecto = "", dirigidoA = "", tipoTarjeta = "", queHace = "", queModifica = "", efectoValor = 5000),
        Tarjeta(nombre = "Tarjeta Dinero", nombreEfecto = "", tipoEfecto = "", dirigidoA = "", tipoTarjeta = "", queHace = "", queModifica = "", efectoValor = 5000),
        Tarjeta(nombre = "Tarjeta Aleatoria", nombreEfecto = "", tipoEfecto = "", dirigidoA = "", tipoTarjeta = "", queHace = "", queModifica = "", efectoValor = 5000),
        Tarjeta(nombre = "Campaña Viral", nombreEfecto = "Impulso de Ingresos", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 30),
        Tarjeta(nombre = "Expansión Internacional", nombreEfecto = "Expansión de Mercado", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 100),
        Tarjeta(nombre = "Mejora de Producto", nombreEfecto = "Mejora de Ventas", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 20),
        Tarjeta(nombre = "Estrategia de Precios", nombreEfecto = "Ajuste de Precios", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 15),
        Tarjeta(nombre = "Alianza Comercial", nombreEfecto = "Socios Estratégicos", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 25),
        Tarjeta(nombre = "Automatización", nombreEfecto = "Reducción de Costos", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 20),
        Tarjeta(nombre = "Energía Renovable", nombreEfecto = "Ahorro Energético", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 15),
        Tarjeta(nombre = "Negociación de Proveedores", nombreEfecto = "Mejora de Contratos", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 25),
        Tarjeta(nombre = "Mejora Logística", nombreEfecto = "Optimización de Rutas", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 30),
        Tarjeta(nombre = "Subsidio Gubernamental", nombreEfecto = "Fondo de Apoyo", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 20),
        Tarjeta(nombre = "Subvención Pública", nombreEfecto = "Fondo de Innovación", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 700),
        Tarjeta(nombre = "Venta de Acciones", nombreEfecto = "Flujo de Caja", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 500),
        Tarjeta(nombre = "Premio Empresarial", nombreEfecto = "Premio de Éxito", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 1000),
        Tarjeta(nombre = "Dividendo Especial", nombreEfecto = "Pago de Dividendos", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 300),
        Tarjeta(nombre = "Patrocinio Comercial", nombreEfecto = "Ingreso Extra", tipoEfecto = "Positivo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 800),
        Tarjeta(nombre = "Seminario Gratuito", nombreEfecto = "Capacitación Ajena", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 25),
        Tarjeta(nombre = "Asesoría Externa", nombreEfecto = "Consultoría Provechosa", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 30),
        Tarjeta(nombre = "Inversión Social", nombreEfecto = "Apoyo Comunitario", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 15),
        Tarjeta(nombre = "Subvención Competitiva", nombreEfecto = "Ayuda de Costos Ajena", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 20),
        Tarjeta(nombre = "Exención Tributaria", nombreEfecto = "Alivio Fiscal Ajeno", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 25),
        Tarjeta(nombre = "Descuento de Proveedores", nombreEfecto = "Contrato Benevolente", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 15),
        Tarjeta(nombre = "Regalo Corporativo", nombreEfecto = "Donación de Dinero", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 500),
        Tarjeta(nombre = "Bonificación por Lealtad", nombreEfecto = "Incentivo a Rival", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 400),
        Tarjeta(nombre = "Patrocinio Ajeno", nombreEfecto = "Ingreso para Rival", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 700),
        Tarjeta(nombre = "Premio al Mérito", nombreEfecto = "Galardón Financiero", tipoEfecto = "Positivo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 600),
        Tarjeta(nombre = "Inspección Sanitaria", nombreEfecto = "Multa de Ventas", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 30),
        Tarjeta(nombre = "Escándalo Público", nombreEfecto = "Pérdida de Confianza", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 25),
        Tarjeta(nombre = "Campaña de Boicot", nombreEfecto = "Boicot Comercial", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 20),
        Tarjeta(nombre = "Retirada de Licencia", nombreEfecto = "Suspensión de Actividades", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 100),
        Tarjeta(nombre = "Críticas en Redes", nombreEfecto = "Malas Opiniones", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 15),
        Tarjeta(nombre = "Aumento de Impuestos", nombreEfecto = "Carga Fiscal", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 20),
        Tarjeta(nombre = "Escasez de Materias", nombreEfecto = "Coste Elevado", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 25),
        Tarjeta(nombre = "Regulación Estricta", nombreEfecto = "Controles Gubernamentales", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 30),
        Tarjeta(nombre = "Huelga de Trabajadores", nombreEfecto = "Parón Laboral", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 20),
        Tarjeta(nombre = "Fallo de Proveedores", nombreEfecto = "Retraso de Suministro", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 15),
        Tarjeta(nombre = "Robo de Cartera", nombreEfecto = "Hurto Financiero", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 500),
        Tarjeta(nombre = "Estafa Piramidal", nombreEfecto = "Fraude Financiero", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 400),
        Tarjeta(nombre = "Multa Legislativa", nombreEfecto = "Sanción Monetaria", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 600),
        Tarjeta(nombre = "Cobro de Deuda", nombreEfecto = "Reclamación Judicial", tipoEfecto = "Negativo", dirigidoA = "Otros", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 700),
        Tarjeta(nombre = "Pago de Impuestos", nombreEfecto = "Carga Fiscal Personal", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 500),
        Tarjeta(nombre = "Gasto Inesperado", nombreEfecto = "Emergencia", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 400),
        Tarjeta(nombre = "Devolución de Fondos", nombreEfecto = "Reembolso Forzado", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 600),
        Tarjeta(nombre = "Reparaciones Urgentes", nombreEfecto = "Mantenimiento", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 300),
        Tarjeta(nombre = "Multa por Incumplimiento", nombreEfecto = "Penalización", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "dinero", queHace = "dinero", queModifica = "dinero", efectoValor = 700),
        Tarjeta(nombre = "Crisis de Reputación", nombreEfecto = "Pérdida de Ventas", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 25),
        Tarjeta(nombre = "Problemas de Calidad", nombreEfecto = "Devoluciones Masivas", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 30),
        Tarjeta(nombre = "Fallo Tecnológico", nombreEfecto = "Caída de Servicios", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "ingresos", queModifica = "ingresos", efectoValor = 100),
        Tarjeta(nombre = "Inflación Extrema", nombreEfecto = "Aumento de Costos", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 20),
        Tarjeta(nombre = "Retraso en Entregas", nombreEfecto = "Penalización de Proveedores", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 25),
        Tarjeta(nombre = "Fuga de Talento", nombreEfecto = "Indemnización Laboral", tipoEfecto = "Negativo", dirigidoA = "Propio", tipoTarjeta = "negocio", queHace = "costes", queModifica = "costes", efectoValor = 30)
    )
    tarjetas.forEach { tarjetaDao.insert(it) }
}
