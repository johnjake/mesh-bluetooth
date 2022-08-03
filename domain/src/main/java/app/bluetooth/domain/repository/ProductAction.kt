package app.bluetooth.domain.repository

import app.bluetooth.domain.data.Products
import live.ditto.DittoDocumentID

interface ProductAction {
    suspend fun addProduct(product: Products): DittoDocumentID
}