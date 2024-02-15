package com.example.mobilecomputing.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Note::class,
        TextNote::class,
        AudioNote::class,
        ImageNote::class,
        DrawingNote::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun textNoteDao(): TextNoteDao

    abstract fun audioNoteDao(): AudioNoteDao

    abstract fun imageNoteDao(): ImageNoteDao

    abstract fun drawingNoteDao(): DrawingNoteDao
}