package com.ant.thread

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ThreadViewModel : ViewModel() {

    companion object {
        private const val TAG = "ThreadDemo"
    }

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()

    private val timeFmt = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    private fun log(msg: String) {
        val t = Thread.currentThread()
        val entry = "${timeFmt.format(Date())} [${t.name}] $msg"
        Log.d(TAG, entry)
        _logs.update { it + entry }
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }

    // 기본 Thread 생성 & 상태 전이
    fun runBasicThread() {
        Thread {
            log("=== [1] 기본 Thread 생성 ===")
            log("호출 스레드: ${Thread.currentThread().name}")

            val t = Thread {
                log("새 Thread 실행 시작")
                Thread.sleep(400)
                log("새 Thread 작업 완료")
            }
            t.name = "BasicThread"

            log("state (start 전): ${t.state}")   // NEW
            t.start()
            log("state (start 직후): ${t.state}") // RUNNABLE
            t.join()
            log("state (join 후): ${t.state}")    // TERMINATED
        }.apply { name = "BasicThread-Launcher" }.start()
    }

    // 멀티 Thread 동시 실행
    fun runMultiThread() {
        Thread {
            log("=== [2] 멀티 Thread 동시 실행 ===")
            val threads = (1..4).map { i ->
                Thread {
                    log("Worker-$i 시작")
                    //Thread.sleep(150L * i)
                    Thread.sleep(Random.nextLong(100, 600))
                    log("Worker-$i 완료")
                }.apply { name = "Worker-$i" }
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }
            log("모든 Thread 종료")
        }.apply { name = "MultiThread-Launcher" }.start()
    }

}
