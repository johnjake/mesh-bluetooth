package app.bluetooth.domain.repository

import app.bluetooth.domain.data.Products
import app.bluetooth.utilities.extension.castToMap
import app.bluetooth.utilities.manager.DittoManager
import kotlinx.coroutines.flow.Flow
import live.ditto.DittoCollection
import live.ditto.DittoDocumentID
import live.ditto.DittoUpdateResult
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val builder: DittoManager
) : ProductAction {
    override suspend fun addProduct(product: Products): DittoDocumentID {
        val mapProduct = castToMap(product)
        return builder.upsert(mapProduct)
    }
}