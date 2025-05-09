package com.yasir.code.features.breeddetail.model

sealed class DogBreedDetailScreenUiState(
    open val title: String
) {
     class DogBreedDetailUiState(
         override val title: String,
         val images: List<String>,
    ) : DogBreedDetailScreenUiState(title)

    data class Loading(override val title: String) : DogBreedDetailScreenUiState(title)

    data class Error(override val title: String, val message: String) : DogBreedDetailScreenUiState(title)
}