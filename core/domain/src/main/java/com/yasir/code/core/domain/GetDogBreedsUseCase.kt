package com.yasir.code.core.domain

import androidx.annotation.VisibleForTesting
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedImagesRepository
import com.yasir.code.core.domain.repository.DogBreedsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDogBreedsUseCase @Inject constructor(
    private val dogBreedsRepository: DogBreedsRepository,
    private val dogBreedImagesRepository: DogBreedImagesRepository

) {
    operator fun invoke(): Flow<Result<List<DogBreed>>> {
        return dogBreedsRepository.fetchDogBreeds()
    }

    fun test(): Flow<Result<List<DogBreedWithImage>>> {
        return invoke()
            .flatMapConcat { result: Result<List<DogBreed>> ->
                when (result) {
                    is Result.Success -> flow {
                        val urls: List<DogBreedWithImage> = result.data.map { breed: DogBreed ->
                            breed to dogBreedImagesRepository.fetchDogBreedImage_(breed)
                        }.map { (breed, urlResult) ->
                            when (urlResult) {
                                is Result.Success -> DogBreedWithImage(breed, urlResult.data)
                                is Result.Failure -> DogBreedWithImage(breed, "")
                            }
                        }
                        emit(Result.Success(urls))
                    }

                    is Result.Failure -> flow {
                        val fail: Result<List<DogBreedWithImage>> =
                            Result.Failure<List<DogBreedWithImage>>(result.error, result.message)
                        emit(fail)
                    }
                }
            }
    }

    fun fetchBreedsWithImagesFlow(coroutineScope: CoroutineScope): Flow<Result<List<DogBreedWithImage>>> = flow {
        emit(fetchBreedsWithImages(coroutineScope))
    }

    suspend fun fetchBreedsWithImages(coroutineScope: CoroutineScope): Result<List<DogBreedWithImage>> {
        return when (val breedsResult = dogBreedsRepository.fetchBreeds()) {
            is Result.Success -> Result.Success(fetchImages(breedsResult.data, coroutineScope))
            is Result.Failure -> Result.Failure<List<DogBreedWithImage>>(
                breedsResult.error,
                breedsResult.message
            )
        }
    }

    @VisibleForTesting
    suspend fun fetchImages(
        breeds: List<DogBreed>,
        coroutineScope: CoroutineScope
    ): List<DogBreedWithImage> {
        val res: List<Deferred<DogBreedWithImage>> = breeds.map { breed ->
            coroutineScope.async {
                when (val imageResult = dogBreedImagesRepository.fetchDogBreedImage_(breed)) {
                    is Result.Success -> DogBreedWithImage(breed, imageResult.data)
                    is Result.Failure -> DogBreedWithImage(breed, "")
                }
            }
        }
        return res.awaitAll()
    }
}