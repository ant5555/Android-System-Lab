package com.ant.flow.basic

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val flow = flow {
        emit(1)
        emit(2)
        emit(3)
    }
    flow.collect { println("collect: $it") }

    listOf("a", "b", "c").asFlow()
        .collect { println("asFlow: $it") }

    (1..5).asFlow()
        .collect { println("rangeFlow: $it") }

    flowOf(10, 20, 30)
        .collect { println("flowOf: $it") }
}
