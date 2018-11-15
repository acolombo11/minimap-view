package com.fusaimoe.minimap

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addScrollListener(func: (dx: Int, dy: Int) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            func(dx, dy)
        }
    })
}

fun RecyclerView.addLayoutChangeListener(func: () -> Unit) {
    this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        func()
    }
}