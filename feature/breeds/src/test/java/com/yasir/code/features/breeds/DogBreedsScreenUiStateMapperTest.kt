package com.yasir.code.features.breeds

import com.flextrade.jfixture.JFixture
import com.yasir.code.common.R
import com.yasir.code.common.StringLoader
import com.yasir.code.core.domain.model.DogBreedWithImage
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeds.model.DogBreedsScreenUiState
import com.yasir.code.test.util.str
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.UnknownHostException

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


    @Test
    fun `map failure`() = runTest {
        val spy: DogBreedsScreenUiStateMapper = spyk(sut)
        val fixtSuccessUiState: DogBreedsScreenUiState.Error = fixture.create(DogBreedsScreenUiState.Error::class.java)

        val fixtBreedWithImageResult:Result.Failure<List<DogBreedWithImage>> = Result.Failure()
        every { spy.mapFailure(fixtBreedWithImageResult) } returns fixtSuccessUiState

        val uiState = spy.map(fixtBreedWithImageResult)

        verify { spy.mapFailure(fixtBreedWithImageResult) }
        assert(uiState is DogBreedsScreenUiState.Error)
        assertThat(uiState as DogBreedsScreenUiState.Error, `is`(fixtSuccessUiState))
    }



    @Test
    fun `mapError returns No Internet error message for UnknownHostException`() = runTest {
        val fixtErrorMessage = fixture.str()
        every { mockStringLoader.getString(R.string.no_internet) } returns fixtErrorMessage

        val fixtError = UnknownHostException()

        val errorUiState: DogBreedsScreenUiState.Error = sut.mapError(fixtError)

        assertThat(errorUiState.message, `is`(fixtErrorMessage))
    }

    @Test
    fun `mapError returns network error message for IOException`() = runTest {
        val fixtErrorMessage = fixture.str()
        every { mockStringLoader.getString(R.string.network_error) } returns fixtErrorMessage

        val fixtError = IOException()

        val errorUiState: DogBreedsScreenUiState.Error = sut.mapError(fixtError)

        assertThat(errorUiState.message, `is`(fixtErrorMessage))
    }

    @Test
    fun `mapError returns unknown error message for any other exception`() = runTest {
        val fixtErrorMessage = fixture.str()
        every { mockStringLoader.getString(R.string.unknown_error) } returns fixtErrorMessage

        val fixtError = IllegalStateException()

        val errorUiState: DogBreedsScreenUiState.Error = sut.mapError(fixtError)

        assertThat(errorUiState.message, `is`(fixtErrorMessage))
    }

}