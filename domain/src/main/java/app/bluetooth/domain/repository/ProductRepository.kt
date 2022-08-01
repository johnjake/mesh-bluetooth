package app.bluetooth.domain.repository

import app.bluetooth.domain.data.Products
import app.bluetooth.utilities.extension.castToMap
import app.bluetooth.utilities.manager.DittoManager
import kotlinx.coroutines.flow.Flow
import live.ditto.DittoDocumentID
import live.ditto.DittoUpdateResult
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val dittoManager: DittoManager
) : ProductAction {
    override suspend fun addProduct(product: Products): DittoDocumentID {
        val mapProduct = castToMap(product)
        dittoManager.startDitto()
        return dittoManager.collectionManager.upsert(mapProduct)
    }

    override suspend fun updateProduct(product: Products): Flow<List<DittoUpdateResult>> {
        TODO("Not yet implemented")
    }
}