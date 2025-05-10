package com.yasir.code.features.breeds

import com.flextrade.jfixture.JFixture
import com.yasir.code.common.StringLoader
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeds.model.DogBreedsScreenUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

import org.junit.Before
import org.junit.Test

class DogBreedsScreenUiStateMapperTest {

    private val mockStringLoader: StringLoader = mockk()
    private lateinit var sut: DogBreedsScreenUiStateMapper
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = DogBreedsScreenUiStateMapper(mockStringLoader)
    }

    @Test
    fun `map success `() = runTest {
        val spy: DogBreedsScreenUiStateMapper = spyk(sut)
        val fixtSuccessUiState = fixture.create(DogBreedsScreenUiState.DogBreedsUiState::class.java)

        val fixtBreedWithImage = fixture.create(DogBreedWithImage::class.java)
        val fixtBreedWithImageResult = Result.Success(listOf(fixtBreedWithImage))
        every { spy.mapSuccess(fixtBreedWithImageResult) } returns fixtSuccessUiState

        val uiState = spy.map(fixtBreedWithImageResult)

        verify { spy.mapSuccess(fixtBreedWithImageResult) }
        assert(uiState is DogBreedsScreenUiState.DogBreedsUiState)
        assertThat((uiState as DogBreedsScreenUiState.DogBreedsUiState).breeds, `is`(fixtSuccessUiState.breeds))
    }


}