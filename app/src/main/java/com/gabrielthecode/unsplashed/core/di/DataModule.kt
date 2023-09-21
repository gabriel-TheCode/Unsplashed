package com.gabrielthecode.unsplashed.core.di

import com.gabrielthecode.unsplashed.application.UnsplashedDataStore
import com.gabrielthecode.unsplashed.core.local.LocalDataSource
import com.gabrielthecode.unsplashed.core.local.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object DataModule {
    /*@Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }*/

    @Singleton
    @Provides
    fun provideDataStore(dataStore: UnsplashedDataStore): LocalDataSource {
        return LocalDataSourceImpl(dataStore)
    }
}
