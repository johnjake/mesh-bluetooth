package app.bluetooth.mesh.features.product

import live.ditto.DittoDocument

sealed class ProductState {
    object ShowLoader : ProductState()
    object HideLoader : ProductState()
    data class OnInsertSuccess(val documentId: String) : ProductState()
    data class OnFailed(val error: String) : ProductState()
    data class OnDittoList(val data: List<DittoDocument>) : ProductState()
    data class UpdateDocument(val data: List<DittoDocument>) : ProductState()
}