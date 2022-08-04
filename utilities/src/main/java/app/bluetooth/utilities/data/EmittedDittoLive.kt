package app.bluetooth.utilities.data

import live.ditto.DittoDocument

data class EmittedDittoLive(
    val isInitial: Boolean? = false,
    val documents: List<DittoDocument>? = emptyList()
)