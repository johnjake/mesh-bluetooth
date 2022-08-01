package app.bluetooth.utilities.extension

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.lifecycle.LifecycleCoroutineScope

fun View.gone() {
    this.visibility = GONE
}

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}

fun View.setVisible(visible: Boolean, resize: Boolean) {
    this.visibility = if (visible) VISIBLE else if (resize) GONE else INVISIBLE
}

fun View.setEnable(enable: Boolean) {
    this.isEnabled = enable
}

fun Array<View>.invisibleViews() {
    this.map {
        it.invisible()
    }
}

/**
 * This extension function use for triggering suspend function with view click listener
 **/
fun View.launchIn(lifecycleScope: LifecycleCoroutineScope, uiAction: suspend () -> Unit) {
    this.setOnClickListener {
        lifecycleScope.launchWhenCreated {
            uiAction()
        }
    }
}

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}

fun View.setCustomPadding(
    start: Int = this.paddingStart,
    top: Int = this.paddingTop,
    end: Int = this.paddingEnd,
    bottom: Int = this.paddingBottom,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setPadding(start, top, end, bottom)
    }
}

fun View.setCustomHeight(
    customHeight: Int = this.height
) {
    layoutParams = (layoutParams as ViewGroup.LayoutParams).apply {
        height = customHeight
    }
}

fun View.toolbarChangeColor(scrollBoundary: Int = 0, isScrolled: (Boolean) -> Unit) {
    this.setOnScrollChangeListener { _, _, scrollY, _, _ ->
        isScrolled(
            scrollY > resources.getDimension(scrollBoundary)
        )
    }
}

fun convertDpToPixel(dp: Int, context: Context): Float {
    return dp * (
        context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT
        )
}
