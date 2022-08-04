package app.bluetooth.utilities.data

import live.ditto.DittoDocument
import live.ditto.DittoLiveQueryEvent

data class DittoLiveQueryEmission(
    val documents: List<DittoDocument>,
    val event: DittoLiveQueryEvent
)