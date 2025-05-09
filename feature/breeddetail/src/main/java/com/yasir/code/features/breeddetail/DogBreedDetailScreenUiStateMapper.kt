package com.yasir.code.features.breeddetail

import androidx.annotation.VisibleForTesting
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState
import java.util.Locale
import javax.inject.Inject

class DogBreedDetailScreenUiStateMapper @Inject constructor() {

    fun map(breed: DogBreed, result: Result<DogBreedImages>): DogBreedDetailScreenUiState =
        when(result) {
            is Result.Success -> DogBreedDetailScreenUiState.DogBreedDetailUiState(mapTitle(breed), result.data.images)
            is Result.Failure -> DogBreedDetailScreenUiState.Error(mapTitle(breed), result.message)
        }

    fun mapTitle(breed: DogBreed) = breed.name.capitalize(Locale.getDefault())

//    @VisibleForTesting
//    fun mapError(result: Result.Failure<DogBreedImages>): DogBreedDetailScreenUiState.Error {
//        when {
//            result.error?
//        }
//    }

}