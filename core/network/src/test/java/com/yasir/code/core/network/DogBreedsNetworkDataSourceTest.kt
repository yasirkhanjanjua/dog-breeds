package com.yasir.code.core.network

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.network.model.NetworkDogBreedsResponse
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`

import org.junit.Before
import org.junit.Test
import retrofit2.Response

class DogBreedsNetworkDataSourceTest {

    private val mockApiService: ApiService = mockk()
    private val mockNetworkResponseHandler: NetworkResponseHandler = mockk()
    private lateinit var sut: DogBreedsNetworkDataSource
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = DogBreedsNetworkDataSource(
            mockApiService, mockNetworkResponseHandler
        )
    }


    @Test
    fun fetchBreeds() = runTest {
        val fixtNetworkDogBreeds = mapOf(
            "bulldog" to listOf("american", "british"),
            "german shepherd" to listOf()
        )
        val fixtNetworkBreedsResponse = NetworkDogBreedsResponse(
            fixtNetworkDogBreeds, "OK"
        )
        coEvery { mockApiService.fetchDogBreeds() } returns Response.success(fixtNetworkBreedsResponse)
        val fixtResult: Result.Success<NetworkDogBreedsResponse> = Result.Success(fixtNetworkBreedsResponse)
        coEvery { mockNetworkResponseHandler.handle<NetworkDogBreedsResponse>(any()) } returns fixtResult

        val breedsResult: Result<NetworkDogBreedsResponse> = sut.fetchBreeds()

        assertThat(breedsResult, `is`(instanceOf(Result.Success::class.java)))
        val successResult = breedsResult as Result.Success
        assertThat(successResult.data, `is`(fixtNetworkBreedsResponse))
    }

}