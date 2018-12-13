package eu.acolombo.minimap.example

import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflate(layoutRes: Int) = LayoutInflater.from(context).inflate(layoutRes, this, false)