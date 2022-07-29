package app.bluetooth.utilities.module

import android.content.Context
import app.bluetooth.utilities.utils.DittoBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BuildModule {
    @Singleton
    @Provides
    fun provideDittoBuild(
        @ApplicationContext contexts: Context
    ) =
        DittoBuilder.getInstance(contexts)
}
