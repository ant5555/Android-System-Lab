package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()
    val participantDeferred1: Deferred<Array<String>> = async(Dispatchers.IO) {
        delay(1000L)
        arrayOf("Yun", "Kim")
    }

    val participantDeferred2: Deferred<Array<String>> = async(Dispatchers.IO) {
        delay(1000L)
        arrayOf("Park")
    }

    val results: List<Array<String>> = listOf(participantDeferred1,participantDeferred2).awaitAll()
    println("[${getElapsedTime(startTime)}] 참여자 목록: ${listOf(*results[0], *results[1])}")

}

fun getElapsedTime(startTime: Long): String = "지난 시간: ${System.currentTimeMillis() - startTime}ms"
