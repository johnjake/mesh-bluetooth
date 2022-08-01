package app.bluetooth.domain.repository

import app.bluetooth.domain.data.Products
import kotlinx.coroutines.flow.Flow
import live.ditto.DittoDocumentID
import live.ditto.DittoUpdateResult

interface ProductAction {
    suspend fun addProduct(product: Products): DittoDocumentID
    suspend fun updateProduct(product: Products): Flow<List<DittoUpdateResult>>
}