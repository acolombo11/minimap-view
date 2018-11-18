package com.fusaimoe.minimap.example

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fusaimoe.minimap.example.data.Parking
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_parking.*

class ParkingAdapter(private val items: List<Parking>) : RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_parking))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(parking: Parking) {

            parking.space?.let {
                val spaceDrawable = ContextCompat.getDrawable(containerView.context, it.image)
                spaceDrawable?.mirror(it.mirrorVertical, it.mirrorHorizontal)
                imageSpace.setImageDrawable(spaceDrawable)
            }

            parking.car?.let {
                imageCar.setImageResource(it.image)
            }
        }

    }
}
