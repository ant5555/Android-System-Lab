package com.ant.thread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        viewModelScope.launch(Dispatchers.IO) {
            log("=== [1] 기본 Thread 생성 ===")
            log("호출 스레드: ${Thread.currentThread().name}")

            val t = Thread {
                log("새 Thread 실행 시작")
                Thread.sleep(400)
                log("새 Thread 작업 완료")
            }.apply { name = "BasicThread" }

            log("state (start 전): ${t.state}")   // NEW
            t.start()
            log("state (start 직후): ${t.state}") // RUNNABLE
            t.join()
            log("state (join 후): ${t.state}")    // TERMINATED
        }
    }

}
