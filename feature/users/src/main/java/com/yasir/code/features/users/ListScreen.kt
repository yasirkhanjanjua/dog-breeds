package com.yasir.code.features.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yasir.code.features.users.model.DogBreedsScreenUiState
import com.yasir.code.features.users.model.UserUiState

@Composable
fun ListScreen(
    modifier: Modifier = Modifier.Companion,
    viewModel: DogBreedsScreenViewModel = hiltViewModel<DogBreedsScreenViewModel>()
) {
    val state: State<DogBreedsScreenUiState> = viewModel.usersState.collectAsStateWithLifecycle()
    ListScreen(state.value)
}

@Composable
fun ListScreen(uiState: DogBreedsScreenUiState, modifier: Modifier = Modifier.Companion) {
    when(uiState) {
        is DogBreedsScreenUiState.DogBreedsUiState -> ShowUsers(uiState)
        is DogBreedsScreenUiState.Loading -> ShowLoading()
        is DogBreedsScreenUiState.Error -> ShowError()
    }
}

@Composable
fun ShowUsers(breedsUiState: DogBreedsScreenUiState.DogBreedsUiState, modifier: Modifier = Modifier) {
    LazyColumn {
        items(breedsUiState.breeds) {
            Text(text = it.name)
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