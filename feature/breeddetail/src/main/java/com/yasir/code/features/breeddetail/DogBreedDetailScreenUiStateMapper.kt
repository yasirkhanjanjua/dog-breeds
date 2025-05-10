package com.yasir.code.features.breeddetail

import androidx.annotation.VisibleForTesting
import com.yasir.code.common.R
import com.yasir.code.common.StringLoader
import com.yasir.code.common.capitalize
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class DogBreedDetailScreenUiStateMapper @Inject constructor(private val stringLoader: StringLoader) {
    fun map(breed: DogBreed, result: Result<DogBreedImages>): DogBreedDetailScreenUiState =
        when (result) {
            is Result.Success -> DogBreedDetailScreenUiState.DogBreedDetailUiState(
                mapTitle(breed),
                limitImages(result, MAXIMUM_IMAGES_COUNT)
            )

            is Result.Failure -> DogBreedDetailScreenUiState.Error(mapTitle(breed), result.message)
        }

    fun mapError(breed: DogBreed, error: Throwable) = when (error) {
        is UnknownHostException -> DogBreedDetailScreenUiState.Error(mapTitle(breed), stringLoader.getString(R.string.no_internet))
        is IOException -> DogBreedDetailScreenUiState.Error(mapTitle(breed), stringLoader.getString(R.string.network_error))
        else -> DogBreedDetailScreenUiState.Error(mapTitle(breed), stringLoader.getString(R.string.unknown_error))
    }

    fun mapTitle(breed: DogBreed): String =
        when {
            breed.subType.isNotEmpty() -> "${breed.subType.capitalize()} ${breed.name.capitalize()}"

            else -> breed.name.capitalize()
        }

    @VisibleForTesting
    fun limitImages(result: Result.Success<DogBreedImages>, selection: Int) =
        when {
            result.data.images.size > selection -> result.data.images.shuffled().take(selection)
            else -> result.data.images
        }

    companion object {
        private const val MAXIMUM_IMAGES_COUNT = 10
    }

}