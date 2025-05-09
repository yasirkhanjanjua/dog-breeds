package com.yasir.code.core.network

import com.yasir.code.core.network.model.NetworkBreedImage
import com.yasir.code.core.network.model.NetworkBreedImages
import com.yasir.code.core.network.model.NetworkDogBreedsResponse
import com.yasir.code.core.network.model.NetworkUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// TODO: Rename
interface ApiService {
    @GET("breeds/list/all")
    suspend fun fetchDogBreeds(): Response<NetworkDogBreedsResponse>

    @GET("breed/{breed}/images")
    suspend fun fetchBreedImages(@Path("breed") breed: String): Response<NetworkBreedImages>

    @GET("breed/{breed}/images/random")
    suspend fun fetchBreedImage(@Path("breed") breed: String): Response<NetworkBreedImage>
}