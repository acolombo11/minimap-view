package com.fusaimoe.minimap.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fusaimoe.minimap.MinimapView.Companion.minimap
import com.fusaimoe.minimap.example.data.Car
import com.fusaimoe.minimap.example.data.Parking
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PARKING_LOT_WIDTH = 9
    }

    private var removedLines = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = FixedGridLayoutManager().apply { setTotalColumnCount(PARKING_LOT_WIDTH) }
        val adapter = ParkingAdapter(
            getExampleParkingLot().flatten().toMutableList(),
            object : ParkingAdapter.ParkingInteractionListener {
                override fun onParkingCountChange(count: Int) {
                    updateCounter(count)
                }
            })

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.minimap = minimapView

        updateCounter(adapter.emptyParkingSpots)
        minimapView.setOnClickListener {
            layoutBottom.visibility = if (layoutBottom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        buttonLastLine.setOnClickListener {
            toggleLastLines(adapter)
        }

    }

    private fun toggleLastLines(adapter: ParkingAdapter) {
        if (removedLines < 2) {
            for (i in 0..2) {
                for (j in 0 until PARKING_LOT_WIDTH) adapter.items.removeAt(adapter.items.size - 1)
            }
        } else if (removedLines < 4) {
            for (i in 0..2) {
                for (j in 0 until PARKING_LOT_WIDTH) adapter.items.add(Parking())
            }
        }
        adapter.notifyDataSetChanged()
        updateCounter(adapter.emptyParkingSpots)
        removedLines++
        if (removedLines == 4) removedLines = 0
    }

    private fun updateCounter(count: Int) {
        textAvailability.text = getString(R.string.parking_availability, count)
    }

    private fun getExampleParkingLot(): List<List<Parking>> {
        // Init the parking lot
        val bigParkingLot = mutableListOf<MutableList<Parking>>().apply {
            for (i in 0..7) add(mutableListOf())
        }

        // Setup some lanes
        val parkingLane1 = Parking.getEmptyParkingLane(4)
        parkingLane1[0][0].car = Car.RED
        parkingLane1[0][2].car = Car.AZURE
        parkingLane1[1][1].car = Car.YELLOW
        parkingLane1[1][2].car = Car.BROWN
        val parkingLane2 = Parking.getEmptyParkingLane(3)
        val parkingLane3 = Parking.getEmptyParkingLane(4)
        parkingLane3[0][1].car = Car.GREEN
        parkingLane3[0][3].car = Car.BLUE
        val parkingLane4 = Parking.getEmptyParkingLane(4)

        // Setup the parking lot
        bigParkingLot[0].apply {
            val list1 = parkingLane1[0]
            val list2 = parkingLane2[0]
            addAll(list1)
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH - list1.size - list2.size))
            addAll(list2)
        }
        bigParkingLot[1].apply {
            val list1 = parkingLane1[1]
            val list2 = parkingLane2[1]
            addAll(list1)
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH - list1.size - list2.size))
            addAll(list2)
        }
        bigParkingLot[2].apply {
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH))
        }
        bigParkingLot[3].apply {
            val list = parkingLane3[0]
            addAll(list)
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH - list.size))
        }
        bigParkingLot[4].apply {
            val list = parkingLane3[1]
            addAll(list)
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH - list.size))
        }
        bigParkingLot[5].apply {
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH))
        }
        bigParkingLot[6].apply {
            val list1 = parkingLane4[0]
            val list2 = parkingLane2[0]
            addAll(list1)
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH - list1.size - list2.size))
            addAll(list2)
        }
        bigParkingLot[7].apply {
            val list1 = parkingLane4[1]
            val list2 = parkingLane2[1]
            addAll(list1)
            addAll(Parking.getEmptyRoad(PARKING_LOT_WIDTH - list1.size - list2.size))
            addAll(list2)
        }
        return bigParkingLot
    }


}
