package com.example.mobilecomputing.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AudioNoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAudioNote(audioNote: AudioNote)

    @Update
    suspend fun updateAudioNote(audioNote: AudioNote)

    @Delete
    suspend fun deleteAudioNote(audioNote: AudioNote)

    @Query("SELECT * from audios WHERE id= :id")
    fun getAudioNote(id: Int): AudioNote
}