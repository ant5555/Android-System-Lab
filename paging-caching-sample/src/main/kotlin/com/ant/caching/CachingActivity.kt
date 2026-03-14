package com.ant.caching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ant.caching.presentation.CharacterScreen
import com.ant.caching.presentation.CharacterViewModel
import com.ant.caching.ui.theme.AndroidSystemLabTheme

class CachingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSystemLabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = hiltViewModel<CharacterViewModel>()
                    val characters = viewModel.characterPagingFlow.collectAsLazyPagingItems()
                    CharacterScreen(
                        characters = characters,
                    )
                }
            }
        }
    }
}
