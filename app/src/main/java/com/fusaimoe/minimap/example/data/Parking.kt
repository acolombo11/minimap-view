package com.fusaimoe.minimap.example.data

import androidx.annotation.IntRange

data class Parking(var space: Space? = null, var car: Car? = null) {
    companion object {

        private const val PARKING_LOT_HEIGHT = 2

        fun getEmptyParkingLot(@IntRange(from = 2, to = Long.MAX_VALUE) width: Int) : MutableList<List<Parking>> {
            val lot: MutableList<List<Parking>> = mutableListOf()
            for (i in 0 until PARKING_LOT_HEIGHT) {
                val row: MutableList<Parking> = mutableListOf()
                for (j in 0 until width) {
                    row.add(
                        Parking(
                            when (j) {
                                0 -> if (i == 0) Space.TOP_LEFT else Space.BOTTOM_LEFT
                                width - 1 -> if (i == 0) Space.TOP_RIGHT else Space.BOTTOM_RIGHT
                                else -> if (i == 0) Space.TOP_CENTER else Space.BOTTOM_CENTER
                            }
                        )
                    )
                }
                lot.add(row)
            }
            return lot
        }

    }
}