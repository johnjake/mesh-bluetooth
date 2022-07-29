package app.bluetooth.utilities.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import live.ditto.Ditto
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies

abstract class DittoBuilder() {
    companion object {
        fun getInstance(
            @ApplicationContext context: Context
        ): Ditto = build(context)

        private fun build(
            context: Context,
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
}
