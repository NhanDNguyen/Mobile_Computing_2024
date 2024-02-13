package com.example.mobilecomputing.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "Title",
    val date: String = "",
    val body: String = "",
    val imageData: Bitmap? = null,
    val audios: Int = 0,
)

@Entity(
    tableName = "audios",
    foreignKeys = [ForeignKey(entity = Note::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Audio(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: Int,
    val fileName: String,
    val filePath: String,
    val timestamp: Long,
    val duration: String,
    val ampsPath: String
)