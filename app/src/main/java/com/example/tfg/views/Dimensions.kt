package com.example.tfg.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimensions {
    @Composable
    fun screenWidth() = LocalConfiguration.current.screenWidthDp.dp

    @Composable
    fun screenHeight() = LocalConfiguration.current.screenHeightDp.dp

    @Composable
    fun responsiveSp(baseSp: Float) = (baseSp * LocalConfiguration.current.fontScale).sp

    @Composable
    fun widthPercentage(percentage: Float) = (LocalConfiguration.current.screenWidthDp * percentage / 100).dp

    @Composable
    fun heightPercentage(percentage: Float) = (LocalConfiguration.current.screenHeightDp * percentage / 100).dp
}