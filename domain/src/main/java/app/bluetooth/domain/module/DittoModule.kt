package app.bluetooth.domain.module

import app.bluetooth.domain.repository.ProductAction
import app.bluetooth.domain.repository.ProductRepository
import app.bluetooth.utilities.manager.DittoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class DittoModule {
    @ActivityScoped
    @Provides
    fun provideDittoRepository(
        dittoManager: DittoManager
    ): ProductAction = ProductRepository(dittoManager)
}