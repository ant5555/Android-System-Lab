package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val updateToken: Job = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 토큰 업데이트 시작")
        delay(100L)
        println("[${Thread.currentThread().name}] 토큰 업데이트 완료")
    }

    val independentJob: Job = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 독립된 작업 시작")
    }

    updateToken.join()

    val networkCallJob: Job = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 네트워크 요청")
    }
}
