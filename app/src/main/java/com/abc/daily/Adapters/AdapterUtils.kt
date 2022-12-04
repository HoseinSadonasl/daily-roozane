package com.abc.daily.Adapters

import androidx.recyclerview.widget.DiffUtil
import com.abc.daily.domain.model.note.Note

object AdapterUtils {

    val diffUtil = object: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

    }

}