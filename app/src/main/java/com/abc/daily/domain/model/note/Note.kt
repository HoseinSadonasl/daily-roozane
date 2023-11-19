package com.abc.daily.domain.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abc.daily.util.NoteStateEnum

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    var title: String,
    var description: String,
    var createdAt: String? = null,
    var modifiedAt: String? = null,
    var type: String? = null,
    var remindAt: String? = null,
    var state: NoteStateEnum? = null,
    var items: List<Item>? = listOf()
)
