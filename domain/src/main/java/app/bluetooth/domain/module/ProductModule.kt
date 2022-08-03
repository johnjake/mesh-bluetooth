package app.bluetooth.domain.module

import app.bluetooth.domain.repository.ProductAction
import app.bluetooth.domain.repository.ProductRepository
import app.bluetooth.utilities.manager.DittoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class ProductModule {
    @FragmentScoped
    @Provides
    fun provideDittoRepository(
        dittoManager: DittoManager
    ): ProductAction = ProductRepository(dittoManager)
}