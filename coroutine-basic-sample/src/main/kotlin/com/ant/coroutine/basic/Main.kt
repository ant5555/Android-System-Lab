package com.ant.coroutine.basic

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val convertImage1: Job = launch(Dispatchers.Default) {
        Thread.sleep(1000)
        println("[${Thread.currentThread().name}] 이미지1 변환 완료")
    }

    val convertImage2: Job = launch(Dispatchers.Default) {
        Thread.sleep(1000)
        println("[${Thread.currentThread().name}] 이미지2 변환 완료")
    }


    joinAll(convertImage1,convertImage2)

    val uploadImage: Job = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 이미지1,2 업로드")
    }
}
