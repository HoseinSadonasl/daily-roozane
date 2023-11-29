package com.abc.daily.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.RadioButton
import com.abc.daily.R
import com.abc.daily.domain.util.NoteOrder
import com.abc.daily.domain.util.OrderType
import com.google.android.material.button.MaterialButton

class NoteSortDialog(
    activity: Activity,
    var noteOrder: (sort: NoteOrder) -> Unit
) : Dialog(activity) {

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
    }

    private fun init() {
        sortRbAsc = findViewById(R.id.sortrb_ascending_notesFragment)
        sortRbDsc = findViewById(R.id.sortrb_descending_notesFragment)
        sortRbName = findViewById(R.id.sortrb_byname_notesFragment)
        sortRbDate = findViewById(R.id.sortrb_bydate_notesFragment)
        sortSubmit = findViewById(R.id.positive)
        sortCancel = findViewById(R.id.negative)
        sortSubmit.text = context.getString(R.string.ok)
        sortCancel.text = context.getString(R.string.cancel)
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
            if (sortRbName.isChecked) {
                noteOrder(NoteOrder.ByName(OrderType.Ascending))
            }
            if (sortRbDate.isChecked) {
                noteOrder(NoteOrder.ByDate(OrderType.Ascending))
            }
        }
        if (sortRbDsc.isChecked) {
            if (sortRbName.isChecked) {
                noteOrder(NoteOrder.ByName(OrderType.Descending))
            }
            if (sortRbDate.isChecked) {
                noteOrder(NoteOrder.ByName(OrderType.Descending))
            }
        }
    }
}