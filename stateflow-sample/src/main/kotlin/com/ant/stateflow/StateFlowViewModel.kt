package com.ant.stateflow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class StateFlowViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val color = savedStateHandle.getStateFlow("color", 0xFFFFFFFF)

    var composeColor by mutableStateOf(savedStateHandle.get<Long>("color") ?: 0xFFFFFFFF)
        private set

    fun generateNewColor() {
        val color = Random.nextLong(0xFFFFFFFF)
        savedStateHandle["color"] = color
        composeColor = color
    }
}