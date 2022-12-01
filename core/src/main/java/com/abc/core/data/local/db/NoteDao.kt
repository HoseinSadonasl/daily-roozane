package com.abc.core.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abc.core.data.local.note.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM Note WHERE id= :id")
    fun getNote(id: Int)

    @Query("SELECT * FROM Note")
    fun getNoteList(id: Int)

}