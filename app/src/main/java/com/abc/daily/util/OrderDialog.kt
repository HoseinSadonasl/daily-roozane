package com.abc.daily.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.RadioButton
import com.abc.daily.R
import com.google.android.material.button.MaterialButton

class OrderDialog(activity: Activity, defaultValues: Pair<Int, Int>, var orderCallback: (order: Pair<Int, Int>) -> Unit) : Dialog(activity) {

    companion object {
        const val ORDER_DSC = 100
        const val ORDER_ASC = 200
        const val ORDER_BY_DATE = 1
        const val ORDER_BY_NAME = 2
    }

    lateinit var sortRbAsc: RadioButton
    lateinit var sortRbDsc: RadioButton
    lateinit var sortRbName: RadioButton
    lateinit var sortRbDate: RadioButton
    lateinit var sortSubmit: MaterialButton
    lateinit var sortCancel: MaterialButton

    init {
        window!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
        setContentView(R.layout.layout_custom_dialog_notesort)
        init()
        initListeners()
        setDefaults(defaultValues)
    }

    private fun init() {
        sortRbAsc = findViewById(R.id.sortrb_ascending_notesFragment)
        sortRbDsc = findViewById(R.id.sortrb_descending_notesFragment)
        sortRbName = findViewById(R.id.sortrb_byname_notesFragment)
        sortRbDate = findViewById(R.id.sortrb_bydate_notesFragment)
        sortSubmit = findViewById(R.id.positive)
        sortCancel = findViewById(R.id.negative)
        sortSubmit.text = context.getString(R.string.orderDialog_submitBtn)
        sortCancel.text = context.getString(R.string.orderDialog_cancelBtn)

    }

    private fun setDefaults(defaultValues: Pair<Int, Int>) {
        if (defaultValues.first == ORDER_DSC)
            sortRbDsc.isChecked = true
        else
            sortRbAsc.isChecked = true

        when (defaultValues.second) {
            ORDER_BY_DATE -> sortRbDate.isChecked = true
            ORDER_BY_NAME -> sortRbName.isChecked = true
        }
    }

    private fun initListeners() {
        sortSubmit.setOnClickListener {
            sortNotes()
            this.dismiss()
        }
        sortCancel.setOnClickListener { this.dismiss() }
    }

    private fun sortNotes() {
        if (sortRbAsc.isChecked) {
            if (sortRbName.isChecked) orderCallback(ORDER_ASC to ORDER_BY_NAME)
            if (sortRbDate.isChecked) orderCallback(ORDER_ASC to ORDER_BY_DATE)
        }
        if (sortRbDsc.isChecked) {
            if (sortRbName.isChecked) orderCallback(ORDER_DSC to ORDER_BY_NAME)
            if (sortRbDate.isChecked) orderCallback(ORDER_DSC to ORDER_BY_DATE)
        }
    }
}