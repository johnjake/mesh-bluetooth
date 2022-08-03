package app.bluetooth.utilities.manager

import android.content.Context
import live.ditto.Ditto
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies
import javax.inject.Inject

class DittoInstance @Inject constructor(private val context: Context) {
    fun instance(
        appId: String = "f2b5f038-6d00-433a-9176-6e84011da136",
        token: String = "545717fe-6ffc-4e9f-ab47-7b500430a6ce"
    ): Ditto {
        val dependency = DefaultAndroidDittoDependencies(context)
        return Ditto(
            dependency,
            DittoIdentity.OnlinePlayground(
                dependency,
                appId,
                token,
                enableDittoCloudSync = true
            )
        )
    }
}