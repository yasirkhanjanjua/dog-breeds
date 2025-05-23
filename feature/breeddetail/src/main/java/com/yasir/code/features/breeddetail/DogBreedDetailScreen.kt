package com.yasir.code.features.breeddetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yasir.code.breeddetail.R
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState

@Composable
fun DogBreedDetailScreen(
    viewModel: DogBreedImagesScreenViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState: State<DogBreedDetailScreenUiState> =
        viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BreedDetailTopAppBar(uiState.value.title, onBackPressed)
        }
    ) { innerPadding ->
        DogBreedDetails(innerPadding, uiState.value, viewModel::onRetry)
    }
}

@Composable
fun DogBreedDetails(
    paddingValues: PaddingValues,
    uiState: DogBreedDetailScreenUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when (uiState) {
            is DogBreedDetailScreenUiState.DogBreedDetailUiState -> DogBreedDetails(uiState)
            is DogBreedDetailScreenUiState.Loading -> ShowLoading()
            is DogBreedDetailScreenUiState.Error -> ShowError(onRetry)
        }
    }
}

@Composable
fun DogBreedDetails(
    uiState: DogBreedDetailScreenUiState.DogBreedDetailUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
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

@Composable
fun DogBreedImagePreview() {
    DogBreedImage(url = "https://images.dog.ceo/breeds/retriever-flatcoated/n02099267_5089.jpg")
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BreedDetailTopAppBar(name: String, onBackPressed: () -> Unit) {
    val isBackHandled = remember { mutableStateOf(false) }
    BackHandler(enabled = !isBackHandled.value) {
        onBackPressed()
        isBackHandled.value = true
    }
    TopAppBar(
        title = { Text(text = name, style = MaterialTheme.typography.titleMedium) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = Color.White,
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!isBackHandled.value) {
                        onBackPressed()
                        isBackHandled.value = true
                    }
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }
        }
    )
}

@Preview
@Composable
fun BreedDetailTopAppBarPreview() {
    BreedDetailTopAppBar(name = "Breed detail") {
        
    }
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
            text = stringResource(R.string.breed_details_load_error),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Button(onRetry) {
            Text(
                text = stringResource(R.string.breed_details_retry),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun ShowErrorPreview(modifier: Modifier = Modifier) {
    ShowError({})
}