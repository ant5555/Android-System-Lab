package com.ant.flow.basic

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking<Unit> {

    // SharedFlow
    println("=== SharedFlow ===")
    val sharedFlow = MutableSharedFlow<Int>()

    val job1 = launch {
        sharedFlow.collect { println("collector1 받음: $it") }
    }

    delay(100L)
    sharedFlow.emit(1) // collector1 받음
    sharedFlow.emit(2) // collector1 받음

    val job2 = launch {
        sharedFlow.collect { println("collector2 받음: $it") }
    }

    delay(100L)
    sharedFlow.emit(3) // collector1, collector2 둘 다 받음

    job1.cancel()
    job2.cancel()

    // StateFlow
    println("\n=== StateFlow ===")
    val stateFlow = MutableStateFlow(0) // 초기값 있음

    val job3 = launch {
        stateFlow.collect { println("collector1 받음: $it") }
    }

    delay(100L)
    stateFlow.value = 1
    stateFlow.value = 2

    val job4 = launch {
        stateFlow.collect { println("collector2 받음: $it") } // 현재값 2 바로 받음
    }

    delay(100L)
    stateFlow.value = 3

    job3.cancel()
    job4.cancel()

    // StateFlow (같은 값 중복 emit 무시)
    println("\n=== StateFlow 중복값 무시 ===")
    val stateFlow2 = MutableStateFlow(0)
    val job5 = launch {
        stateFlow2.collect { println("받음: $it") }
    }
    delay(100L)
    stateFlow2.value = 1
    stateFlow2.value = 1 // 무시됨
    stateFlow2.value = 2
    delay(100L)
    job5.cancel()
}

/*
  - 늦게 구독 — SharedFlow는 이전 값 못 받고, StateFlow는 현재 값 바로 받음
  - 초기값 — StateFlow는 반드시 초기값이 있어요, SharedFlow는 없음
  - 중복값 — StateFlow는 같은 값 연속으로 넣으면 무시, SharedFlow는 다 흘려보냄
 */