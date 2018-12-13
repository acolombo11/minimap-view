package eu.acolombo.minimap

import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.addScrollListener(func: (dx: Int, dy: Int) -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            func(dx, dy)
        }
    })
}

fun <T: View> T.addLayoutChangeListenerHandler(func: T.() -> Unit) {
    this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        Handler().post {
            func()
        }

    }
}

fun <T: View> T.addLayoutChangeListener(func: T.() -> Unit) {
    this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        func()
    }
}

inline fun <T: View> T.afterMeasured(crossinline func: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                func()
            }
        }
    })
}