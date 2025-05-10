package com.yasir.code.features.breeddetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yasir.code.breeddetail.R
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState

@Composable
fun DogBreedDetailScreen(viewModel: DogBreedImagesScreenViewModel, onBackPressed: () -> Unit, modifier: Modifier = Modifier) {
    val uiState: State<DogBreedDetailScreenUiState> =
        viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BreedDetailTopAppBar(uiState.value.title, onBackPressed)
        }
    ) { innerPadding ->
        DogBreedDetails(innerPadding, uiState.value)
    }
}

@Composable
fun DogBreedDetails(paddingValues: PaddingValues, uiState: DogBreedDetailScreenUiState, modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxSize().padding(paddingValues)) {
        when (uiState) {
            is DogBreedDetailScreenUiState.DogBreedDetailUiState -> DogBreedDetails(uiState)
            is DogBreedDetailScreenUiState.Loading -> {}
            is DogBreedDetailScreenUiState.Error -> {}
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