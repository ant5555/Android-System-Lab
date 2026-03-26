package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    // withContext — 순차 실행 (약 2000ms)
    val startTime1 = System.currentTimeMillis()
    println("[withContext] 시작 스레드: ${Thread.currentThread().name}")
    val participants1: Array<String> = withContext(Dispatchers.IO) {
        println("[withContext] participants1 스레드: ${Thread.currentThread().name}")
        delay(1000L)
        arrayOf("Yun", "Kim")
    }
    val participants2: Array<String> = withContext(Dispatchers.IO) {
        println("[withContext] participants2 스레드: ${Thread.currentThread().name}")
        delay(1000L)
        arrayOf("Park")
    }
    println("[withContext] ${getElapsedTime(startTime1)} 참여자: ${listOf(*participants1, *participants2)}")

    // async + awaitAll — 동시 실행 (약 1000ms)
    val startTime2 = System.currentTimeMillis()
    println("[awaitAll] 시작 스레드: ${Thread.currentThread().name}")
    val deferred1: Deferred<Array<String>> = async(Dispatchers.IO) {
        println("[awaitAll] deferred1 스레드: ${Thread.currentThread().name}")
        delay(1000L)
        arrayOf("Yun", "Kim")
    }
    val deferred2: Deferred<Array<String>> = async(Dispatchers.IO) {
        println("[awaitAll] deferred2 스레드: ${Thread.currentThread().name}")
        delay(1000L)
        arrayOf("Park")
    }
    val results = listOf(deferred1, deferred2).awaitAll()
    println("[awaitAll]   ${getElapsedTime(startTime2)} 참여자: ${listOf(*results[0], *results[1])}")
}

fun getElapsedTime(startTime: Long): String = "지난 시간: ${System.currentTimeMillis() - startTime}ms"
