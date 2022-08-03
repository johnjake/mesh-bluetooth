package app.bluetooth.mesh.features

import androidx.lifecycle.viewModelScope
import app.bluetooth.mesh.bases.BaseViewModel
import app.bluetooth.mesh.features.product.ProductState
import app.bluetooth.utilities.manager.DittoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import live.ditto.DittoDocumentID
import live.ditto.DittoLiveQueryEvent
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val builder: DittoManager
) : BaseViewModel() {

    private val dittoAddFlow: MutableSharedFlow<ProductState> = MutableSharedFlow(replay = 1)
    val productState: SharedFlow<ProductState> = dittoAddFlow

    fun storeDittoNode() {
        builder.collectionManager = builder.collectionDitto()
    }

    fun deleteNode(id: DittoDocumentID) {
        builder.removeDocument(id)
    }

    fun insert(products: Map<String, Any?>) {
        builder.upsert(products)
    }

    fun refreshPermission() {
        builder.refreshedPermission()
    }

    fun observeDittoManager() {
        builder.dittoFindAll().observe { docs, event ->
            when (event) {
                is DittoLiveQueryEvent.Update -> {
                    viewModelScope.launch { dittoAddFlow.emit(ProductState.UpdateDocument(docs)) }
                }
                is DittoLiveQueryEvent.Initial -> {
                    viewModelScope.launch { dittoAddFlow.emit(ProductState.OnDittoList(docs)) }
                }
            }
        }
    }
}