package com.abc.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abc.core.data.local.note.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "daily_notes_db"
    }

}