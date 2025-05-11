package com.yasir.code.core.domain

import androidx.annotation.VisibleForTesting
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedImagesRepository
import com.yasir.code.core.domain.repository.DogBreedsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.yasir.code.common.di.IoDispatcher
import javax.inject.Inject

class GetDogBreedsUseCase @Inject constructor(
    private val dogBreedsRepository: DogBreedsRepository,
    private val dogBreedImagesRepository: DogBreedImagesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(coroutineScope: CoroutineScope): Flow<Result<List<DogBreedWithImage>>> =
        flow { emit(fetchBreedsWithImages(coroutineScope)) }

    @VisibleForTesting
    suspend fun fetchBreedsWithImages(coroutineScope: CoroutineScope): Result<List<DogBreedWithImage>> {
        return when (val breedsResult = dogBreedsRepository.fetchBreeds()) {
            is Result.Success -> Result.Success(fetchImages(breedsResult.data, coroutineScope))
            is Result.Failure -> Result.Failure(
                breedsResult.error,
                breedsResult.message
            )
        }
    }

    @VisibleForTesting
    suspend fun fetchImages(
        breeds: List<DogBreed>,
        coroutineScope: CoroutineScope
    ): List<DogBreedWithImage> =
        breeds.map { breed ->
            coroutineScope.async(ioDispatcher) {
                when (val imageResult = dogBreedImagesRepository.fetchDogBreedImage(breed)) {
                    is Result.Success -> DogBreedWithImage(breed, imageResult.data)
                    is Result.Failure -> DogBreedWithImage(breed, "")
                }
            }
        }.awaitAll()
}