package com.yasir.code

import androidx.compose.runtime.Composable
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
import com.yasir.code.features.breeds.DogBreedsScreen
import kotlinx.serialization.Serializable


@Serializable
data object DogBreedsScreen

@Serializable
data class DogBreedDetailsScreen(
    val name: String,
    val subType: String
)

@Composable
fun NavHostContainer() {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navHostController, DogBreedsScreen) {
        composable<DogBreedsScreen> {
            DogBreedsScreen { breed ->
                navHostController.navigate(
                    DogBreedDetailsScreen(
                        name = breed.name,
                        subType = breed.subType
                    )
                )
            }
        }
        composable<DogBreedDetailsScreen> { entry ->
            val screen: DogBreedDetailsScreen = entry.toRoute<DogBreedDetailsScreen>()
            val viewModel: DogBreedImagesScreenViewModel =
                hiltViewModel<DogBreedImagesScreenViewModel, DogBreedImagesScreenViewModelFactory>(
                    key = screen.name
                ) { factory: DogBreedImagesScreenViewModelFactory ->
                    factory.create(DogBreed(screen.name, screen.subType))
                }
            DogBreedDetailScreen(viewModel, onBackPressed = { navHostController.popBackStack() })
        }
    }
}