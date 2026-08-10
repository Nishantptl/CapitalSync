package com.nishant.capitalsync.di

import com.nishant.capitalsync.data.repository.CapitalSyncRepositoryImpl
import com.nishant.capitalsync.data.repository.PreferenceRepositoryImpl
import com.nishant.capitalsync.domain.repository.CapitalSyncRepository
import com.nishant.capitalsync.domain.repository.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindCapitalSyncRepository(
        capitalSyncRepositoryImpl: CapitalSyncRepositoryImpl
    ): CapitalSyncRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        preferenceRepositoryImpl: PreferenceRepositoryImpl
    ): PreferenceRepository
}