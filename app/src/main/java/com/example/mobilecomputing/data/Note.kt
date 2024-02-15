package com.example.mobilecomputing.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
open class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val date: Long = Date().time,
    val type: String = ""
)

@Entity(tableName = "audios")
data class AudioNote(
    @PrimaryKey
    val id: Long = 0,
    val filePath: String = "",
    val timestamp: Long = 0,
    val duration: String = "",
    val ampsPath: String = ""
)

@Entity(tableName = "images")
data class ImageNote(
    @PrimaryKey
    val id: Long = 0,
    val imageData: Bitmap? = null,
)

@Entity(tableName = "texts")
data class TextNote(
    @PrimaryKey
    val id: Long = 0,
    val body: String = ""
)

@Entity(tableName = "drawings")
data class DrawingNote(
    @PrimaryKey
    val blabal: Int
)













