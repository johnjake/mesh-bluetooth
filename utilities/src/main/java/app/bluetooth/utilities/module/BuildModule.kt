package app.bluetooth.utilities.module

import app.bluetooth.utilities.manager.DittoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BuildModule {
    @Singleton
    @Provides
    fun provideDittoManager() = DittoManager()
}
