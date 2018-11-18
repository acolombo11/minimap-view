package com.fusaimoe.minimap.example.data

import androidx.annotation.DrawableRes
import com.fusaimoe.minimap.example.R

enum class Car (@DrawableRes val image: Int){
    RED(R.drawable.car_red),
    YELLOW(R.drawable.car_yellow),
    BLUE(R.drawable.car_blue),
    BROWN(R.drawable.car_brown),
    GREEN(R.drawable.car_green),
    AZURE(R.drawable.car_azure)
}