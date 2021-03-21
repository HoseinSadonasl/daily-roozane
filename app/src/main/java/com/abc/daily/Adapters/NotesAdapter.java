package com.abc.daily.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.daily.AddReadNote;
import com.abc.daily.R;

import java.util.List;

import com.abc.daily.Objects.NoteObjects;

import com.abc.daily.app.db;
import com.abc.daily.app.spref;
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
            String color = spref.get(spref.tags.THEME).getString(spref.Theme.THEME_COLOR, spref.Theme.DEFAULT_THEME_COLOR);;
            switch (color) {
                case "purple" : {
                    holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_purple));
                    holder.reminderIc.setColorFilter(context.getResources().getColor(R.color.deep_purple_400));
                    break;
                }
                case "red" : {
                    holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_red));
                    holder.reminderIc.setColorFilter(context.getResources().getColor(R.color.red_500));
                    break;
                }
                case "orange" : {
                    holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_orange));
                    holder.reminderIc.setColorFilter(context.getResources().getColor(R.color.orange_400));
                    break;
                }
                case "blue" : {
                    holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_blue));
                    holder.reminderIc.setColorFilter(context.getResources().getColor(R.color.blue_500));
                    break;
                }
                case "teal" : {
                    holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_teal));
                    holder.reminderIc.setColorFilter(context.getResources().getColor(R.color.teal_500));
                    break;
                }
                case "green" : {
                    holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_green));
                    break;
                }
                default: holder.reminder.setBackground(ContextCompat.getDrawable(context, R.drawable.border_back_teal));
            }
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
