package com.fusaimoe.minimap.example

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflate(layoutRes: Int) = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun Drawable.mirror(vertical: Boolean, horizontal: Boolean) = setBounds(
    0, 0,
    if (horizontal) this.intrinsicWidth * -1 else this.intrinsicWidth,
    if (vertical) this.intrinsicHeight * -1 else this.intrinsicHeight
)