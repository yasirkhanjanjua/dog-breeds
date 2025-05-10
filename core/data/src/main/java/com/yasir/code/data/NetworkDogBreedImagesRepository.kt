package com.yasir.code.data

import com.yasir.code.common.di.IoDispatcher
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedImagesRepository
import com.yasir.code.core.network.NetworkBreedImagesSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkDogBreedImagesRepository @Inject constructor(
    private val networkBreedImagesSource: NetworkBreedImagesSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DogBreedImagesRepository {

    override fun fetchDogBreedImages(breed: DogBreed): Flow<Result<DogBreedImages>> = flow {
        when (val response = networkBreedImagesSource.fetchBreedImages(breed.name)) {
            is Result.Success -> {
                val imagesResponse = Result.Success(DogBreedImages(response.data.message))
                emit(imagesResponse)
            }

            is Result.Failure -> {
                emit(Result.Failure(response.error, response.message))
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun fetchDogBreedImage(breed: DogBreed): Result<String> =
        withContext(ioDispatcher) {
            when (val result = networkBreedImagesSource.fetchBreedImage(breed.name)) {
                is Result.Success -> Result.Success(result.data.message)
                is Result.Failure -> Result.Failure(result.error, result.message)
            }
        }
}