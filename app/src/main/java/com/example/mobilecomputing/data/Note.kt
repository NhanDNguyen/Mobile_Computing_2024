package com.example.mobilecomputing.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note (
    // General
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val date: Long = Date().time,
    val type: String = "",

    // Text
    val body: String = "",

    // Image
    val imageData: Bitmap? = null,

    // Audio
    val filePath: String = "",
    val durationMillis: Long = 0L,
    val ampsPath: String = "",
    // Drawing
)














