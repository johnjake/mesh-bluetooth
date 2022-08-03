package app.bluetooth.mesh.features.dialog

import androidx.lifecycle.viewModelScope
import app.bluetooth.domain.data.Products
import app.bluetooth.domain.repository.ProductRepository
import app.bluetooth.mesh.bases.BaseViewModel
import app.bluetooth.mesh.features.product.ProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val repository: ProductRepository
) : BaseViewModel() {

    private val dittoAddFlow: MutableSharedFlow<ProductState> = MutableSharedFlow(replay = 1)
    val productState: SharedFlow<ProductState> = dittoAddFlow

    fun insertProduct(product: Products) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                showErrorMessage(true, error)
            }
        ) {
            dittoAddFlow.emit(ProductState.ShowLoader)
            val dittoId = repository.addProduct(product)
            dittoAddFlow.emit(ProductState.OnInsertSuccess(dittoId.value.toString()))
        }
    }
}