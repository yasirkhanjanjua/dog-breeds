package com.yasir.code.features.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.features.users.model.DogBreedsScreenUiState
import com.yasir.code.users.R

@Composable
fun DogBreedsScreen(
    modifier: Modifier = Modifier.Companion,
    viewModel: DogBreedsScreenViewModel = hiltViewModel<DogBreedsScreenViewModel>(),
    onBreedSelected: (breed: DogBreed) -> Unit
) {
    val state: State<DogBreedsScreenUiState> = viewModel.usersState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BreedsTopAppBar()
        }
    ) { innerPadding: PaddingValues ->
        DogBreedsScreen(innerPadding, state.value, onBreedSelected)
    }
}

@Composable
fun DogBreedsScreen(
    paddingValues: PaddingValues,
    uiState: DogBreedsScreenUiState,
    onBreedSelected: (breed: DogBreed) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    when (uiState) {
        is DogBreedsScreenUiState.DogBreedsUiState -> ShowDogBreeds(uiState, onBreedSelected)
        is DogBreedsScreenUiState.Loading -> ShowLoading()
        is DogBreedsScreenUiState.Error -> ShowError()
    }
}

@Composable
fun ShowDogBreeds(
    breedsUiState: DogBreedsScreenUiState.DogBreedsUiState,
    onBreedSelected: (breed: DogBreed) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.padding(all = 16.dp), contentPadding = PaddingValues(8.dp)) {
        items(breedsUiState.breeds) {
            Surface(onClick = { onBreedSelected(it.breed) }) {
                Text(text = it.name)
            }
        }
    }
}

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
fun ShowError(modifier: Modifier = Modifier) {

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BreedsTopAppBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.breeds_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = Color.White,
        )
    )
}