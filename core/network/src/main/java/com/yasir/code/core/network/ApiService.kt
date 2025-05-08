package com.yasir.code.core.network

import com.yasir.code.core.network.model.NetworkDogBreedsResponse
import com.yasir.code.core.network.model.NetworkUser
import retrofit2.Response
import retrofit2.http.GET

// TODO: Rename
interface ApiService {
    @GET("breeds/list/all")
    suspend fun fetchDogBreeds(): Response<NetworkDogBreedsResponse>
    
}