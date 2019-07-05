package eu.acolombo.minimap.example

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import eu.acolombo.minimap.example.data.Car
import eu.acolombo.minimap.example.data.Parking
import eu.acolombo.minimap.minimap
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        const val PARKING_LOT_WIDTH = 9
    }

    private var removedLines = 0
    private val rnd = Random(69)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = FixedGridLayoutManager()
            .apply { setTotalColumnCount(PARKING_LOT_WIDTH) }
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
                for (j in 0 until PARKING_LOT_WIDTH) adapter.items.add(
                    Parking()
                )
            }
        }
        adapter.notifyDataSetChanged()
        updateCounter(adapter.emptyParkingSpots)
        removedLines++
        if (removedLines == 4) removedLines = 0

        // If we are still having problems in the future, make updateScaleFactor public and use it when notifying adapter changes like this:
        // minimapView.updateScaleFactor(recyclerView)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_tests, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_test_size -> {
                minimapView.maxSize = getRandomSize().toFloat()
                true
            }
            R.id.action_test_corner -> {
                minimapView.cornerRadius = getRandomRadius().toFloat()
                true
            }
            R.id.action_test_background -> {
                minimapView.mapBackgroundColor = getRandomColor()
                true
            }
            R.id.action_test_color -> {
                minimapView.indicatorColor = getRandomColor()
                true
            }
            R.id.action_test_border -> {
                minimapView.borderWidth = getRandomRadius()/2.toFloat()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getRandomSize() = rnd.nextInt(200,600)
    private fun getRandomRadius() = rnd.nextInt(0,30)
    private fun getRandomColor() = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

}
