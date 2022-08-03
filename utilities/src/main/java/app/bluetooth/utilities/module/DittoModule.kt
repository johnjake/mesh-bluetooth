package app.bluetooth.utilities.module

import android.content.Context
import app.bluetooth.utilities.manager.DittoInstance
import app.bluetooth.utilities.manager.DittoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DittoModule {
    @Singleton
    @Provides
    fun provideDittoManager(builder: DittoInstance) = DittoManager(builder)

    @Singleton
    @Provides
    fun provideDittoInstance(@ApplicationContext context: Context) = DittoInstance(context)
}
