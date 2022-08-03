package app.bluetooth.mesh.features.product

import live.ditto.DittoDocument
import live.ditto.DittoDocumentID

sealed class ProductState {
    object ShowLoader : ProductState()
    object HideLoader : ProductState()
    data class OnSuccess(val data: DittoDocumentID) : ProductState()
    data class OnFailed(val error: String) : ProductState()
    data class OnDittoList(val data: List<DittoDocument>) : ProductState()
    data class UpdateDocument(val data: List<DittoDocument>) : ProductState()
}