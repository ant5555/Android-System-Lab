package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    // 1. 부모-자식 구조 확인
    println("=== 부모-자식 구조 ===")
    val parent = launch(CoroutineName("Parent")) {
        val child1 = launch(CoroutineName("Child1")) {
            delay(1000L)
            println("[${coroutineContext[CoroutineName]}] 완료")
        }
        val child2 = launch(CoroutineName("Child2")) {
            delay(500L)
            println("[${coroutineContext[CoroutineName]}] 완료")
        }
        println("[${coroutineContext[CoroutineName]}] 스레드: ${Thread.currentThread().name}")
        println("[${coroutineContext[CoroutineName]}] child1 job: ${child1[Job]}")
        println("[${coroutineContext[CoroutineName]}] child2 job: ${child2[Job]}")
    }
    parent.join()

    // 2. 부모 취소 → 자식도 취소
    println("\n=== 부모 취소 시 자식 취소 ===")
    val parentJob = launch(CoroutineName("Parent")) {
        launch(CoroutineName("Child1")) {
            delay(1000L)
            println("[Child1] 완료") // 찍히면 안됨
        }
        launch(CoroutineName("Child2")) {
            delay(1000L)
            println("[Child2] 완료") // 찍히면 안됨
        }
        println("[Parent] 자식 2개 생성")
    }
    delay(100L)
    parentJob.cancel()
    parentJob.join()
    println("[Parent] 취소됨 - isCancelled: ${parentJob.isCancelled}")

    // 3. 자식 취소 → 부모는 유지
    println("\n=== 자식 취소 시 부모 유지 ===")
    val parentJob2 = launch(CoroutineName("Parent")) {
        val child1 = launch(CoroutineName("Child1")) {
            delay(1000L)
            println("[Child1] 완료") // 찍히면 안됨
        }
        val child2 = launch(CoroutineName("Child2")) {
            delay(500L)
            println("[Child2] 완료")
        }
        child1.cancel()
        println("[Parent] Child1만 취소 - isCancelled: ${child1.isCancelled}")
        child2.join()
        println("[Parent] 완료")
    }
    parentJob2.join()

    // 4. coroutineScope vs 새 Job — 스코프 차이
    println("\n=== coroutineScope 스코프 ===")
    launch(CoroutineName("Outer")) {
        coroutineScope {
            launch(CoroutineName("Inner1")) {
                delay(300L)
                println("[Inner1] 완료 - 스레드: ${Thread.currentThread().name}")
            }
            launch(CoroutineName("Inner2")) {
                delay(100L)
                println("[Inner2] 완료 - 스레드: ${Thread.currentThread().name}")
            }
            println("[Outer] coroutineScope 안 — Inner 다 끝날때까지 대기")
        }
        println("[Outer] coroutineScope 끝난 후 여기 실행")
    }.join()
}
