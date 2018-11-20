package com.fusaimoe.minimap.example

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fusaimoe.minimap.example.data.Parking
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_parking.*
import kotlinx.android.synthetic.main.item_parking.view.*

class ParkingAdapter(val items: MutableList<Parking>) : RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_parking))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(parking: Parking){

            parking.space?.let {
                imageSpace.setImageResource(it.image)
                if (it.mirrorHorizontal) imageSpace.scaleX = -1f
                if (it.mirrorVertical) imageSpace.scaleY = -1f
            }

            parking.car?.let {
                imageCar.setImageResource(it.image)
            }
        }

    }

    fun removeLastRow(){
        for (i in 0..28) items.removeAt(items.size-1)
        notifyDataSetChanged()
    }
}
