package com.yasir.code.core.network

import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.network.model.NetworkBreedImage
import com.yasir.code.core.network.model.NetworkBreedImages
import javax.inject.Inject

class NetworkBreedImagesSource @Inject constructor(
    private val apiService: ApiService,
    private val networkResponseHandler: NetworkResponseHandler
) {
    suspend fun fetchBreedImages(breed: String): Result<NetworkBreedImages> {
        return networkResponseHandler.handle {
            apiService.fetchBreedImages(breed)
        }
    }

    suspend fun fetchBreedImage(breed: String): Result<NetworkBreedImage> {
        return networkResponseHandler.handle {
            apiService.fetchBreedImage(breed)
        }
    }
}