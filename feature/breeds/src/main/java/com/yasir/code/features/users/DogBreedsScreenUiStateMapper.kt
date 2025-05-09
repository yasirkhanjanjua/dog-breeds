package com.yasir.code.features.users

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.model.User
import com.yasir.code.features.users.model.DogBreedUiState
import com.yasir.code.features.users.model.DogBreedsScreenUiState
import com.yasir.code.features.users.model.UserUiState
import com.yasir.code.features.users.model.UsersScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class DogBreedsScreenUiStateMapper @Inject constructor() {

    fun map(result: Result<List<DogBreed>>): DogBreedsScreenUiState {
        return when (result) {
            is Result.Success -> mapSuccess(result)
            is Result.Failure -> mapError(result)
        }
    }

    @VisibleForTesting
    fun mapSuccess(result: Result.Success<List<DogBreed>>): DogBreedsScreenUiState.DogBreedsUiState {
        val breeds: List<DogBreedUiState> = result.data.flatMap { breed: DogBreed ->
            mapDogBreedUiStates(breed)
        }
        return DogBreedsScreenUiState.DogBreedsUiState(
            breeds
        )
    }

    private fun mapDogBreedUiStates(breed: DogBreed): List<DogBreedUiState> =
        when {
            // TODO: Names are english only
            breed.subTypes.isNotEmpty() -> breed.subTypes.map { DogBreedUiState(breed, "${it.capitalize(Locale.current)} ${breed.name}") }
            else -> listOf(DogBreedUiState(breed, breed.name.capitalize(Locale.current)))
        }

    @VisibleForTesting
    fun mapError(result: Result.Failure<List<DogBreed>>): DogBreedsScreenUiState.Error {
        return DogBreedsScreenUiState.Error(
            result.message
        )
    }

    fun map(result: Result<List<User>>): UsersScreenUiState =
        when (result) {
            is Result.Success -> mapSuccess(result)
            is Result.Failure -> mapError(result)
        }

    @VisibleForTesting
    fun mapSuccess(result: Result.Success<List<User>>): UsersScreenUiState =
        UsersScreenUiState.UsersUiState(
            result.data.map {
                UserUiState(
                    name = it.name
                )
            })

    @VisibleForTesting
    fun mapError(result: Result.Failure<List<User>>): UsersScreenUiState =
        UsersScreenUiState.Error(result.message)
}