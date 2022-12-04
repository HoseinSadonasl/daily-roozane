package com.abc.daily.Adapters

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.abc.daily.databinding.NotesListItemBinding
import com.abc.daily.domain.model.note.Note

class NoteViewHolder(
    val binding: NotesListItemBinding
) : ViewHolder(binding.root) {

    fun bind(item: Note) {
        item.let {
            binding.title.text = item.title
            binding.content.text = item.description
            binding.textViewDateNotesFragment.text = "textViewDateNotesFragment"
            binding.reminderDaeTime.text = "reminderDaeTime"
        }
    }
}