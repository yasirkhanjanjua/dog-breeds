package com.yasir.code.core.network.model

import com.yasir.code.core.domain.model.DogBreed

data class NetworkDogBreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

fun NetworkDogBreedsResponse.toDogBreeds(): List<DogBreed> {
    return message.map { (key, value) ->
        DogBreed(
            name = key,
            subTypes = value
        )
    }
}

