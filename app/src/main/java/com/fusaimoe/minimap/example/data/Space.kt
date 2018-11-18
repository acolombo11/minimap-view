package com.fusaimoe.minimap.example.data

import androidx.annotation.DrawableRes
import com.fusaimoe.minimap.example.R

enum class Space(@DrawableRes val image: Int, val mirrorVertical: Boolean, val mirrorHorizontal: Boolean) {
    TOP_LEFT(R.drawable.space_edge, false, false),
    TOP_CENTER(R.drawable.space_center, false, false),
    TOP_RIGHT(R.drawable.space_edge, false, true),
    BOTTOM_LEFT(R.drawable.space_edge, true, false),
    BOTTOM_CENTER(R.drawable.space_center, true, false),
    BOTTOM_RIGHT(R.drawable.space_edge, true, true)
}