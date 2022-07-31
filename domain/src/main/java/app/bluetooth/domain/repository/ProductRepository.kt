package app.bluetooth.domain.repository

import app.bluetooth.domain.data.Products
import kotlinx.coroutines.flow.Flow
import live.ditto.DittoDocumentID
import live.ditto.DittoUpdateResult

class ProductRepository : ProductAction {
    override suspend fun addProduct(product: Products): DittoDocumentID {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Products): Flow<List<DittoUpdateResult>> {
        TODO("Not yet implemented")
    }
}