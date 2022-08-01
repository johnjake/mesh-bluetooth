package app.bluetooth.domain.data

import app.bluetooth.utilities.EMPTY
import live.ditto.DittoDocument
import java.util.UUID

data class Products(
    var _id: String? = UUID.randomUUID().toString(),
    val category: String? = EMPTY,
    val name: String? = EMPTY,
    val imageUrl: String? = EMPTY,
    val price: Double? = 0.0,
    val description: String? = EMPTY
) {
    constructor(document: DittoDocument) :
        this(
            document["_id"].stringValue,
            document["category"].stringValue,
            document["name"].stringValue,
            document["imageUrl"].stringValue,
            document["price"].doubleValue,
            document["description"].stringValue
        )
}