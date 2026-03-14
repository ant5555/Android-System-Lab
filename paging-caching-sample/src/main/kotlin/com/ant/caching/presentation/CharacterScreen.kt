package com.ant.caching.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.ant.caching.domain.Character

@Composable
fun CharacterScreen(
    characters: LazyPagingItems<Character>,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = characters.loadState) {
        val errorState = characters.loadState.refresh as? LoadState.Error
            ?: characters.loadState.append as? LoadState.Error
            ?: characters.loadState.prepend as? LoadState.Error

        errorState?.let {
            snackbarHostState.showSnackbar(
                message = it.error.localizedMessage ?: "알 수 없는 에러"
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (characters.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    count = characters.itemCount,
                ) { index ->
                    val character = characters[index]
                    if (character != null) {
                        CharacterItem(character = character)
                    }
                }

                if (characters.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

    }
}