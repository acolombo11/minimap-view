package eu.acolombo.minimap.example

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.acolombo.minimap.example.data.Car
import eu.acolombo.minimap.example.data.Parking
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_parking.*
import kotlin.random.Random

class ParkingAdapter(val items: MutableList<Parking>, val listener: ParkingInteractionListener) :
    RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    var emptyParkingSpots = 0
        get() = items.filter { it.space != null && it.car == null }.size
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_parking))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], position)

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(parking: Parking, position: Int) {

            parking.space?.let {
                imageSpace.setImageResource(it.image)
                if (it.mirrorHorizontal) imageSpace.scaleX = -1f
                if (it.mirrorVertical) imageSpace.scaleY = -1f

                containerView.setOnClickListener {
                    parking.car = if (parking.car != null) null else getRandomCar()
                    notifyItemChanged(position)
                    listener.onParkingCountChange(emptyParkingSpots)
                }
            }

            parking.car?.let {
                imageCar.setImageResource(it.image)
            }

        }
    }

    private fun getRandomCar() = Car.values()[Random.nextInt(Car.values().size)]

    interface ParkingInteractionListener {
        fun onParkingCountChange(count: Int)
    }

}
