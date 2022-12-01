package com.abc.core.data.local.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abc.core.util.NoteStateEnum

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String? = null,
    var description: String? = null,
    var createdAt: String? = null,
    var modifiedAt: String? = null,
    var type: String? = null,
    var remindAt: String? = null,
    val state: NoteStateEnum? = null
)
