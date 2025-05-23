package com.example.tfg.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.BikeScooter
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalCarWash
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.RollerSkating
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.ui.graphics.vector.ImageVector

// Función para mapear el nombre del icono guardado en la base de datos a un ImageVector
fun iconFromString(name: String): ImageVector = when (name) {
    "LocalCafe" -> Icons.Default.LocalCafe
    "Restaurant" -> Icons.Default.Restaurant
    "Store" -> Icons.Default.Store
    "FitnessCenter" -> Icons.Default.FitnessCenter
    "MenuBook" -> Icons.Default.MenuBook
    "LocalPharmacy" -> Icons.Default.LocalPharmacy
    "LocalMovies" -> Icons.Default.LocalMovies
    "Hotel" -> Icons.Default.Hotel
    "ContentCut" -> Icons.Default.ContentCut
    "ShoppingCart" -> Icons.Default.ShoppingCart
    "Build" -> Icons.Default.Build
    "Cake" -> Icons.Default.Cake
    "LocalDrink" -> Icons.Default.LocalDrink
    "Newspaper" -> Icons.Default.Newspaper
    "Icecream" -> Icons.Default.Icecream
    "PhoneIphone" -> Icons.Default.PhoneIphone
    "LocalCarWash" -> Icons.Default.LocalCarWash
    "Fastfood" -> Icons.Default.Fastfood
    "LocalGroceryStore" -> Icons.Default.LocalGroceryStore
    "CardGiftcard" -> Icons.Default.CardGiftcard
    "PedalBike" -> Icons.Default.PedalBike
    "Checkroom" -> Icons.Default.Checkroom
    "PhotoCamera" -> Icons.Default.PhotoCamera
    "Pets" -> Icons.Default.Pets
    "LocalPostOffice" -> Icons.Default.LocalPostOffice
    "WbSunny" -> Icons.Default.WbSunny
    "Toys" -> Icons.Default.Toys
    "RollerSkating" -> Icons.Default.RollerSkating
    "Coffee" -> Icons.Default.Coffee
    "Computer" -> Icons.Default.Computer
    "Draw" -> Icons.Default.Draw
    "ChildCare" -> Icons.Default.ChildCare
    "TravelExplore" -> Icons.Default.TravelExplore
    "Storefront" -> Icons.Default.Storefront
    "Language" -> Icons.Default.Language
    "CarRepair" -> Icons.Default.CarRepair
    "BikeScooter" -> Icons.Default.BikeScooter
    "DesignServices" -> Icons.Default.DesignServices
    "BakeryDining" -> Icons.Default.BakeryDining
    "Spa" -> Icons.Default.Spa
    "Book" -> Icons.Default.Book
    "ShoppingBasket" -> Icons.Default.ShoppingBasket
    "FoodBank" -> Icons.Default.FoodBank
    "Shop" -> Icons.Default.Shop
    "Workspaces" -> Icons.Default.Workspaces
    "RestaurantMenu" -> Icons.Default.RestaurantMenu
    "FaceRetouchingNatural" -> Icons.Default.FaceRetouchingNatural
    "Code" -> Icons.Default.Code
    "PrecisionManufacturing" -> Icons.Default.PrecisionManufacturing
    "Movie" -> Icons.Default.Movie
    "RocketLaunch" -> Icons.Default.RocketLaunch
    "OndemandVideo" -> Icons.Default.OndemandVideo
    "Recycling" -> Icons.Default.Recycling
    "SmartToy" -> Icons.Default.SmartToy
    "Sailing" -> Icons.Default.Sailing
    "VideogameAsset" -> Icons.Default.VideogameAsset
    "Science" -> Icons.Default.Science
    "LocalHospital" -> Icons.Default.LocalHospital
    "ElectricCar" -> Icons.Default.ElectricCar
    "Tv" -> Icons.Default.Tv
    "SatelliteAlt" -> Icons.Default.SatelliteAlt
    "ShoppingBag" -> Icons.Default.ShoppingBag
    else -> Icons.Default.Storefront
}