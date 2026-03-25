package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    // New → Active → Completed
    val job = launch(start = CoroutineStart.LAZY) {
        println("실행 중 - isActive: ${coroutineContext[Job]?.isActive}")
        delay(300)
    }
    println("생성 직후  - isNew: ${!job.isActive && !job.isCompleted && !job.isCancelled}")
    job.start()
    println("start 후   - isActive: ${job.isActive}")
    job.join()
    println("join 후    - isCompleted: ${job.isCompleted}")

    println()

    // Active → Cancelling → Cancelled
    val job2 = launch(Dispatchers.Default) {
        while (isActive) {
            println("job2 실행 중")
        }
    }
    delay(100)
    println("cancel 전  - isActive: ${job2.isActive}")
    job2.cancel()
    job2.join()
    println("cancel 후  - isCancelled: ${job2.isCancelled}, isCompleted: ${job2.isCompleted}")
}
