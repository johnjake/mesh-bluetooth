package app.bluetooth.utilities.manager

import android.content.Context
import live.ditto.Ditto
import live.ditto.DittoCollection
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies
import javax.inject.Inject

class DittoManager @Inject constructor(private val context: Context) {
    lateinit var collectionManager: DittoCollection
    lateinit var ditto: Ditto
    private lateinit var dittoManager: Ditto
    private fun builder(
        appId: String = "f2b5f038-6d00-433a-9176-6e84011da136",
        token: String = "545717fe-6ffc-4e9f-ab47-7b500430a6ce"
    ): Ditto {
        val dependency = DefaultAndroidDittoDependencies(context)
        val dittoInstance = Ditto(
            dependency,
            DittoIdentity.OnlinePlayground(
                dependency,
                appId,
                token,
                enableDittoCloudSync = true
            )
        )
        ditto = dittoInstance
        return dittoInstance
    }

    fun instance(): Ditto {
        dittoManager = builder()
        return dittoManager
    }

    fun startDitto() {
        ditto.startSync()
    }
}