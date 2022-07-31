package app.bluetooth.utilities.extension

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        with(inputMethodManager) {
            hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

/**
 * Extension method to show keyboard for [Activity].
 */
fun Activity.showSoftKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    with(inputMethodManager) {
        showSoftInput(view, 0)
    }
}

/**
 * Used for show a toast message in the UI Thread
 */
fun Activity.toast(message: String, duration: Int? = null) {
    runOnUiThread { Toast.makeText(this, message, duration ?: Toast.LENGTH_SHORT).show() }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return when {
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
        else -> false
    }
}

fun Activity.createProgressDialog(): ProgressDialog = ProgressDialog(this)

fun Activity.setWindowUiTheme(dominantColor: Int, defaultColor: Int) {
    if (dominantColor != 0) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
    window.statusBarColor = if (dominantColor != 0) dominantColor else ContextCompat.getColor(this, defaultColor)
}

fun Activity.setStatusBarColorOnScroll(colorExpanded: Int, colorCollapsed: Int): AppBarLayout.OnOffsetChangedListener {
    return AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
            if (window != null) window.statusBarColor = ContextCompat.getColor(
                this,
                colorCollapsed
            )

            // Collapsed
        } else if (verticalOffset >= -120) {
            // Expanded
            if (window != null) window.statusBarColor = ContextCompat.getColor(
                this,
                colorExpanded
            )
        } else {
            // Somewhere in between
            if (window != null) window.statusBarColor = ContextCompat.getColor(
                this,
                colorCollapsed
            )
        }
    }
}

fun Activity.setSoftInputMode(adjustResize: Boolean) {
    if (adjustResize) window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) else window.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
    )
}

