package app.bluetooth.utilities.extension

import app.bluetooth.utilities.data.EmittedDittoLive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import live.ditto.DittoDocument
import live.ditto.DittoLiveQuery
import live.ditto.DittoLiveQueryEvent
import live.ditto.DittoPendingCursorOperation
import kotlin.coroutines.CoroutineContext

fun DittoPendingCursorOperation.asFlow(): Flow<EmittedDittoLive> {
    return callbackFlow {
        val query = observeWithCoroutineContext(coroutineContext) { documents, event ->
            when (event) {
                is DittoLiveQueryEvent.Initial -> send(
                    EmittedDittoLive(
                        isInitial = true,
                        documents = documents
                    )
                )
                else -> send(
                    EmittedDittoLive(
                        isInitial = false,
                        documents = documents
                    )
                )
            }
        }
        awaitClose { query.stop() }
    }.buffer(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
}

private inline fun DittoPendingCursorOperation.observeWithCoroutineContext(
    coroutineContext: CoroutineContext,
    crossinline onEvent: suspend (
        documents: List<DittoDocument>,
        event: DittoLiveQueryEvent
    ) -> Unit
): DittoLiveQuery {
    val coroutineScope = CoroutineScope(coroutineContext)
    return observeWithNextSignal { documents, event, nextSignal ->
        coroutineScope.launch {
            onEvent(documents, event)
            nextSignal()
        }
    }
}