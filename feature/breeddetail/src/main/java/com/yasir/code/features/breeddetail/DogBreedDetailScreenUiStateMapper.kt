package com.yasir.code.features.breeddetail

import com.yasir.code.common.capitalize
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState
import javax.inject.Inject

class DogBreedDetailScreenUiStateMapper @Inject constructor() {
    fun map(breed: DogBreed, result: Result<DogBreedImages>): DogBreedDetailScreenUiState =
        when (result) {
            is Result.Success -> DogBreedDetailScreenUiState.DogBreedDetailUiState(
                mapTitle(breed),
                result.data.images
            )

            is Result.Failure -> DogBreedDetailScreenUiState.Error(mapTitle(breed), result.message)
        }

    fun mapTitle(breed: DogBreed): String =
        when {
            breed.subType.isNotEmpty() -> "${breed.subType.capitalize()} ${breed.name.capitalize()}"

            else -> breed.name.capitalize()
        }


//    @VisibleForTesting
//    fun mapError(result: Result.Failure<DogBreedImages>): DogBreedDetailScreenUiState.Error {
//        when {
//            result.error?
//        }
//    }

}