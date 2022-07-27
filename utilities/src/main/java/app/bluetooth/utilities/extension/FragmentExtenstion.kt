package app.bluetooth.utilities

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(
    dest: NavDirections,
    navigatorExtras: Navigator.Extras? = null
) {
    if (isAdded) {
        if (navigatorExtras != null) {
            findNavController()
                .navigate(
                    dest,
                    navigatorExtras
                )
        } else {
            findNavController().navigate(
                dest
            )
        }
    } else {
        throw IllegalStateException(" does not have a NavController set")
    }
}

fun Fragment.navigateUp() = if (isAdded) {
    findNavController().navigateUp()
} else {
    throw IllegalStateException(" does not have a NavController set")
}

fun Fragment.popupBackStack() = if (isAdded) {
    findNavController().popBackStack()
} else {
    throw IllegalStateException(" does not have a NavController set")
}
