package com.example.mobilecomputing.audio

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}