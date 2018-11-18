package com.fusaimoe.minimap.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fusaimoe.minimap.MinimapView.Companion.minimap
import com.fusaimoe.minimap.example.data.Car
import com.fusaimoe.minimap.example.data.Parking
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val layoutManager = FixedGridLayoutManager().apply { setTotalColumnCount(8) }
        val adapter = ParkingAdapter(getExampleParkingLot().flatten())

        recyclerView.layoutManager = layoutManager
        recyclerView.minimap = minimapView
        recyclerView.adapter = adapter
    }

    private fun getExampleParkingLot(): List<List<Parking>> {
        val parkingLot1 = Parking.getEmptyParkingLot(4)
        parkingLot1[0][0].car = Car.RED
        parkingLot1[0][2].car = Car.AZURE
        parkingLot1[1][1].car = Car.YELLOW
        parkingLot1[1][2].car = Car.BROWN
        val parkingLot2 = Parking.getEmptyParkingLot(2)
        parkingLot2[0][1].car = Car.GREEN
        parkingLot2[0][3].car = Car.BLUE
        val parkingLot3 = Parking.getEmptyParkingLot(4)

        val bigParkingLot : MutableList<MutableList<Parking>> = mutableListOf()
        bigParkingLot[0] = mutableListOf()
        bigParkingLot[0].addAll(parkingLot1[0])
        bigParkingLot[0].addAll(listOf(Parking(), Parking()))
        bigParkingLot[0].addAll(parkingLot2[0])
        bigParkingLot[1] = mutableListOf()
        bigParkingLot[1].addAll(parkingLot1[1])
        bigParkingLot[1].addAll(listOf(Parking(), Parking()))
        bigParkingLot[1].addAll(parkingLot2[1])
        bigParkingLot[2].addAll(listOf(Parking(), Parking(), Parking(), Parking(), Parking(), Parking(), Parking(), Parking()))
        bigParkingLot[3] = mutableListOf()
        bigParkingLot[3].addAll(parkingLot3[0])
        bigParkingLot[3].addAll(listOf(Parking(), Parking(), Parking(), Parking(), Parking(), Parking()))
        bigParkingLot[4] = mutableListOf()
        bigParkingLot[4].addAll(parkingLot3[1])
        bigParkingLot[4].addAll(listOf(Parking(), Parking(), Parking(), Parking(), Parking(), Parking()))
        bigParkingLot[5].addAll(listOf(Parking(), Parking(), Parking(), Parking(), Parking(), Parking(), Parking(), Parking()))
        bigParkingLot[6] = mutableListOf()
        bigParkingLot[6].addAll(parkingLot3[0])
        bigParkingLot[6].addAll(listOf(Parking(), Parking()))
        bigParkingLot[6].addAll(parkingLot2[0])
        bigParkingLot[7] = mutableListOf()
        bigParkingLot[7].addAll(parkingLot3[1])
        bigParkingLot[7].addAll(listOf(Parking(), Parking()))
        bigParkingLot[7].addAll(parkingLot2[1])
        return bigParkingLot
    }
}
