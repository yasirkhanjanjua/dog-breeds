package com.yasir.code.core.domain.repository

import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface DogBreedImagesRepository {
    fun fetchDogBreedImages(breed: DogBreed): Flow<Result<DogBreedImages>>
    suspend fun fetchDogBreedImage(breed: DogBreed): Result<String>
}