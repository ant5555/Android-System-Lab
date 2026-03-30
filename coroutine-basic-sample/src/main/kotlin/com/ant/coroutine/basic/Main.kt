package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    // ==========  CancellationException  ==========

    // 1. CancellationException은 예외가 아니라 취소 신호 — 부모로 전파 안됨
    println("=== [1] CancellationException 전파 안됨 ===")
    val handler = CoroutineExceptionHandler { _, e ->
        println("Handler 캐치: ${e.message}") // 찍히면 안됨
    }
    launch(handler) {
        throw CancellationException("취소 신호")
    }.join()
    println("CancellationException은 핸들러 안 탐")

    // 2. catch(Exception)으로 CancellationException 삼키면 취소가 무시
    println("\n=== [2] CancellationException 삼키기 — 위험 ===")
    val job = launch {
        try {
            delay(1000L)
        } catch (e: Exception) { // CancellationException도 잡힘
            println("잡힘: ${e::class.simpleName}")
            // 취소 신호를 삼켜버림 — 코루틴이 계속 살아있으려 함
            delay(500L) // 여기서 다시 CancellationException 발생
            println("여기 찍히면 안됨")
        }
    }
    delay(100L)
    job.cancel()
    job.join()
    println("job isCancelled: ${job.isCancelled}")

    // 3. CancellationException은 반드시 다시 던져야 함
    println("\n=== [3] CancellationException 다시 던지기 — 올바른 패턴 ===")
    val job2 = launch {
        try {
            delay(1000L)
        } catch (e: CancellationException) {
            println("취소 감지 — 정리 작업 후 rethrow")
            throw e // 반드시 다시 던짐
        } catch (e: Exception) {
            println("일반 예외만 처리: ${e.message}")
        }
    }
    delay(100L)
    job2.cancel()
    job2.join()
    println("job2 isCancelled: ${job2.isCancelled}")

    // ==========  try-finally + NonCancellable  ==========

    // 4. finally — 취소돼도 실행 보장
    println("\n=== [4] finally 취소 시 실행 ===")
    val job3 = launch {
        try {
            println("작업 시작")
            delay(1000L)
            println("작업 완료") // 찍히면 안됨
        } finally {
            println("finally 실행 — 리소스 정리") // 취소돼도 찍혀야 함
        }
    }
    delay(100L)
    job3.cancel()
    job3.join()

    // 5. finally 안에서 suspend 함수 호출 — 취소 상태라 바로 CancellationException
    println("\n=== [5] finally 안 suspend 함수 — 실행 안됨 ===")
    val job4 = launch {
        try {
            delay(1000L)
        } finally {
            println("finally 진입")
            delay(100L) // 취소 상태라 즉시 CancellationException 발생
            println("여기 찍히면 안됨")
        }
    }
    delay(100L)
    job4.cancel()
    job4.join()

    // 6. NonCancellable — finally 안에서 suspend 함수 보장
    println("\n=== [6] NonCancellable — finally suspend 보장 ===")
    val job5 = launch {
        try {
            delay(1000L)
        } finally {
            withContext(NonCancellable) { // 취소 무시
                println("NonCancellable 진입")
                delay(300L)              // 취소 상태여도 실행됨
                println("DB 닫기 / 로그 전송 등 중요 작업 완료")
            }
        }
    }
    delay(100L)
    job5.cancel()
    job5.join()
}
