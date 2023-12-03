package com.abc.daily.data.local.db

import androidx.room.*
import com.abc.daily.domain.model.note.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(note: Note): Long

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM Note WHERE id= :id")
    suspend fun getNote(id: Int): Note?

    @Query("SELECT * FROM Note")
    fun getNoteList(): Flow<List<Note>>

}