package com.yasir.code.features.breeds

import androidx.lifecycle.viewModelScope
import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.GetDogBreedsUseCase
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeds.model.DogBreedUiState
import com.yasir.code.features.breeds.model.DogBreedsScreenUiState
import com.yasir.code.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class DogBreedsScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val mockGetDogBreedsUseCase: GetDogBreedsUseCase = mockk()
    private val mockDogBreedsScreenUiStateMapper: DogBreedsScreenUiStateMapper = mockk()

    private lateinit var sut: DogBreedsScreenViewModel
    private val fixture = JFixture()

    @Before
    fun setUp() {
        val fixtBreeds = listOf(
            fixture.create(DogBreedWithImage::class.java),
            fixture.create(DogBreedWithImage::class.java)
        )
        val fixtBreedsResult = Result.Success(fixtBreeds)
        coEvery { mockGetDogBreedsUseCase.invoke(any()) } returns flowOf(fixtBreedsResult)

        val fixtUiState = listOf(
            fixture.create(DogBreedUiState::class.java),
            fixture.create(DogBreedUiState::class.java)
        )
        val fixtScreenUiState = DogBreedsScreenUiState.DogBreedsUiState(fixtUiState)
        every { mockDogBreedsScreenUiStateMapper.map(fixtBreedsResult) } returns fixtScreenUiState

        sut = DogBreedsScreenViewModel(
            mockGetDogBreedsUseCase, mockDogBreedsScreenUiStateMapper
        )
    }

    @Test
    fun `initial ui state is loading`() {
        assertThat(sut.breedsState.value, `is`(DogBreedsScreenUiState.Loading))
    }

    @Test
    fun `loads breeds and populates ui state`() = runTest {
        val fixtBreeds = listOf(
            fixture.create(DogBreedWithImage::class.java),
            fixture.create(DogBreedWithImage::class.java)
        )
        val fixtBreedsResult = Result.Success(fixtBreeds)
        coEvery { mockGetDogBreedsUseCase.invoke(any()) } returns flowOf(fixtBreedsResult)

        val fixtUiState = listOf(
            fixture.create(DogBreedUiState::class.java),
            fixture.create(DogBreedUiState::class.java)
        )
        val fixtScreenUiState = DogBreedsScreenUiState.DogBreedsUiState(fixtUiState)
        every { mockDogBreedsScreenUiStateMapper.map(fixtBreedsResult) } returns fixtScreenUiState

        sut.performLoad()

        advanceUntilIdle()

        assertThat(sut.breedsState.value, `is`(fixtScreenUiState))
    }

    @Test
    fun `emits error when there is failure`() = runTest {
        val fixtError = Result.Failure<List<DogBreedWithImage>>(error = UnknownHostException())
        every { mockGetDogBreedsUseCase.invoke(sut.viewModelScope) } returns flowOf(fixtError)
        val fixtUiState = fixture.create(DogBreedsScreenUiState.Error::class.java)
        every { mockDogBreedsScreenUiStateMapper.map(fixtError) } returns fixtUiState

        sut.performLoad()

        advanceUntilIdle()

        assertThat(sut.breedsState.value, `is`(fixtUiState))
    }
}