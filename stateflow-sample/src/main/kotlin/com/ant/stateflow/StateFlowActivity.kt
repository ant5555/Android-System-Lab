package com.ant.stateflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ant.stateflow.ui.theme.AndroidSystemLabTheme

class StateFlowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSystemLabTheme {
                val viewModel = viewModel<StateFlowViewModel>()
                val composeColor = viewModel.composeColor
                val flowColor by viewModel.color.collectAsState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(flowColor))
                        .clickable {
                            viewModel.generateNewColor()
                        }
                )
            }
        }
    }
}