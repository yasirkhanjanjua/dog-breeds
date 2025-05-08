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
import com.yasir.code.domain.model.User
import com.yasir.code.features.users.model.UserUiState
import com.yasir.code.features.users.model.UsersScreenUiState

@Composable
fun ListScreen(
    modifier: Modifier = Modifier.Companion,
    viewModel: ListScreenViewModel = hiltViewModel<ListScreenViewModel>()
) {
    val state: State<UsersScreenUiState> = viewModel.usersState.collectAsStateWithLifecycle()
    ListScreen(state.value)
}

@Composable
fun ListScreen(uiState: UsersScreenUiState, modifier: Modifier = Modifier.Companion) {
    when(uiState) {
        is UsersScreenUiState.UsersUiState -> ShowUsers(uiState.users)
        is UsersScreenUiState.Loading -> ShowLoading()
        is UsersScreenUiState.Error -> ShowError()
    }
}

@Composable
fun ShowUsers(users: List<UserUiState>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(users) {
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