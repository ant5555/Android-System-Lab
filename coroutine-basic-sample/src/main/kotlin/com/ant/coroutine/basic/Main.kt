package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    // 1. 여러 구성요소 합치기 (+)
    val context = Dispatchers.IO + CoroutineName("MyCoroutine") + Job()
    launch(context) {
        println("합친 컨텍스트 - 스레드: ${Thread.currentThread().name}")
        println("합친 컨텍스트 - 이름: ${coroutineContext[CoroutineName]}")
    }.join()

    // 2. 키로 구성요소 꺼내기
    launch(Dispatchers.Default + CoroutineName("KeyTest")) {
        val name = coroutineContext[CoroutineName]   // 키로 꺼냄
        val job  = coroutineContext[Job]             // 키로 꺼냄
        println("이름: $name, job 활성: ${job?.isActive}")
    }.join()

    // 3. 구성요소 제거하기 (minusKey)
    val fullContext = Dispatchers.IO + CoroutineName("RemoveTest")
    val removedContext = fullContext.minusKey(CoroutineName)  // 이름 제거
    launch(removedContext) {
        println("이름 제거 후: ${coroutineContext[CoroutineName]}")  // null
        println("Dispatcher는 유지: ${Thread.currentThread().name}")
    }.join()

    // 4. 자식 코루틴은 부모 컨텍스트 상속
    launch(CoroutineName("Parent")) {
        println("부모 이름: ${coroutineContext[CoroutineName]}")
        launch {
            // Dispatcher, Job은 새로 생성되지만 CoroutineName은 상속
            println("자식 이름: ${coroutineContext[CoroutineName]}")
        }.join()
    }.join()
}
