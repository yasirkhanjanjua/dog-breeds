package com.yasir.code.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DogBreed(
    val name: String,
    val subType: String
)