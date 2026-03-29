package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    // ==========

    // 1. 자식 예외 → 부모까지 취소
    println("=== [1] 자식 예외 → 부모 취소 ===")
    val parent = launch {
        launch {
            delay(100L)
            throw RuntimeException("Child 예외 발생")
        }
        launch {
            delay(500L)
            println("다른 자식 완료") // 찍히면 안됨
        }
    }
    parent.join()
    println("parent isCancelled: ${parent.isCancelled}")

    // 2. CoroutineExceptionHandler — 예외 잡기
    println("\n=== [2] CoroutineExceptionHandler ===")
    val handler = CoroutineExceptionHandler { _, e ->
        println("Handler 캐치: ${e.message}")
    }
    val job = launch(handler) {
        launch {
            delay(100L)
            throw IllegalStateException("예외!")
        }
        delay(500L)
        println("완료") // 찍히면 안됨
    }
    job.join()

    // 3. SupervisorJob — 자식 예외가 다른 자식에 영향 없음
    println("\n=== [3] SupervisorJob ===")
    val supervisor = CoroutineScope(SupervisorJob() + handler)
    val child1 = supervisor.launch {
        delay(100L)
        throw RuntimeException("Child1 예외")
    }
    val child2 = supervisor.launch {
        delay(500L)
        println("Child2 완료") // SupervisorJob이라 찍혀야 함
    }
    joinAll(child1, child2)

    // ==========

    // 4. async 예외 — await() 호출 시점에 터짐
    println("\n=== [4] async 예외 전파 ===")
    val handler2 = CoroutineExceptionHandler { _, e ->
        println("Handler 캐치: ${e.message}")
    }
    launch(handler2) {
        val deferred = async {
            delay(100L)
            throw RuntimeException("async 예외")
            "결과"
        }
        try {
            deferred.await() // 여기서 예외 터짐
        } catch (e: Exception) {
            println("await catch: ${e.message}")
        }
    }.join()

    // 5. supervisorScope — 자식 예외 격리 + 개별 처리
    println("\n=== [5] supervisorScope ===")
    launch {
        supervisorScope {
            val a = async {
                delay(100L)
                throw RuntimeException("a 예외")
                "a 결과"
            }
            val b = async {
                delay(200L)
                "b 결과"
            }
            // a 예외를 개별로 처리, b는 정상
            println("a: ${runCatching { a.await() }.exceptionOrNull()?.message}")
            println("b: ${b.await()}")
        }
    }.join()
}
