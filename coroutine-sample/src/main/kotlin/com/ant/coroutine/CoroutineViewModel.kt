package com.ant.coroutine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoroutineViewModel : ViewModel() {

    companion object {
        private const val TAG = "CoroutineDemo"
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

    // Thread 10개 — 각각 새 스레드 생성
    fun runThreads() {
        log("=== Thread 10개 ===")
        repeat(10) { i ->
            Thread {
                log("Thread-$i 실행")
            }.apply { name = "MyThread-$i" }.start()
        }
    }

    // launch 10개 — 스레드 새로 안 만들고 메인에서 처리
    fun runCoroutines() {
        log("=== launch 10개 ===")
        repeat(10) { i ->
            viewModelScope.launch {
                log("launch-$i 실행")
                delay(100)
                log("launch-$i 끝")
            }
        }
    }
}
