package com.abc.daily.Adapters;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.daily.AddReadNote;
import com.abc.daily.R;

import java.util.List;

import com.abc.daily.Objects.NoteObjects;

import com.abc.daily.app.app;
import com.abc.daily.app.db;
import com.abc.daily.fragments.BottomSheetFragment;
import com.abc.daily.interfaces.ModifyObject;
import com.abc.daily.interfaces.NotificationObject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    List<NoteObjects> objects;
    AppCompatActivity context;

    public NotesAdapter(AppCompatActivity context, List<NoteObjects> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(objects.get(position).getNoteTitle());
        holder.content.setText(objects.get(position).getNoteContent());
        holder.date.setText(objects.get(position).getNoteDate());
        String dateStr = objects.get(position).getReminderDate();
        String timeStr = objects.get(position).getReminderTime();
        if (!dateStr.equals("Today") && !timeStr.equals("At:")){
            holder.reminder.setVisibility(View.VISIBLE);
            holder.reminderIc.setVisibility(View.VISIBLE);
            holder.reminderDateTime.setVisibility(View.VISIBLE);
            holder.reminderDateTime.setText(objects.get(position).getReminderDate() + " " + objects.get(position).getReminderTime());
        } else {
            holder.reminder.setVisibility(View.GONE);
            holder.reminderIc.setVisibility(View.GONE);
            holder.reminderDateTime.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout parent;
        AppCompatImageView reminderIc;
        MaterialTextView title, content, date, reminderDateTime;
        LinearLayout reminder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            date = itemView.findViewById(R.id.date);
            reminder = itemView.findViewById(R.id.reminder);
            reminderIc = itemView.findViewById(R.id.reminderIc);
            reminderDateTime = itemView.findViewById(R.id.reminderDaeTime);
            parent.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            NoteObjects object = objects.get(getAdapterPosition());
            Intent intent = new Intent(context, AddReadNote.class);
            intent.putExtra(db.Note.NOTE_ID, object.getId());
            context.startActivity(intent);
        }
    }

}
