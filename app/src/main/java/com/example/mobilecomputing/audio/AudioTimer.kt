package com.example.mobilecomputing.audio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioTimer {

    var formattedTime by mutableStateOf("00:00:000")

    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private var isActive = false

    private var timeMillis = 0L
    private var lastTimestamp = 0L

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        if(isActive) return
        coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@AudioTimer.isActive = true
            while(this@AudioTimer.isActive) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = formatTime(timeMillis)
            }
            if (!this@AudioTimer.isActive) {
                this.cancel()
            }
        }
    }

    fun pause() {
        isActive = false
    }

    fun reset() {
        coroutineScope.cancel()
        timeMillis = 0L
        lastTimestamp = 0L
        formattedTime = "00:00:000"
        isActive = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTime(timeMillis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss:SSS",
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }
}