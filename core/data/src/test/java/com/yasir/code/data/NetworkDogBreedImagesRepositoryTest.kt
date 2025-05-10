package com.yasir.code.data

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.network.NetworkBreedImagesSource
import com.yasir.code.core.network.model.NetworkBreedImages
import com.yasir.code.test.MainDispatcherRule
import com.yasir.code.test.util.str
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NetworkDogBreedImagesRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockNetworkBreedImagesSource: NetworkBreedImagesSource = mockk()
    private lateinit var sut: NetworkDogBreedImagesRepository
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = NetworkDogBreedImagesRepository(
            mockNetworkBreedImagesSource,
            mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `fetchDogBreedImages on success returns breed images`() = runTest {
        val fixtBreed = fixture.create(DogBreed::class.java)
        val fixtNetworkBreedImages = fixture.create(NetworkBreedImages::class.java)
            .copy(message = listOf(fixture.str(), fixture.str()))
        coEvery { mockNetworkBreedImagesSource.fetchBreedImages(fixtBreed.name) } returns Result.Success(
            fixtNetworkBreedImages
        )

        val result = sut.fetchDogBreedImages(fixtBreed).first()

        assertThat(result, `is`(instanceOf(Result.Success::class.java)))
        val successResult = result as Result.Success
        assertThat(successResult.data.images.size, `is`(fixtNetworkBreedImages.message.size))
        assertThat(successResult.data.images[0], `is`(fixtNetworkBreedImages.message[0]))
        assertThat(successResult.data.images[1], `is`(fixtNetworkBreedImages.message[1]))
    }

    @Test
    fun `fetchDogBreedImages on failure returns failure`() = runTest {
        val fixtBreed = fixture.create(DogBreed::class.java)
        val fixtErrorMsg = fixture.str()
        coEvery { mockNetworkBreedImagesSource.fetchBreedImages(fixtBreed.name) } returns Result.Failure(message = fixtErrorMsg)

        val result = sut.fetchDogBreedImages(fixtBreed).first()

        assertThat(result, `is`(instanceOf(Result.Failure::class.java)))
        val failureResult = result as Result.Failure
        assertThat(failureResult.message, `is`(fixtErrorMsg))
    }

}