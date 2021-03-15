package com.abc.daily.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.abc.daily.Objects.SpinnerObjects;
import com.abc.daily.R;
import com.abc.daily.app.Application;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerObjects> {
    List<SpinnerObjects> list;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerObjects> objects) {
        super(context, resource, objects);
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, parent);
    }

    private View createView(int position, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) Application.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.spinner_layout, parent, false);
        AppCompatImageView icon = view.findViewById(R.id.icon);
        MaterialTextView name = view.findViewById(R.id.name);
        icon.setImageResource(list.get(position).getIcon());
        name.setText(list.get(position).getSpinnerObjName());
        return view;
    }

}
