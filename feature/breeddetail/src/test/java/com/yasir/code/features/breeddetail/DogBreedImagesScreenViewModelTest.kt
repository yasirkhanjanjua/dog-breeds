package com.yasir.code.features.breeddetail

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.GetDogBreedImagesUseCase
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.yasir.code.test.MainDispatcherRule
import com.yasir.code.test.util.str
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

@OptIn(ExperimentalCoroutinesApi::class)
class DogBreedImagesScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val mockGetDogBreedImagesUseCase: GetDogBreedImagesUseCase = mockk()
    private val mockDogBreedDetailScreenUiStateMapper: DogBreedDetailScreenUiStateMapper = mockk()
    private lateinit var sut: DogBreedImagesScreenViewModel
    private val fixture = JFixture()
    private lateinit var fixtBreed: DogBreed
    private lateinit var fixtTitle: String

    @Before
    fun setUp() {
        fixtBreed = fixture.create(DogBreed::class.java)

        val fixtDogBreedImages = fixture.create(DogBreedImages::class.java)
        val fixtDogBreedsResult = Result.Success(fixtDogBreedImages)
        coEvery { mockGetDogBreedImagesUseCase.invoke(fixtBreed) } returns flowOf(fixtDogBreedsResult)

        val fixtUiState = fixture.create(DogBreedDetailScreenUiState.DogBreedDetailUiState::class.java)
        every { mockDogBreedDetailScreenUiStateMapper.map(fixtBreed, fixtDogBreedsResult) } returns fixtUiState

        fixtTitle = fixture.str()
        every { mockDogBreedDetailScreenUiStateMapper.mapTitle(fixtBreed) } returns fixtTitle

        sut = DogBreedImagesScreenViewModel(
            mockGetDogBreedImagesUseCase,
            mockDogBreedDetailScreenUiStateMapper,
            fixtBreed
        )
    }

    @Test
    fun `performLoad fetches breed images and emits ui state`() = runTest {
        val fixtDogBreedImages = fixture.create(DogBreedImages::class.java)
        val fixtDogBreedsResult = Result.Success(fixtDogBreedImages)
        coEvery { mockGetDogBreedImagesUseCase.invoke(fixtBreed) } returns flowOf(fixtDogBreedsResult)

        val fixtUiState = fixture.create(DogBreedDetailScreenUiState.DogBreedDetailUiState::class.java)
        every { mockDogBreedDetailScreenUiStateMapper.map(fixtBreed, fixtDogBreedsResult) } returns fixtUiState

        fixtTitle = fixture.str()
        every { mockDogBreedDetailScreenUiStateMapper.mapTitle(fixtBreed) } returns fixtTitle

        sut.performLoad()

        advanceUntilIdle()

        assertThat(sut.uiState.value, `is`(fixtUiState))
    }

    @Test
    fun `performLoad fetches breed images and emits error state on failure`() = runTest {
        val fixtDogBreedsResult: Result<DogBreedImages> = Result.Failure()
        coEvery { mockGetDogBreedImagesUseCase.invoke(fixtBreed) } returns flowOf(fixtDogBreedsResult)

        val fixtErrorState = fixture.create(DogBreedDetailScreenUiState.Error::class.java).copy(title = fixtTitle)
        every { mockDogBreedDetailScreenUiStateMapper.map(fixtBreed, fixtDogBreedsResult) } returns fixtErrorState

        fixtTitle = fixture.str()
        every { mockDogBreedDetailScreenUiStateMapper.mapTitle(fixtBreed) } returns fixtTitle

        sut.performLoad()

        advanceUntilIdle()

        assertThat(sut.uiState.value, `is`(fixtErrorState))
    }
}