package com.yasir.code.core.domain.repository

import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface DogBreedsRepository {
    fun fetchDogBreeds(): Flow<Result<List<DogBreed>>>
    suspend fun fetchBreeds(): Result<List<DogBreed>>
}