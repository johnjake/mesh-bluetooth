package app.bluetooth.mesh.bases

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking

open class BaseViewModel : ViewModel() {
    fun emitState(
        emitAction: suspend () -> Unit
    ) = runBlocking {
        emitAction()
    }

    open fun showErrorMessage(withHideLoader: Boolean = true, errorMsg: Throwable) {}
}
