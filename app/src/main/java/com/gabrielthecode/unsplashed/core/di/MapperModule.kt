package com.gabrielthecode.unsplashed.core.di

import com.gabrielthecode.unsplashed.core.domain.SearchDomainModel
import com.gabrielthecode.unsplashed.datasource.network.mapper.EntityMapper
import com.gabrielthecode.unsplashed.datasource.network.mapper.SearchMapper
import com.gabrielthecode.unsplashed.datasource.network.model.SearchResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Singleton
    @Provides
    fun provideSearchResponseMapper(): EntityMapper<SearchResponse, SearchDomainModel> {
        return SearchMapper()
    }
}
