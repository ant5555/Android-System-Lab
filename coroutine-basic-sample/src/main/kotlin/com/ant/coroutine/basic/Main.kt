package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val whileJob: Job = launch(Dispatchers.Default) {
        while(this.isActive) { //while(true) {
            println("작업중 ")
            //delay(1L) //1번 일시중단 성능저하
            //yield()   //2번 스레드 사용 양보도 비효율적
        }
    }
    delay(100L)
    whileJob.cancel()
}
