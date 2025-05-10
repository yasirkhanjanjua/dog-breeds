package com.yasir.code.data

import com.yasir.code.common.di.IoDispatcher
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.network.DogBreedsNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import  com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedsRepository
import com.yasir.code.core.network.model.toDogBreeds
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class OnlineDogBreedsRepository @Inject constructor(
    private val dogBreedsNetworkDataSource: DogBreedsNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DogBreedsRepository {

    override suspend fun fetchBreeds(): Result<List<DogBreed>> =
        withContext(ioDispatcher) {
            when (val response = dogBreedsNetworkDataSource.fetchBreeds()) {
                is Result.Success -> {
                    Result.Success(response.data.toDogBreeds())
                }

                is Result.Failure -> {
                    Result.Failure(response.error, response.message)
                }
            }
        }
}