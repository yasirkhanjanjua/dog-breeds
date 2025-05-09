package com.yasir.code.core.network.model

import com.yasir.code.core.domain.model.DogBreed

data class NetworkDogBreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

fun NetworkDogBreedsResponse.toDogBreeds(): List<DogBreed> {
    return message.flatMap { (breed, subTypes) ->
        when {
            subTypes.isNotEmpty() -> subTypes.map { DogBreed(breed, it) }
            else -> listOf(DogBreed(breed, ""))
        }
    }
}

