package app.bluetooth.mesh.features

import androidx.lifecycle.viewModelScope
import app.bluetooth.mesh.bases.BaseViewModel
import app.bluetooth.mesh.features.product.ProductState
import app.bluetooth.utilities.manager.DittoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import live.ditto.Ditto
import live.ditto.DittoDocumentID
import live.ditto.DittoLiveQueryEvent
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dittoManager: DittoManager
) : BaseViewModel() {

    private val dittoAddFlow: MutableSharedFlow<ProductState> = MutableSharedFlow(replay = 1)
    val productState: SharedFlow<ProductState> = dittoAddFlow

    fun instance(): Ditto {
        return dittoManager.instance()
    }

    fun startSync() {
        dittoManager.startDitto()
    }

    fun storeDittoNode(ditto: Ditto) {
        dittoManager.collectionManager = ditto?.store?.collection("products")
    }

    fun deleteNode(id: DittoDocumentID) {
        dittoManager.ditto.store.collection("products").findByID(id).remove()
    }

    fun insert(products: Map<String, Any?>) {
        dittoManager.collectionManager.upsert(products)
    }

    fun refreshPermission(ditto: Ditto) {
        ditto.refreshPermissions()
    }

    fun observeDittoManager() {
        dittoManager.collectionManager.findAll().observe { docs, event ->
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