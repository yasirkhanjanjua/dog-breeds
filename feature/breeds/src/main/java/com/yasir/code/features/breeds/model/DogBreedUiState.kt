package com.yasir.code.features.breeds.model

import com.yasir.code.core.domain.model.DogBreed

data class DogBreedUiState(
    val breed: DogBreed,
    val name: String,
    val image: String
)