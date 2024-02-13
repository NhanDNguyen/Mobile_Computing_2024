package com.example.mobilecomputing.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * from notes WHERE id= :id")
    fun getNote(id: Int): Note

    @Query("SELECT * from notes ORDER BY title ASC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAudio(audio: Audio)

    @Update
    suspend fun updateAudio(audio: Audio)

    @Delete
    suspend fun deleteAudio(audio: Audio)

    @Query("SELECT * from audios WHERE id= :id")
    fun getAudio(id: Int): Audio

    @Query("SELECT * from audios ORDER BY fileName ASC")
    fun getAllAudios(): Flow<List<Audio>>

    @Query("SELECT * from audios WHERE userId= :userId ORDER BY timestamp ASC")
    fun getAllAudiosByUserId(userId: Int): Flow<List<Audio>>
}