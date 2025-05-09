package com.yasir.code.features.breeddetail.model

sealed class DogBreedDetailScreenUiState {

    data class DogBreedDetailUiState(
        val images: List<String>
    ) : DogBreedDetailScreenUiState()

    data object Loading : DogBreedDetailScreenUiState()

    data class Error(val message: String) : DogBreedDetailScreenUiState()
}