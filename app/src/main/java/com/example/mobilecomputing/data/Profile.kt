package com.example.mobilecomputing.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name: String,
    val info: String,
    val imageData: Bitmap? = null,
    val imageDescription: String = ""
)