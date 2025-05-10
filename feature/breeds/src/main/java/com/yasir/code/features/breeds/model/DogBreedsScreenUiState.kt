package com.yasir.code.features.breeds.model

sealed class DogBreedsScreenUiState {

    data class DogBreedsUiState(
        val breeds: List<DogBreedUiState>
    ) : DogBreedsScreenUiState()

    data object Loading : DogBreedsScreenUiState()

    data class Error(val message: String) : DogBreedsScreenUiState()
}

