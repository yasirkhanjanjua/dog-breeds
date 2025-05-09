package com.yasir.code.core.domain.repository

import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface DogBreedImagesRepository {
    fun fetchDogBreedImages(breed: DogBreed): Flow<Result<DogBreedImages>>
    fun fetchDogBreedImage(breed: DogBreed): Flow<Result<String>>
    suspend fun fetchDogBreedImage_(breed: DogBreed): Result<String>
}