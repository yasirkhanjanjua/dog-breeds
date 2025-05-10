package com.yasir.code.features.breeddetail

import com.flextrade.jfixture.JFixture
import com.yasir.code.common.R
import com.yasir.code.common.StringLoader
import com.yasir.code.common.capitalize
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.DogBreedImages
import com.yasir.code.core.domain.model.Result
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState
import com.yasir.code.test.util.str
import io.mockk.every
import io.mockk.mockk
import okio.IOException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`

import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class DogBreedDetailScreenUiStateMapperTest {
    private val mockStringLoader: StringLoader = mockk()
    private lateinit var sut: DogBreedDetailScreenUiStateMapper
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = DogBreedDetailScreenUiStateMapper(mockStringLoader)
    }

    @Test
    fun `map success`() {
        val fixtBreedName = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java).copy(name = fixtBreedName, subType = "")
        val fixtImage = fixture.str()
        val fixtDogBreedImages = fixture.create(DogBreedImages::class.java).copy(images = listOf(fixtImage))

        val uiState: DogBreedDetailScreenUiState = sut.map(fixtBreed, Result.Success(fixtDogBreedImages))

        assertThat(uiState, `is`(instanceOf(DogBreedDetailScreenUiState.DogBreedDetailUiState::class.java)))
        assertThat(uiState.title, `is`(fixtBreedName.capitalize()))
        assertThat((uiState as DogBreedDetailScreenUiState.DogBreedDetailUiState).images, `is`(listOf(fixtImage)))
    }

    @Test
    fun `map success limits breed images to 10`() {
        val fixtBreedName = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java).copy(name = fixtBreedName, subType = "")
        val images = generateSequence {
            fixture.str()
        }.take(20).toList()
        val fixtDogBreedImages = fixture.create(DogBreedImages::class.java).copy(images = images)

        val uiState: DogBreedDetailScreenUiState = sut.map(fixtBreed, Result.Success(fixtDogBreedImages))

        assertThat(uiState, `is`(instanceOf(DogBreedDetailScreenUiState.DogBreedDetailUiState::class.java)))
        assertThat(uiState.title, `is`(fixtBreedName.capitalize()))
        assertThat((uiState as DogBreedDetailScreenUiState.DogBreedDetailUiState).images.size, `is`(10))
    }

    @Test
    fun `map failure`() {
        val fixtBreedName = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java).copy(name = fixtBreedName, subType = "")

        val uiState: DogBreedDetailScreenUiState = sut.map(fixtBreed, Result.Failure())

        assertThat(uiState, `is`(instanceOf(DogBreedDetailScreenUiState.Error::class.java)))
    }

    @Test
    fun `mapError UnknownHostException`() {
        val fixtBreedName = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java).copy(name = fixtBreedName, subType = "")
        val fixtErrorMessage = fixture.str()
        every { mockStringLoader.getString(R.string.no_internet) } returns fixtErrorMessage
        val uiState = sut.mapError(fixtBreed, UnknownHostException())

        assertThat(uiState, `is`(instanceOf(DogBreedDetailScreenUiState.Error::class.java)))
        assertThat(uiState.message, `is`(fixtErrorMessage))
    }

    @Test
    fun `mapError IOException`() {
        val fixtBreedName = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java).copy(name = fixtBreedName, subType = "")
        val fixtErrorMessage = fixture.str()
        every { mockStringLoader.getString(R.string.network_error) } returns fixtErrorMessage
        val uiState = sut.mapError(fixtBreed, IOException())

        assertThat(uiState, `is`(instanceOf(DogBreedDetailScreenUiState.Error::class.java)))
        assertThat(uiState.message, `is`(fixtErrorMessage))
    }

    @Test
    fun `mapError unknown`() {
        val fixtBreedName = fixture.str()
        val fixtBreed = fixture.create(DogBreed::class.java).copy(name = fixtBreedName, subType = "")
        val fixtErrorMessage = fixture.str()
        every { mockStringLoader.getString(R.string.unknown_error) } returns fixtErrorMessage
        val uiState = sut.mapError(fixtBreed, IllegalStateException())

        assertThat(uiState, `is`(instanceOf(DogBreedDetailScreenUiState.Error::class.java)))
        assertThat(uiState.message, `is`(fixtErrorMessage))
    }
}