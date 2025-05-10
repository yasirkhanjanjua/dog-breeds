package com.yasir.code.data

import com.flextrade.jfixture.JFixture
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.network.DogBreedsNetworkDataSource
import com.yasir.code.core.network.model.NetworkDogBreedsResponse
import com.yasir.code.test.MainDispatcherRule
import com.yasir.code.test.util.str
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OnlineDogBreedsRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val mockDogBreedsNetworkDataSource: DogBreedsNetworkDataSource = mockk()
    private lateinit var sut: OnlineDogBreedsRepository
    private val fixture = JFixture()

    @Before
    fun setUp() {
        sut = OnlineDogBreedsRepository(mockDogBreedsNetworkDataSource, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `fetchBreeds fetches network dog breeds and on success returns dog breeds`() = runTest {
        val fixtNetworkDogBreeds = mapOf(
            "bulldog" to listOf("american", "british"),
            "german shepherd" to listOf()
        )
        val fixtNetworkBreedsResponse = NetworkDogBreedsResponse(
            fixtNetworkDogBreeds, "OK"
        )

        coEvery { mockDogBreedsNetworkDataSource.fetchBreeds() } returns Result.Success(fixtNetworkBreedsResponse)

        val breedsResult: Result<List<DogBreed>> = sut.fetchBreeds()

        assertThat(breedsResult,`is`(instanceOf(Result.Success::class.java)))
        val breeds = (breedsResult as Result.Success).data
        assertThat(breeds.size, `is`(3))
        assertThat(breeds[0].name, `is`("bulldog"))
        assertThat(breeds[0].subType, `is`("american"))
        assertThat(breeds[1].name, `is`("bulldog"))
        assertThat(breeds[1].subType, `is`("british"))
        assertThat(breeds[2].name, `is`("german shepherd"))
        assertThat(breeds[2].subType, `is`(""))
    }

    @Test
    fun `fetchBreeds fetches network dog breeds and on failure returns failure`() = runTest {
        val fixtErrorMessage = fixture.str()
        coEvery { mockDogBreedsNetworkDataSource.fetchBreeds() } returns Result.Failure(message = fixtErrorMessage)

        val breedsResult: Result<List<DogBreed>> = sut.fetchBreeds()

        assertThat(breedsResult,`is`(instanceOf(Result.Failure::class.java)))
        val failure = breedsResult as Result.Failure
        assertThat(failure.message, `is`(fixtErrorMessage))
    }
}