package com.yasir.code

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.features.breeddetail.DogBreedDetailScreen
import com.yasir.code.features.breeddetail.DogBreedImagesScreenViewModel
import com.yasir.code.features.breeddetail.DogBreedImagesScreenViewModelFactory
import com.yasir.code.features.users.ListScreen
import kotlinx.serialization.Serializable


@Serializable
data object DogBreedsScreen

@Serializable
data class DogBreedDetailsScreen(val breed: DogBreed)

@Composable
fun NavHostContainer(modifier: Modifier = Modifier) {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navHostController, DogBreedsScreen) {
        composable<DogBreedsScreen> {
            ListScreen(modifier = Modifier.padding()) { breed ->
                navHostController.navigate(DogBreedDetailsScreen(breed))
            }
        }
        composable<DogBreedDetailsScreen> { entry ->
            val screen: DogBreedDetailsScreen = entry.toRoute<DogBreedDetailsScreen>()
            val viewModel: DogBreedImagesScreenViewModel =
                hiltViewModel<DogBreedImagesScreenViewModel, DogBreedImagesScreenViewModelFactory>(
                    key = screen.breed.toString()
                ) { factory: DogBreedImagesScreenViewModelFactory ->
                    factory.create(screen.breed)
                }
            DogBreedDetailScreen(viewModel)
        }
    }
}