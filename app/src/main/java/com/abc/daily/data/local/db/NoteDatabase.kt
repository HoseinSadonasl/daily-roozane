package com.abc.daily.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abc.daily.domain.model.note.Note
import com.abc.daily.domain.util.TypeConvertors

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConvertors::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "daily_notes_db"
    }

}