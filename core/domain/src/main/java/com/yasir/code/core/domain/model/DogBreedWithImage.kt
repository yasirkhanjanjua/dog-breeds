package com.yasir.code.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DogBreedWithImage(
    val breed: DogBreed,
    val image: String
)