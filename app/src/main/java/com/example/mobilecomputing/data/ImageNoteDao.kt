package com.example.mobilecomputing.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface ImageNoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImageNote(imageNote: ImageNote)

    @Update
    suspend fun updateImageNote(imageNote: ImageNote)

    @Delete
    suspend fun deleteImageNote(imageNote: ImageNote)

    @Query("SELECT * from images WHERE id= :id")
    fun getImageNote(id: Int): ImageNote
}