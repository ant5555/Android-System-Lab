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

    // Race Condition — 동기화 없이 공유 변수 증가
    fun runRaceCondition() {
        Thread {
            log("=== [3] Race Condition ===")
            var counter = 0
            val threads = (1..3).map { i ->
                val t = Thread {
                    repeat(1000) { counter++ }
                    log("Thread-$i 완료, counter=$counter")
                }
                t.name = "Race-$i"
                t
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }
            log("최종 counter = $counter  (기댓값: 3000)")
        }.apply { name = "RaceCondition-Launcher" }.start()
    }

    // Synchronized — lock으로 임계 구역 보호
    fun runSynchronized() {
        Thread {
            log("=== [4] Synchronized ===")
            var counter = 0
            val lock = Any()
            val threads = (1..3).map { i ->
                val t = Thread {
                    repeat(1000) { synchronized(lock) { counter++ } }
                    log("Thread-$i 완료")
                }
                t.name = "Sync-$i"
                t
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }
            log("최종 counter = $counter  (기댓값: 3000)")
        }.apply { name = "Synchronized-Launcher" }.start()
    }

}
