package com.yasir.code.core.domain

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.repository.DogBreedImagesRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class GetDogBreedImagesUseCaseTest {
    private val mockDogBreedImagesRepository: DogBreedImagesRepository = mockk()
    private lateinit var sut: GetDogBreedImagesUseCase
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = GetDogBreedImagesUseCase(mockDogBreedImagesRepository)
    }

    @Test
    fun invoke() = runTest {
        val fixtBreed = fixture.create(DogBreed::class.java)
        val fixtDogBreedImages = fixture.create(DogBreedImages::class.java)
        every { mockDogBreedImagesRepository.fetchDogBreedImages(fixtBreed) } returns flowOf(Result.Success(fixtDogBreedImages))

        val breedImagesResult = sut.invoke(fixtBreed).first()

        assertThat(breedImagesResult, `is`(instanceOf(Result.Success::class.java)))
        assertThat((breedImagesResult as Result.Success).data, `is`(breedImagesResult.data))
    }
}