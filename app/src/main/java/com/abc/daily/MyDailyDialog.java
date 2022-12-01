package com.abc.daily;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.abc.daily.R;
import com.google.android.material.button.MaterialButton;

import com.abc.daily.interfaces.DialogInterface;

public class MyDailyDialog extends Dialog implements View.OnClickListener {

    DialogInterface dialogInterface;
    private String positiveText = "";
    private String negativeText = "";
    private String titleText    = "";
    private String subtitleText = "";
    private int textInputVisibility;
    private int alertSubtitleVisibility;
    int imageVisibility;

    MaterialButton positive, negative;
    AppCompatTextView alertTitle, alertSubtitle;
    AppCompatEditText textInput;
    AppCompatImageView image_view;


    public MyDailyDialog(@NonNull Context context, String positiveText, String negativeText, String titleText, String subtitleText,
                         int alertSubtitleVisibility, int textInputVisibility, DialogInterface dialogInterface, int imageVisibility) {
        super(context);
        this.dialogInterface=dialogInterface;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.titleText    = titleText;
        this.subtitleText = subtitleText;
        this.textInputVisibility = textInputVisibility;
        this.alertSubtitleVisibility = alertSubtitleVisibility;
        this.imageVisibility = imageVisibility;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_dialog);
        init();
    }


    private void init() {

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        alertTitle = findViewById(R.id.alertTitle);
        alertSubtitle = findViewById(R.id.alertSubtitle);
        textInput = findViewById(R.id.textInput);
        image_view = findViewById(R.id.image_view);

        textInput.setVisibility(textInputVisibility == 0 ? View.GONE : View.VISIBLE);
        alertSubtitle.setVisibility(alertSubtitleVisibility == 0 ? View.GONE : View.VISIBLE);
        image_view.setVisibility(imageVisibility == 0 ? View.GONE : View.VISIBLE);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        positive.setText(positiveText);
        negative.setText(negativeText);
        alertTitle.setText(titleText);
        alertSubtitle.setText(subtitleText);


    }

    public String getInputText() {
        textInputVisibility = 1;
        textInput.setVisibility(View.VISIBLE);
        alertSubtitleVisibility = 1;
        alertSubtitle.setVisibility(View.VISIBLE);
        textInput.requestFocus();
        return textInput.getText().toString().length() == 0 ? "" : textInput.getText().toString();
    }

    public void  setLogoImg(int image){
        imageVisibility = 1;
        image_view.setVisibility(View.VISIBLE);
        image_view.setImageResource(image);
    }


    @Override
    public void onClick(View v) {
        if (v == positive) {
            dialogInterface.onPositiveClick();
        } else {
            dialogInterface.onNegativeClick();
        }
    }
}
