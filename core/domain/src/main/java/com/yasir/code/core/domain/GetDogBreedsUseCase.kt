package com.yasir.code.core.domain

import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDogBreedsUseCase @Inject constructor(
    private val dogBreedsRepository: DogBreedsRepository
) {
    operator fun invoke(): Flow<Result<List<DogBreed>>> =
        dogBreedsRepository.fetchDogBreeds()
}