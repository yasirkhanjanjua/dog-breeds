package com.yasir.code.core.domain

import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedImagesRepository
import com.yasir.code.core.domain.repository.DogBreedsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDogBreedImagesUseCase @Inject constructor(
    private val dogBreedImagesRepository: DogBreedImagesRepository
) {
    operator fun invoke(breed: DogBreed): Flow<Result<DogBreedImages>> =
        dogBreedImagesRepository.fetchDogBreedImages(breed)
}