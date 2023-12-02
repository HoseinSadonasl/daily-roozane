package com.abc.daily.Adapters

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.abc.daily.R
import com.abc.daily.databinding.NotesListItemBinding
import com.abc.daily.domain.model.note.Note
import com.abc.daily.util.DateUtil
import java.util.Date

class NoteViewHolder(
    val binding: NotesListItemBinding,
    val onItemClick: (id: Int) -> Unit
) : ViewHolder(binding.root) {

    fun bind(item: Note) {
        item?.let {
            binding.root.setOnClickListener { item.id?.let { it1 -> onItemClick(it1) } }
            binding.title.text = item.title
            if (it.description.isNotBlank()) {
                binding.content.apply {
                    visibility = View.VISIBLE
                    text = it.description
                }
            }
            binding.textViewDateNotesFragment.text = DateUtil.toPersianDateAndTime(item.modifiedAt.toString(), binding.root.context)
            it.remindAt?.let {
                binding.reminderIc.visibility = View.VISIBLE
                binding.reminderDaeTime.text = DateUtil.alarmToPersianDateAndTime(it)
                if (it.toLong() < Date().time) binding.reminderIc.imageTintList = ColorStateList.valueOf(binding.root.context.getColor(R.color.text_secondary))
            }
        }
    }
}