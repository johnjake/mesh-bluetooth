package app.bluetooth.mesh

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FlowTest {

    @Test
    fun testFlow() {
        runBlocking {
            flow {
                for (i in 1..3) {
                    println("Emitting $i")
                    emit(i)
                }
            }.buffer(0)
                .collect {
                        value ->
                    delay(100)
                    println("Consuming $value")
                }
        }
    }

    // https://stackoverflow.com/questions/65952256/kotlin-flow-buffer-capacity#:~:text=The%20buffer%20function%20actually%20creates,executed%20by%20the%20same%20coroutine.
}
