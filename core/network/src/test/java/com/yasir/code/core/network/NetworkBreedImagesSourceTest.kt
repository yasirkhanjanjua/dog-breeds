package com.yasir.code.core.network

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.network.model.NetworkBreedImage
import com.yasir.code.core.network.model.NetworkBreedImages
import com.yasir.code.core.network.model.NetworkDogBreedsResponse
import com.yasir.code.test.util.str
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import retrofit2.Response

class NetworkBreedImagesSourceTest {

    private val mockApiService: ApiService = mockk()
    private val mockNetworkResponseHandler: NetworkResponseHandler = mockk()
    private lateinit var sut: NetworkBreedImagesSource
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = NetworkBreedImagesSource(
            mockApiService,
            mockNetworkResponseHandler
        )
    }


    @Test
    fun fetchBreeds() = runTest {
        val fixtBreed = fixture.str()
        val fixtNetworkBreedImages = fixture.create(NetworkBreedImages::class.java)

        coEvery { mockApiService.fetchBreedImages(fixtBreed) } returns Response.success(
            fixtNetworkBreedImages
        )
        val fixtResult: Result.Success<NetworkBreedImages> =
            Result.Success(fixtNetworkBreedImages)
        coEvery { mockNetworkResponseHandler.handle<NetworkBreedImages>(any()) } returns fixtResult

        val breedsResult: Result<NetworkBreedImages> = sut.fetchBreedImages(fixtBreed)

        MatcherAssert.assertThat(breedsResult, `is`(instanceOf(Result.Success::class.java)))
        val successResult = breedsResult as Result.Success
        MatcherAssert.assertThat(successResult.data, `is`(fixtNetworkBreedImages))
    }

    @Test
    fun fetchBreedImage() = runTest {
        val fixtBreed = fixture.str()
        val fixtNetworkBreedImage = fixture.create(NetworkBreedImage::class.java)

        coEvery { mockApiService.fetchBreedImage(fixtBreed) } returns Response.success(
            fixtNetworkBreedImage
        )
        val fixtResult: Result.Success<NetworkBreedImage> =
            Result.Success(fixtNetworkBreedImage)
        coEvery { mockNetworkResponseHandler.handle<NetworkBreedImage>(any()) } returns fixtResult

        val breedsResult: Result<NetworkBreedImage> = sut.fetchBreedImage(fixtBreed)

        MatcherAssert.assertThat(breedsResult, `is`(instanceOf(Result.Success::class.java)))
        val successResult = breedsResult as Result.Success
        MatcherAssert.assertThat(successResult.data, `is`(fixtNetworkBreedImage))
    }

}