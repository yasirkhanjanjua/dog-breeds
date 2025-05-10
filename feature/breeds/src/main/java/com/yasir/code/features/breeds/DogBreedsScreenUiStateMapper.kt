package com.yasir.code.features.breeds

import androidx.annotation.VisibleForTesting
import com.yasir.code.common.R
import com.yasir.code.common.StringLoader
import com.yasir.code.common.capitalize
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeds.model.DogBreedUiState
import com.yasir.code.features.breeds.model.DogBreedsScreenUiState
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class DogBreedsScreenUiStateMapper @Inject constructor(
    private val stringLoader: StringLoader
) {
    fun map(result: Result<List<DogBreedWithImage>>): DogBreedsScreenUiState {
        return when (result) {
            is Result.Success -> mapSuccess(result)
            is Result.Failure -> mapFailure(result)
        }
    }

    fun mapError(error: Throwable) = when (error) {
        is UnknownHostException -> DogBreedsScreenUiState.Error(stringLoader.getString(R.string.no_internet))
        is IOException -> DogBreedsScreenUiState.Error(stringLoader.getString(R.string.network_error))
        else -> DogBreedsScreenUiState.Error(stringLoader.getString(R.string.unknown_error))
    }

    @VisibleForTesting
    fun mapSuccess(result: Result.Success<List<DogBreedWithImage>>): DogBreedsScreenUiState.DogBreedsUiState {
        val breeds: List<DogBreedUiState> = result.data.flatMap { breed: DogBreedWithImage ->
            mapDogBreedUiStates(breed)
        }
        return DogBreedsScreenUiState.DogBreedsUiState(
            breeds.sortedBy { it.name }
        )
    }

    @VisibleForTesting
    fun mapDogBreedUiStates(breed: DogBreedWithImage): List<DogBreedUiState> =
        when {
            breed.breed.subType.isNotEmpty() -> listOf(
                DogBreedUiState(
                    breed = breed.breed,
                    name = "${breed.breed.subType.capitalize()} ${breed.breed.name.capitalize()}",
                    image = breed.image
                )
            )

            else -> listOf(
                DogBreedUiState(
                    breed = breed.breed,
                    name = breed.breed.name.capitalize(),
                    image = breed.image
                )
            )
        }


    @VisibleForTesting
    fun mapFailure(result: Result.Failure<List<DogBreedWithImage>>): DogBreedsScreenUiState.Error =
        result.error?.let {
            mapError(it)
        } ?: DogBreedsScreenUiState.Error(result.message)
}