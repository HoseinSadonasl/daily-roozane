package com.abc.daily.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abc.daily.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_note_fragment)
    }
}