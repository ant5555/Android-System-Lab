package com.ant.flow.basic

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking<Unit> {

    // Cold Flow
    println("=== Cold Flow ===")
    val coldFlow = flow {
        println("emit 시작")
        emit(1)
        emit(2)
        emit(3)
    }

    println("-- 첫 번째 collect --")
    coldFlow.collect { println("받음: $it") }

    println("-- 두 번째 collect --")
    coldFlow.collect { println("받음: $it") }

    // Hot Flow
    println("\n=== Hot Flow (SharedFlow) ===")
    val sharedFlow = MutableSharedFlow<Int>()

    val job = launch {
        sharedFlow.collect { println("collector1 받음: $it") }
    }

    delay(100L)
    sharedFlow.emit(1) // collector1 받음
    sharedFlow.emit(2) // collector1 받음

    // 두 번째 collector는 늦게 구독 —> 이미 흘러간 1, 2는 못 받음
    val job2 = launch {
        sharedFlow.collect { println("collector2 받음: $it") }
    }

    delay(100L)
    sharedFlow.emit(3) // collector1, collector2 둘 다 받음

    job.cancel()
    job2.cancel()
}
