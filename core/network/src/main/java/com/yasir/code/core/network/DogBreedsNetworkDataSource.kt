package com.yasir.code.core.network

import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.network.model.NetworkDogBreedsResponse
import javax.inject.Inject

class DogBreedsNetworkDataSource @Inject constructor(
    private val apiService: ApiService,
    private val networkResponseHandler: NetworkResponseHandler
) {
    suspend fun fetchBreeds(): Result<NetworkDogBreedsResponse> {
       return  networkResponseHandler.handle {
            apiService.fetchDogBreeds()
        }
    }
}