package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    // Default — CPU 연산에 최적화, 코어 수만큼 스레드 풀
    launch(Dispatchers.Default) {
        println("[${Thread.currentThread().name}] Dispatchers.Default")
    }

    // IO — 파일/네트워크 I/O에 최적화, 스레드 많이 만들 수 있음
    launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] Dispatchers.IO")
    }

    // Unconfined — 처음엔 호출한 스레드, delay 이후엔 재개되는 스레드
    launch(Dispatchers.Unconfined) {
        println("[${Thread.currentThread().name}] Dispatchers.Unconfined - delay 전")
        delay(100)
        println("[${Thread.currentThread().name}] Dispatchers.Unconfined - delay 후")
    }

    // Dispatcher 없음 — 부모 스코프(runBlocking) 스레드 그대로 사용
    launch {
        println("[${Thread.currentThread().name}] Dispatcher 없음")
    }
}
