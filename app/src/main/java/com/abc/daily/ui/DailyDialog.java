package com.abc.daily.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.abc.daily.R;
import com.google.android.material.button.MaterialButton;

public class DailyDialog extends Dialog {

    private View.OnClickListener negativeClickListener;
    private View.OnClickListener positiveClickListener;
    private String positiveText = "";
    private String negativeText = "";
    private String titleText    = "";
    private String subtitleText = "";
    private int textInputVisibility;

    MaterialButton positive, negative;
    AppCompatTextView alertTitle, alertSubtitle;
    AppCompatEditText textInput;

    public DailyDialog(@NonNull Context context, String positiveText, String negativeText, String titleText, String subtitleText, int textInputVisibility) {
        super(context, R.style.mDialog);
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.titleText    = titleText;
        this.subtitleText = subtitleText;
        this.textInputVisibility = textInputVisibility;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        textInput.setVisibility(textInputVisibility == 0 ? View.GONE : View.VISIBLE);

        positive.setOnClickListener(positiveClickListener);
        negative.setOnClickListener(negativeClickListener);

        positive.setText(positiveText);
        negative.setText(negativeText);

        alertTitle.setText(titleText);
        alertSubtitle.setText(subtitleText);


    }

    public void onPositiveButtonClickListener(View.OnClickListener onClickListener) {
        this.positiveClickListener = onClickListener;
        if (negative != null) {
            negative.setOnClickListener(onClickListener);
        }
    }

    public void setOnNegativeButtonClickListener(View.OnClickListener onClickListener) {
        this.negativeClickListener = onClickListener;
        if (negative != null) {
            negative.setOnClickListener(onClickListener);
        }
    }

    public String getInputText() {
        textInputVisibility = 1;
        textInput.setVisibility(View.VISIBLE);
        textInput.requestFocus();
        return textInput.getText().toString().length() == 0 ? "tehran" : textInput.getText().toString();
    }

}
