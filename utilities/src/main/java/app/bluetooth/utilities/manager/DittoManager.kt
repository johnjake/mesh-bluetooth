package app.bluetooth.utilities.manager

import live.ditto.Ditto
import live.ditto.DittoCollection
import live.ditto.DittoDocumentID
import live.ditto.DittoPendingCursorOperation
import javax.inject.Inject

class DittoManager @Inject constructor(builder: DittoInstance) {
    lateinit var collectionManager: DittoCollection
    val dittoManager: Ditto = builder.instance()

    init {
        dittoManager.startSync()
    }

    fun collectionDitto(): DittoCollection {
        return dittoManager.store.collection("products")
    }

    fun removeDocument(id: DittoDocumentID) {
        dittoManager.store.collection("products").findByID(id).remove()
    }

    fun upsert(item: Map<String, Any?>): DittoDocumentID {
        return collectionManager.upsert(item)
    }

    fun refreshedPermission() {
        dittoManager.refreshPermissions()
    }

    fun dittoFindAll(): DittoPendingCursorOperation {
        return collectionManager.findAll()
    }
}