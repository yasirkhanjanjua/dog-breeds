package com.yasir.code.data.di

import com.yasir.code.core.domain.repository.DogBreedsRepository
import com.yasir.code.data.OnlineDogBreedsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DogBreedsRepositoryModule {
    @Binds
    fun bindDogBreedsRepository(
        onlineDogBreedsRepository: OnlineDogBreedsRepository,
    ): DogBreedsRepository
}