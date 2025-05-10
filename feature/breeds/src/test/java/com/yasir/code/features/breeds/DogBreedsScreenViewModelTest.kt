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
        assertThat(sut.usersState.value, `is`(DogBreedsScreenUiState.Loading))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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

        assertThat(sut.usersState.value, `is`(fixtScreenUiState))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loads breeds and populates ui state_`() = runTest {
        // Create fixture data for DogBreedWithImage
        val fixtBreeds = listOf(
            fixture.create(DogBreedWithImage::class.java),
            fixture.create(DogBreedWithImage::class.java)
        )
        val fixtBreedsResult = Result.Success(fixtBreeds)

        // Mock the GetDogBreedsUseCase to return the fixture result wrapped in a flow
        every { mockGetDogBreedsUseCase.test(sut.viewModelScope) } returns flowOf(fixtBreedsResult)

        mockGetDogBreedsUseCase.test(sut.viewModelScope).collect { input ->
            println(input)
        }

        // Create fixture UI state data
        val fixtUiState = listOf(
            fixture.create(DogBreedUiState::class.java),
            fixture.create(DogBreedUiState::class.java)
        )
        val fixtScreenUiState = DogBreedsScreenUiState.DogBreedsUiState(fixtUiState)

        // Mock the mapper to return the mapped UI state
        every { mockDogBreedsScreenUiStateMapper.map(fixtBreedsResult) } returns fixtScreenUiState

        // Perform the load operation in the ViewModel
        sut.performLoad()

        // Wait until the coroutines are idle and complete all pending operations
        advanceUntilIdle()

        // Assert that the ViewModel's usersState has been updated with the expected UI state
        assertThat(sut.usersState.value, `is`(fixtScreenUiState))
    }

}