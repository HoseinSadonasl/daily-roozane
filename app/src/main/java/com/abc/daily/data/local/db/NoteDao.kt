package com.abc.daily.data.local.db

import androidx.room.*
import com.abc.daily.domain.model.note.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM Note WHERE id= :id")
    fun getNote(id: Int): Note?

    @Query("SELECT * FROM Note")
    fun getNoteList(): Flow<List<Note>>

}