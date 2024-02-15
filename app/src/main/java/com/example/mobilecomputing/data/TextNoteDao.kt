package com.example.mobilecomputing.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TextNoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTextNote(textNote: TextNote)

    @Update
    suspend fun updateTextNote(textNote: TextNote)

    @Delete
    suspend fun deleteTextNote(textNote: TextNote)

    @Query("SELECT * from texts WHERE id= :id")
    fun getTextNote(id: Long): Flow<TextNote>
}