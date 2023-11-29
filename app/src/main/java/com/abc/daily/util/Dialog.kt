package com.abc.daily

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.button.MaterialButton

class Dialog(
    context: Context,
    val onPositiveCallback: (dialog: com.abc.daily.Dialog) -> Unit,
    val onNegativeCallback: (dialog: com.abc.daily.Dialog) -> Unit,
) : Dialog(context) {

    private lateinit var positive: MaterialButton
    private lateinit var negative: MaterialButton
    private lateinit var alertTitle: AppCompatTextView
    private lateinit var alertSubtitle: AppCompatTextView
    private lateinit var textInput: AppCompatEditText
    private lateinit var imageView: AppCompatImageView

    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.custom_dialog)
        init()
    }

    private fun init() {
        positive = findViewById(R.id.positive)
        negative = findViewById(R.id.negative)
        alertTitle = findViewById(R.id.alertTitle)
        alertSubtitle = findViewById(R.id.alertSubtitle)
        textInput = findViewById(R.id.textInput)
        imageView = findViewById(R.id.image_view)
        positive.setOnClickListener { onPositiveCallback(this) }
        negative.setOnClickListener { onNegativeCallback(this) }
    }



    fun setTitle(title: String?) {
        title?.let {
            alertTitle.visibility = View.VISIBLE
            alertTitle.text = it
        }
    }

    fun setDescription(description: String?) {
        description?.let {
            alertSubtitle.visibility = View.VISIBLE
            alertSubtitle.text = it
        }
    }

    fun setPositiveButtonText(text: String?) {
        text?.let {
            positive.visibility = View.VISIBLE
            positive.text = it

        }
    }

    fun setNegativeButtonText(text: String?) {
        text?.let {
            negative.visibility = View.VISIBLE
            negative.text = it
        }
    }

    fun textInput(hint: String?) {
        hint?.let {
            textInput.visibility = View.VISIBLE
            textInput.hint = hint
        }
    }

    val inputText: String = textInput.text.toString().ifEmpty { "" }

    fun setLogoImg(resId: Int?) {
        resId?.let {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(it)
        }
    }
}