package com.yasir.code.core.domain

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedImagesRepository
import com.yasir.code.core.domain.repository.DogBreedsRepository
import com.yasir.code.test.MainDispatcherRule
import com.yasir.code.test.util.str
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetDogBreedsUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val mockDogBreedsRepository: DogBreedsRepository = mockk()
    private val mockDogBreedImagesRepository: DogBreedImagesRepository = mockk()
    private lateinit var sut: GetDogBreedsUseCase
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = GetDogBreedsUseCase(
            mockDogBreedsRepository,
            mockDogBreedImagesRepository
        )
    }

    @Test
    fun `fetches breeds and images for each`() = runTest {
        val spy: GetDogBreedsUseCase = spyk(sut)

        val fixtBreedWithImage = fixture.create(DogBreedWithImage::class.java)
        coEvery { spy.fetchBreedsWithImages(this@runTest) } returns Result.Success(listOf(fixtBreedWithImage))

        val result = spy.invoke(this).first()

        assert(result is Result.Success)
        assertThat((result as Result.Success).data, `is`(listOf(fixtBreedWithImage)))
    }

    @Test
    fun `fetchBreedsWithImages fetches breeds followed by fetching image for each and returns breeds with image`() = runTest {
        val fixtBreed = fixture.create(DogBreed::class.java)
        coEvery { mockDogBreedsRepository.fetchBreeds() } returns Result.Success(listOf(fixtBreed))

        val spy: GetDogBreedsUseCase = spyk(sut)
        val fixtBreedWithImage = fixture.create(DogBreedWithImage::class.java)
        coEvery { spy.fetchImages(listOf(fixtBreed), this@runTest) } returns listOf(fixtBreedWithImage)

        val breedsWithImageResult: Result<List<DogBreedWithImage>> = spy.fetchBreedsWithImages(this)

        advanceUntilIdle()

        assert(breedsWithImageResult is Result.Success)
        assertThat((breedsWithImageResult as Result.Success).data, `is`(listOf(fixtBreedWithImage)))
    }

    @Test
    fun `fetchBreedsWithImages fetches breeds followed by fetching image for each and returns breeds failure on error`() = runTest {
        val fixtBreed = fixture.create(DogBreed::class.java)
        coEvery { mockDogBreedsRepository.fetchBreeds() } returns Result.Failure()

        val spy: GetDogBreedsUseCase = spyk(sut)
        val fixtBreedWithImage = fixture.create(DogBreedWithImage::class.java)
        coEvery { spy.fetchImages(listOf(fixtBreed), this@runTest) } returns listOf(fixtBreedWithImage)

        val breedsWithImageResult: Result<List<DogBreedWithImage>> = spy.fetchBreedsWithImages(this)

        advanceUntilIdle()

        assert(breedsWithImageResult is Result.Failure)
    }

    @Test
    fun `fetchImages fetches image for each breed and returns breed with image`() = runTest {
        val fixtImageUrl = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java)
        coEvery { mockDogBreedImagesRepository.fetchDogBreedImage(fixtBreed) } returns Result.Success(fixtImageUrl)

        val breedsWithImage = sut.fetchImages(listOf(fixtBreed), this)

        advanceUntilIdle()

        assertThat(breedsWithImage.first().breed, `is`(fixtBreed))
        assertThat(breedsWithImage.first().image, `is`(fixtImageUrl))
    }

    @Test
    fun `fetchImages fetches image for each breed and returns breed with empty image on failure`() = runTest {
        val fixtBreed = fixture.create(DogBreed::class.java)
        coEvery { mockDogBreedImagesRepository.fetchDogBreedImage(fixtBreed) } returns Result.Failure()

        val breedsWithImage = sut.fetchImages(listOf(fixtBreed), this)

        advanceUntilIdle()

        assertThat(breedsWithImage.first().breed, `is`(fixtBreed))
        assertThat(breedsWithImage.first().image, `is`(""))
    }
}