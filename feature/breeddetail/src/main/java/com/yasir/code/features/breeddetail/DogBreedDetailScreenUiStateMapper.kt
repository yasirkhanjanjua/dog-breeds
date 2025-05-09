package com.yasir.code.features.breeddetail

import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState
import javax.inject.Inject

class DogBreedDetailScreenUiStateMapper @Inject constructor() {

    fun map(result: Result<DogBreedImages>): DogBreedDetailScreenUiState =
        when(result) {
            is Result.Success -> DogBreedDetailScreenUiState.DogBreedDetailUiState(result.data.images)
            is Result.Failure -> DogBreedDetailScreenUiState.Error(result.message)
        }

//    @VisibleForTesting
//    fun mapError(result: Result.Failure<DogBreedImages>): DogBreedDetailScreenUiState.Error {
//        when {
//            result.error?
//        }
//    }

}