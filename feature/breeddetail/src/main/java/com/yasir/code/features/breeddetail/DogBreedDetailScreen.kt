package com.yasir.code.features.breeddetail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState

@Composable
fun DogBreedDetailScreen(viewModel: DogBreedImagesScreenViewModel, modifier: Modifier = Modifier) {
    val uiState: State<DogBreedDetailScreenUiState> =
        viewModel.usersState.collectAsStateWithLifecycle()

}

@Composable
fun DogBreedDetails(
    uiState: DogBreedDetailScreenUiState.DogBreedDetailUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(uiState.images) {
            DogBreedImage(it)
        }
    }
}

@Composable
fun DogBreedImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        modifier = Modifier
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}