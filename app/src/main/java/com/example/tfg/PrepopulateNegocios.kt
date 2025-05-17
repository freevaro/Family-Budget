package com.example.tfg.util

import com.example.tfg.entity.Negocio
import com.example.tfg.dao.NegocioDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Inserta una lista generada por mi de los negocios que va a haber en la base de datos cada vez que se cree.
  */


suspend fun prepopulateNegocios(negocioDao: NegocioDao) = withContext(Dispatchers.IO) {
    val negocios = listOf(
        Negocio(nombre = "Puesto de limonada", ingresos = 20.0, costeTienda = 100.0, costeMantenimiento = 10.0, categoria = "Baja", icon = "LocalDrink"),
        Negocio(nombre = "Máquina expendedora", ingresos = 40.0, costeTienda = 300.0, costeMantenimiento = 15.0, categoria = "Baja", icon = "ShoppingCart"),
        Negocio(nombre = "Kiosco de prensa", ingresos = 60.0, costeTienda = 500.0, costeMantenimiento = 25.0, categoria = "Baja", icon = "Newspaper"),
        Negocio(nombre = "Venta de helados", ingresos = 80.0, costeTienda = 700.0, costeMantenimiento = 30.0, categoria = "Baja", icon = "Icecream"),
        Negocio(nombre = "Reparación de móviles", ingresos = 110.0, costeTienda = 1000.0, costeMantenimiento = 50.0, categoria = "Baja", icon = "PhoneIphone"),
        Negocio(nombre = "Lavado de coches", ingresos = 150.0, costeTienda = 1500.0, costeMantenimiento = 70.0, categoria = "Baja", icon = "LocalCarWash"),
        Negocio(nombre = "Food Truck de perritos", ingresos = 200.0, costeTienda = 2000.0, costeMantenimiento = 100.0, categoria = "Baja", icon = "Fastfood"),
        Negocio(nombre = "Puesto de palomitas", ingresos = 35.0, costeTienda = 250.0, costeMantenimiento = 20.0, categoria = "Baja", icon = "LocalMovies"),
        Negocio(nombre = "Tienda de golosinas", ingresos = 55.0, costeTienda = 400.0, costeMantenimiento = 30.0, categoria = "Baja", icon = "LocalGroceryStore"),
        Negocio(nombre = "Recarga de móviles", ingresos = 75.0, costeTienda = 600.0, costeMantenimiento = 25.0, categoria = "Baja", icon = "PhoneIphone"),
        Negocio(nombre = "Mini tienda de souvenirs", ingresos = 90.0, costeTienda = 800.0, costeMantenimiento = 50.0, categoria = "Baja", icon = "CardGiftcard"),
        Negocio(nombre = "Paseos en triciclo turístico", ingresos = 130.0, costeTienda = 1200.0, costeMantenimiento = 60.0, categoria = "Baja", icon = "PedalBike"),
        Negocio(nombre = "Venta ambulante de ropa", ingresos = 170.0, costeTienda = 1600.0, costeMantenimiento = 80.0, categoria = "Baja", icon = "Checkroom"),
        Negocio(nombre = "Fotomatón callejero", ingresos = 190.0, costeTienda = 1800.0, costeMantenimiento = 90.0, categoria = "Baja", icon = "PhotoCamera"),
        Negocio(nombre = "Cuidado de mascotas", ingresos = 220.0, costeTienda = 2100.0, costeMantenimiento = 110.0, categoria = "Baja", icon = "Pets"),
        Negocio(nombre = "Reparto de folletos", ingresos = 230.0, costeTienda = 2200.0, costeMantenimiento = 120.0, categoria = "Baja", icon = "LocalPostOffice"),
        Negocio(nombre = "Reparación de bicicletas", ingresos = 250.0, costeTienda = 2400.0, costeMantenimiento = 130.0, categoria = "Baja", icon = "Build"),
        Negocio(nombre = "Venta de gafas de sol", ingresos = 280.0, costeTienda = 2600.0, costeMantenimiento = 150.0, categoria = "Baja", icon = "WbSunny"),
        Negocio(nombre = "Tienda de juguetes usados", ingresos = 300.0, costeTienda = 2800.0, costeMantenimiento = 170.0, categoria = "Baja", icon = "Toys"),
        Negocio(nombre = "Alquiler de patinetes", ingresos = 320.0, costeTienda = 3000.0, costeMantenimiento = 180.0, categoria = "Baja", icon = "RollerSkating"),
        Negocio(nombre = "Tienda de ropa urbana", ingresos = 500.0, costeTienda = 4000.0, costeMantenimiento = 300.0, categoria = "Media", icon = "Checkroom"),
        Negocio(nombre = "Cafetería artesanal", ingresos = 700.0, costeTienda = 6000.0, costeMantenimiento = 400.0, categoria = "Media", icon = "Coffee"),
        Negocio(nombre = "Gimnasio de barrio", ingresos = 1000.0, costeTienda = 8000.0, costeMantenimiento = 600.0, categoria = "Media", icon = "FitnessCenter"),
        Negocio(nombre = "Tienda de informática", ingresos = 1300.0, costeTienda = 10000.0, costeMantenimiento = 800.0, categoria = "Media", icon = "Computer"),
        Negocio(nombre = "Estudio de tatuajes", ingresos = 1600.0, costeTienda = 12000.0, costeMantenimiento = 1000.0, categoria = "Media", icon = "Draw"),
        Negocio(nombre = "Guardería privada", ingresos = 2000.0, costeTienda = 15000.0, costeMantenimiento = 1200.0, categoria = "Media", icon = "ChildCare"),
        Negocio(nombre = "Agencia de viajes", ingresos = 2500.0, costeTienda = 18000.0, costeMantenimiento = 1500.0, categoria = "Media", icon = "TravelExplore"),
        Negocio(nombre = "Tienda de electrodomésticos", ingresos = 2800.0, costeTienda = 20000.0, costeMantenimiento = 1800.0, categoria = "Media", icon = "Storefront"),
        Negocio(nombre = "Escuela de idiomas", ingresos = 3200.0, costeTienda = 22000.0, costeMantenimiento = 2000.0, categoria = "Media", icon = "Language"),
        Negocio(nombre = "Taller de coches tuning", ingresos = 3800.0, costeTienda = 25000.0, costeMantenimiento = 2500.0, categoria = "Media", icon = "CarRepair"),
        Negocio(nombre = "Tienda de bicicletas", ingresos = 4200.0, costeTienda = 27000.0, costeMantenimiento = 2700.0, categoria = "Media", icon = "BikeScooter"),
        Negocio(nombre = "Agencia de diseño gráfico", ingresos = 4800.0, costeTienda = 30000.0, costeMantenimiento = 3000.0, categoria = "Media", icon = "DesignServices"),
        Negocio(nombre = "Panadería gourmet", ingresos = 5200.0, costeTienda = 32000.0, costeMantenimiento = 3200.0, categoria = "Media", icon = "BakeryDining"),
        Negocio(nombre = "Centro de estética", ingresos = 5500.0, costeTienda = 34000.0, costeMantenimiento = 3500.0, categoria = "Media", icon = "Spa"),
        Negocio(nombre = "Clínica veterinaria", ingresos = 6000.0, costeTienda = 36000.0, costeMantenimiento = 3700.0, categoria = "Media", icon = "Pets"),
        Negocio(nombre = "Editorial independiente", ingresos = 6400.0, costeTienda = 38000.0, costeMantenimiento = 3900.0, categoria = "Media", icon = "Book"),
        Negocio(nombre = "Tienda de artículos deportivos", ingresos = 6800.0, costeTienda = 40000.0, costeMantenimiento = 4100.0, categoria = "Media", icon = "ShoppingBasket"),
        Negocio(nombre = "Escuela de cocina", ingresos = 7200.0, costeTienda = 42000.0, costeMantenimiento = 4300.0, categoria = "Media", icon = "FoodBank"),
        Negocio(nombre = "Tienda ecológica", ingresos = 7600.0, costeTienda = 44000.0, costeMantenimiento = 4500.0, categoria = "Media", icon = "Shop"),
        Negocio(nombre = "Coworking creativo", ingresos = 8000.0, costeTienda = 46000.0, costeMantenimiento = 4700.0, categoria = "Media", icon = "Workspaces"),
        Negocio(nombre = "Restaurante gourmet", ingresos = 5000.0, costeTienda = 30000.0, costeMantenimiento = 3000.0, categoria = "Alta", icon = "RestaurantMenu"),
        Negocio(nombre = "Clínica estética privada", ingresos = 7000.0, costeTienda = 40000.0, costeMantenimiento = 4000.0, categoria = "Alta", icon = "FaceRetouchingNatural"),
        Negocio(nombre = "Empresa de software", ingresos = 9000.0, costeTienda = 50000.0, costeMantenimiento = 5000.0, categoria = "Alta", icon = "Code"),
        Negocio(nombre = "Fábrica de drones", ingresos = 11000.0, costeTienda = 60000.0, costeMantenimiento = 6000.0, categoria = "Alta", icon = "PrecisionManufacturing"),
        Negocio(nombre = "Start-up de biotecnología", ingresos = 14000.0, costeTienda = 80000.0, costeMantenimiento = 8000.0, categoria = "Alta", icon = "PrecisionManufacturing"),
        Negocio(nombre = "Productora de cine", ingresos = 18000.0, costeTienda = 100000.0, costeMantenimiento = 10000.0, categoria = "Alta", icon = "Movie"),
        Negocio(nombre = "Empresa aeroespacial", ingresos = 25000.0, costeTienda = 150000.0, costeMantenimiento = 15000.0, categoria = "Alta", icon = "RocketLaunch"),
        Negocio(nombre = "Plataforma de streaming indie", ingresos = 20000.0, costeTienda = 120000.0, costeMantenimiento = 12000.0, categoria = "Alta", icon = "OndemandVideo"),
        Negocio(nombre = "Hotel boutique", ingresos = 23000.0, costeTienda = 140000.0, costeMantenimiento = 14000.0, categoria = "Alta", icon = "Checkroom"),
        Negocio(nombre = "Planta de reciclaje avanzada", ingresos = 27000.0, costeTienda = 160000.0, costeMantenimiento = 16000.0, categoria = "Alta", icon = "Recycling"),
        Negocio(nombre = "Fábrica de robots domésticos", ingresos = 30000.0, costeTienda = 180000.0, costeMantenimiento = 18000.0, categoria = "Alta", icon = "SmartToy"),
        Negocio(nombre = "Empresa de exploración marina", ingresos = 35000.0, costeTienda = 200000.0, costeMantenimiento = 20000.0, categoria = "Alta", icon = "Sailing"),
        Negocio(nombre = "Compañía de videojuegos AAA", ingresos = 38000.0, costeTienda = 220000.0, costeMantenimiento = 22000.0, categoria = "Alta", icon = "VideogameAsset"),
        Negocio(nombre = "Centro de investigación genética", ingresos = 42000.0, costeTienda = 240000.0, costeMantenimiento = 24000.0, categoria = "Alta", icon = "Science"),
        Negocio(nombre = "Cadena de clínicas privadas", ingresos = 46000.0, costeTienda = 260000.0, costeMantenimiento = 26000.0, categoria = "Alta", icon = "LocalHospital"),
        Negocio(nombre = "Industria de autos eléctricos", ingresos = 50000.0, costeTienda = 280000.0, costeMantenimiento = 28000.0, categoria = "Alta", icon = "ElectricCar"),
        Negocio(nombre = "Productora de TV internacional", ingresos = 54000.0, costeTienda = 300000.0, costeMantenimiento = 30000.0, categoria = "Alta", icon = "Tv"),
        Negocio(nombre = "Lanzadora de satélites", ingresos = 58000.0, costeTienda = 320000.0, costeMantenimiento = 32000.0, categoria = "Alta", icon = "SatelliteAlt"),
        Negocio(nombre = "Industria farmacéutica", ingresos = 62000.0, costeTienda = 340000.0, costeMantenimiento = 34000.0, categoria = "Alta", icon = "LocalPharmacy"),
        Negocio(nombre = "Mega centro comercial", ingresos = 66000.0, costeTienda = 360000.0, costeMantenimiento = 36000.0, categoria = "Alta", icon = "ShoppingBag")
    )
    negocios.forEach { negocioDao.insert(it) }
}
