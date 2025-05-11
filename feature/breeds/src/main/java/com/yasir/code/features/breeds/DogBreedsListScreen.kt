package com.yasir.code.features.breeds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.features.breeds.model.DogBreedUiState
import com.yasir.code.features.breeds.model.DogBreedsScreenUiState
import com.yasir.code.users.R

@Composable
fun DogBreedsScreen(
    viewModel: DogBreedsScreenViewModel = hiltViewModel<DogBreedsScreenViewModel>(),
    onBreedSelected: (breed: DogBreed) -> Unit
) {
    val state: State<DogBreedsScreenUiState> = viewModel.breedsState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BreedsTopAppBar()
        }
    ) { innerPadding: PaddingValues ->
        DogBreedsScreen(innerPadding, state.value, onBreedSelected, viewModel::onRetry)
    }
}

@Composable
fun DogBreedsScreen(
    paddingValues: PaddingValues,
    uiState: DogBreedsScreenUiState,
    onBreedSelected: (breed: DogBreed) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when (uiState) {
            is DogBreedsScreenUiState.DogBreedsUiState -> ShowDogBreeds(uiState, onBreedSelected)
            is DogBreedsScreenUiState.Loading -> ShowLoading()
            is DogBreedsScreenUiState.Error -> ShowError(onRetry)
        }
    }
}

@Composable
fun ShowDogBreeds(
    breedsUiState: DogBreedsScreenUiState.DogBreedsUiState,
    onBreedSelected: (breed: DogBreed) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp)) {
        items(breedsUiState.breeds) {
            Surface(
                onClick = { onBreedSelected(it.breed) },
                modifier = modifier
                    .fillParentMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = it.image,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Text(
                        text = it.name, modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun ShowDogBreedsPreview(modifier: Modifier = Modifier) {
    val breeds = listOf(
        DogBreedUiState(
            DogBreed("affenpinscher", ""),
            name = "affenpinscher",
            "https://images.dog.ceo/breeds/pembroke/n02113023_6161.jpg"
        ),

        DogBreedUiState(
            DogBreed("sheepdog", "english"),
            name = "affenpinscher",
            "https://images.dog.ceo/breeds/affenpinscher/n02110627_3972.jpg"
        )
    )

    ShowDogBreeds(DogBreedsScreenUiState.DogBreedsUiState(breeds), {})
}

@Preview
@Composable
fun ShowLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ShowError(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.breeds_load_error),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Button(onRetry) {
            Text(
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun ShowErrorPreview() {
    ShowError({})
}


@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BreedsTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.breeds_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = Color.White,
        )
    )
}