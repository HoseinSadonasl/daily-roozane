package com.abc.daily.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    List<NoteObjects> objects;
    Context context;

    public NotesAdapter(Context context, List<NoteObjects> objects) {
        this.context = context;
        this.objects = objects;
        app.l(objects.size()+"SSS");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(objects.get(position).getNoteTitle());
        holder.content.setText(objects.get(position).getNoteContent());
        holder.date.setText(objects.get(position).getNoteDate());


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parent;
        AppCompatTextView title, content, date;
        AppCompatImageView more, reminder;/**/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            more = itemView.findViewById(R.id.more);
            reminder = itemView.findViewById(R.id.reminder);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoteObjects object = objects.get(getAdapterPosition());
                    Intent intent = new Intent(context, AddReadNote.class);
                    intent.putExtra(db.Note.NOTE_ID, object.getId());
                    context.startActivity(intent);
                    app.l("POSITION " + object.getId());
                }
            });
        }
    }

}
